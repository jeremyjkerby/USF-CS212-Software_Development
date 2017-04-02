import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Execute this file to run the entire program.
 * 
 * @author Jeremy Kerby
 *
 */
public class Driver {

	/**
	 * This is the main method to call functionality of application.
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
			// we were given -index argument with value
			Path path = Paths.get(arguments.getString("-index", "index.json"));
			index.toJSON(path);
		}

		// handle query argument
		QueryFileParser query = new QueryFileParser(index);
		if (arguments.hasFlag("-query")) {
			String queryPath = arguments.getString("-query");
			if (queryPath != null) {
				// we were given -query argument with value
				Path path = Paths.get(queryPath);
				// determine search type
				query.parseQueryFile(path, arguments.hasFlag("-exact"));
			}
		}

		// handle results argument
		if (arguments.hasFlag("-results")) {
			// we were given -results argument with no value
			Path path = Paths.get(arguments.getString("-results", "results.json"));
			query.toJSON(path);
		}
	}

}