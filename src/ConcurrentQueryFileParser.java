import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Reads data to query. Determines what search to take. Executes search. Writes
 * results to file.
 * 
 */
public class ConcurrentQueryFileParser implements QueryFileParserInterface {

	private final Map<String, List<SearchResult>> map;
	private final InvertedIndex index; // TODO Use the thread-safe version

	/**
	 * Initialize the QueryFileParser. Requires an InvertedIndex.
	 * 
	 * @param index
	 *            InvertedIndex that will be used to search
	 */
	public ConcurrentQueryFileParser(InvertedIndex index) { // TODO Use the thread-safe version, add a WorkQueue parameter
		this.index = index;
		map = new TreeMap<String, List<SearchResult>>();
	}

	/**
	 * Synchronized code. Read from file line by line. Clean and sort words.
	 * Determine then perform query.
	 * 
	 * @param path
	 *            location of where query file is
	 * @param exact
	 *            true if exact false if partial
	 * @param queue
	 *            use to queue tasks
	 */
	@Override
	public void parseQueryFile(Path path, boolean exact, WorkQueue queue) {
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				queue.execute(new SearchTask(line, exact));
			}
		} catch (IOException e) {
			System.out.println("Unable to read query file");
		}
	}

	/**
	 * Writes map of results to given path.
	 * 
	 * @param path
	 *            where the result map will be written to
	 */
	@Override
	public void toJSON(Path path) {
		try {
			synchronized (index) { // TODO synchronized (map)
				JSONWriter.searchResults(map, path);
			}
		} catch (IOException e) {
			System.out.println("Unable to write results to file");
		}
	}

	/**
	 * Runnable task that performs search
	 */
	private class SearchTask implements Runnable {

		private String line;
		private boolean exact;

		public SearchTask(String line, boolean exact) {
			this.line = line;
			this.exact = exact;
		}

		@Override
		public void run() {
			String cleanedTemp[] = WordParser.parseWords(line);

			if (cleanedTemp.length != 0) {

				Arrays.sort(cleanedTemp);
				// perform search
				List<SearchResult> results;
				String data = String.join(" ", cleanedTemp);
				if (exact == true) {
					results = index.exactSearch(cleanedTemp);
				} else {
					results = index.partialSearch(cleanedTemp);
				}

				synchronized (index) { // TODO synchronized (map)
					map.put(data, results);
				}

			}
		}

	}

}
