import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
				try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
					index.toJSON(path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// we were given -index argument with value
				Path path = Paths.get(indexArgPayload);
				try {
					index.toJSON(path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}