import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// TODO Warnings

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

		// TODO Delete
		String file = "apple.html";
		index.copyPositions("test", file);

		// handle path argument
		if (arguments.hasFlag("-path")) { // TODO Remove this outer if
			Path path = null;
			if (arguments.getString("-path") != null) {
				path = Paths.get(arguments.getString("-path"));
				InvertedIndexBuilder.buildIndex(path, index);
			}
		}

		// handle index argument
		if (arguments.hasFlag("-index")) {
			// TODO String path = arguments.getString("-index", "index.json");
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

		

		// this needs to be global used in queries search and write
		ArrayList<SearchResult> output = new ArrayList<SearchResult>();

		// handle query argument
		if (arguments.hasFlag("-query")) {
			String queryArgPayload = arguments.getString("-query");
			if (queryArgPayload != null) {
				// we were given -query argument with value
				Path path = Paths.get(queryArgPayload);

				// TODO
				ArrayList<String> queryWords = new ArrayList<String>();
				// for each line you read clean up and prep words
				Query query = new Query(); // re think this
				queryWords = query.buildFromFile(path);
				
				String[] batchQuery;
				// determine search type exact/partial
				if (arguments.hasFlag("-exact")) { // perform exact search
					for (int i = 0; i < queryWords.size(); i++) {
						batchQuery = queryWords.get(i).split(" ");
						output.add(index.exactSearch(batchQuery));
					}
				} else { // perform partial search
					for (int i = 0; i < queryWords.size(); i++) {
						batchQuery = queryWords.get(i).split(" ");
						output.add(index.partialSearch(batchQuery));
					}
				}
			}
		}

		// handle results argument
		if (arguments.hasFlag("-results")) {
			String resultArgPayload = arguments.getString("-results");
			if (resultArgPayload != null) {
				// we were given -results argument with value
				Path path = Paths.get(resultArgPayload);
				index.saveResults(output, path);
			} else {
				// we were given -results argument with no value
				Path path = Paths.get("results.json");
				index.saveResults(output, path);
			}
		}
	}

}