import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Convenience class to write a {@link InvertedIndex} to a text file.
 */
public class WordIndexWriter {

	/**
	 * Writes index data structure to file
	 * @param index
	 * @param path
	 * @throws IOException
	 */
	public static void writeIndex(InvertedIndex index, Path path) throws IOException {

		//System.out.println(">> writeIndex() >> start");

		if (index == null) {
			//System.out.println(">> writeIndex() >> My data was nulll");
			try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
				writer.write("");
			}
		} else {
			//System.out.println(">> writeIndex() >> My data is still " + index.toString());
			JSONWriter.asNestedObject(index.get(), path);
			//System.out.println(">> writeIndex() >> end");
		}

	}

	/**
	 * Processes given arguments and executes related action
	 * @param index
	 * @param arguments
	 * @throws IOException
	 */
	public void processIndexArgs(InvertedIndex index, ArgumentMap arguments) throws IOException {
		//System.out.println(">> processIndexArgs() >> start");

		if (arguments.hasFlag("-index") == false) {
			// no key or value pair found for -index do not save anything
			//System.out.println(">> processIndexArgs() >> You did not enter -index file. No output file produced.");
		} else {
			String indexArgPayload = arguments.getString("-index");
			if (indexArgPayload == null) {
				// we were given -index argument with no value
				Path path = Paths.get("index.json");
				try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
					writer.write("");
				}
			} else {
				// we were given -index argument with value
				saveFile(index, indexArgPayload);
			}
		}
		//System.out.println(">> processIndexArgs() >> end");
	}

	/**
	 * Helper method to assist in saving a data to file
	 * @param index
	 * @param p
	 * @throws IOException
	 */
	private static void saveFile(InvertedIndex index, String p) throws IOException {
		//System.out.println(">> processIndexArgs() >> saveFile() >> Saving file " + p);
		Path path = Paths.get(p);
		WordIndexWriter.writeIndex(index, path);
	}

}
