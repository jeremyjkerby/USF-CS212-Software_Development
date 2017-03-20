import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Execute this file to run the entire program
 * 
 * @author Jeremy Kerby
 *
 */
public class Driver {

	/**
	 * This is the main method to call functionality of application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ArgumentMap arguments = new ArgumentMap(args);
		InvertedIndex index = new InvertedIndex();

		String file = "apple.html";
		index.copyPositions("test", file);

		// handle path argument
		if (arguments.hasFlag("-path")) {
			Path path = null;
			if (arguments.getString("-path") != null) {
				path = Paths.get(arguments.getString("-path"));
				InvertedIndexBuilder.buildIndex(path, index);
			}
		}

		// handle index argument
		if (arguments.hasFlag("-index")) {
			String indexArgPayload = arguments.getString("-index");
			if (indexArgPayload == null) {
				// we were given -index argument with no value
				Path path = Paths.get("index.json");
				index.toJSON(path);
			} else {
				// we were given -index argument with value
				Path path = Paths.get(indexArgPayload);
				index.toJSON(path);
			}
		}

		// handle query argument
		ArrayList<SearchResult> output = new ArrayList<SearchResult>();
		if (arguments.hasFlag("-query")) {
			String queryArgPayload = arguments.getString("-query");
			if (queryArgPayload != null) {
				// we were given -query argument with value
				Path path = Paths.get(queryArgPayload);

				ArrayList<String> queryWords = new ArrayList<String>();
				// for each line you read clean up and prep words
				Query query = new Query();
				try {
					queryWords = query.buildFromFile(path);
				} catch (IOException e) {
					System.out.println("Unable to read query file");
				}

				String[] batchQuery;

				// determine search, only use if query enabled
				if (arguments.hasFlag("-exact")) { // perform exact search
					// batchQuery holds subset search (words per line to search)
					for (int i = 0; i < queryWords.size(); i++) {
						batchQuery = queryWords.get(i).split(" ");
						output.add(index.exactSearch(batchQuery));
					}
				} else { // perform partial search
					// batchQuery holds subset search (words per line to search)
					for (int i = 0; i < queryWords.size(); i++) {
						batchQuery = queryWords.get(i).split(" ");
						output.add(index.partialSearch(batchQuery));
					}

				}
			} else {
				// we were given -query argument with no value
				System.out.println("You gave us an empty query");
			}
		} else {
			// we were not given -query argument with value
			System.out.println("No search being performed");
		}

		// handle results argument
		if (arguments.hasFlag("-results")) {
			String resultArgPayload = arguments.getString("-results");
			if (resultArgPayload != null) {
				// we were given -index argument with value
				Path path = Paths.get(resultArgPayload);
				index.saveResults(output, path);
			} else {
				// we were given -index argument with no value
				Path path = Paths.get("results.json");
				index.saveResults(output, path);
			}
		}
	}

}