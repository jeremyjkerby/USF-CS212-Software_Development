import java.nio.file.Path;
import java.nio.file.Paths;

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

		// handle path argument
		if (arguments.getString("-path") != null) {
			Path path = Paths.get(arguments.getString("-path"));
			InvertedIndexBuilder.buildIndex(path, index);
		}

		// handle index argument
		if (arguments.hasFlag("-index")) {
			String indexPath = arguments.getString("-index", "index.json");
			if (indexPath == null) {
				// we were given -index argument with no value
				Path path = Paths.get("index.json");
				index.toJSON(path);
			} else {
				// we were given -index argument with value
				Path path = Paths.get(indexPath);
				index.toJSON(path);
			}
		}

		// handle query argument
		QueryFileParser query = new QueryFileParser(index);
		if (arguments.hasFlag("-query")) {
			String queryPath = arguments.getString("-query");
			if (queryPath != null) {
				// we were given -query argument with value
				Path path = Paths.get(queryPath);
				
				// determine search type
				if (arguments.hasFlag("-exact")) { // perform exact search
					query.parseQueryFile(path, true);
				} else { // perform partial search
					query.parseQueryFile(path, false);
				}
			}
		}

		// handle results argument
		if (arguments.hasFlag("-results")) {
			String resultsPath = arguments.getString("-results");
			if (resultsPath != null) {
				// we were given -results argument with value
				Path path = Paths.get(resultsPath);
				query.toJSON(path);
			} else {
				// we were given -results argument with no value
				Path path = Paths.get("results.json");
				query.toJSON(path);
			}
		}
	}

}