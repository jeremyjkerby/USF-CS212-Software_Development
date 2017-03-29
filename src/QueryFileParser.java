import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// TODO Javadoc

public class QueryFileParser {

	private final Map<String, List<SearchResult>> map;
	private final InvertedIndex index;

	public QueryFileParser(InvertedIndex index) {
		this.index = index;
		map = new TreeMap<String, List<SearchResult>>();
	}

	public void parseQueryFile(Path path, boolean exact) {
		// handle search in ii file
		// handle how to write search to file

		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

			String line;
			while ((line = reader.readLine()) != null) {
				// for each line in file clean and separate into words
				String cleanedTemp[] = WordParser.parseWords(line);

				Arrays.sort(cleanedTemp);

				// perform search
				List<SearchResult> results;
				if (exact == true) {
					results = index.exactSearch(cleanedTemp);
					map.put(String.join(" ", cleanedTemp), results);
				} else {
					results = index.partialSearch(cleanedTemp);
					if (String.join(" ", cleanedTemp).compareTo("") != 0)
					map.put(String.join(" ", cleanedTemp), results);
				}

			}
		} catch (IOException e) {
			System.out.println("Unable to read query file");
		}

	}

	public void toJSON(Path path) {
		
		try {
			JSONWriter.searchResults(map, path);
		} catch (IOException e) {
			System.out.println("Unable to write results to file");
		}
	}

}
