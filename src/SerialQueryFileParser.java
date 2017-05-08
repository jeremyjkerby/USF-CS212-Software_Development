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
public class SerialQueryFileParser implements QueryFileParserInterface {

	private final Map<String, List<SearchResult>> map;
	private final InvertedIndex index;

	/**
	 * Initialize the QueryFileParser. Requires an InvertedIndex.
	 * 
	 * @param index
	 *            InvertedIndex that will be used to search
	 */
	public SerialQueryFileParser(InvertedIndex index) {
		this.index = index;
		map = new TreeMap<String, List<SearchResult>>();
	}

	/**
	 * Serial Code. Read from file line by line. Clean and sort words. Determine
	 * then perform query.
	 * 
	 * @param path
	 *            location of where query file is
	 * @param exact
	 *            true if exact false if partial
	 */
	@Override
	public void parseQueryFile(Path path, boolean exact, WorkQueue queue) {
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				// for each line in file clean and separate into words
				String cleanedTemp[] = WordParser.parseWords(line);

				if (cleanedTemp.length == 0) {
					continue;
				}

				Arrays.sort(cleanedTemp);
				// perform search
				List<SearchResult> results;
				if (exact == true) {
					results = index.exactSearch(cleanedTemp);
					map.put(String.join(" ", cleanedTemp), results);
				} else {
					results = index.partialSearch(cleanedTemp);
					map.put(String.join(" ", cleanedTemp), results);
				}
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
			JSONWriter.searchResults(map, path);
		} catch (IOException e) {
			System.out.println("Unable to write results to file");
		}
	}

}
