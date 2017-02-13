import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.TreeSet;

public class Driver {

	static ArgumentMap arguments;
	static WordIndex index;

	public static void main(String[] args) throws IOException {
		// TODO
		System.out.println(">> main() >> start");

		arguments = new ArgumentMap(args);

		processPathArgs();

		processIndexArgs();

		System.out.println(">> main() >> end");

	}

	private static void processPathArgs() throws IOException {
		System.out.println(">> processPathArgs() >> start");

		System.out.println(">> processPathArgs() >> current arguments >> " + arguments.toString());

		String pathArgPayload = arguments.getString("-path");

		if (pathArgPayload == null) {
			// no key or value pair found for -path
			System.out.println(">> processPathArgs() >> You did not enter -path file. Try again.");
		} else {

			if (pathArgPayload.endsWith(".html") || pathArgPayload.endsWith(".htm")) {
				System.out.println(">> processPathArgs() >> You entered valid .html or .htm file.");
				// for this file call openFile(path, index)
				File file = new File(pathArgPayload);
				openFile(file.toPath());
			} else if (pathArgPayload.endsWith("/")) {
				System.out.println(">> processPathArgs() >> You entered a valid directory.");
				// for every file call openFile(path, index)
				File directory = new File(pathArgPayload);
				File[] listOfFiles = directory.listFiles();
				for (File file : listOfFiles) {
					openFile(file.toPath());
				}
			} else {
				System.out.println(">> processPathArgs() >> You did not enter a valid directory, .html or .htm file.");
			}
		}
		System.out.println(">> processPathArgs() >> end");
	}

	private static void openFile(Path p) throws IOException {
		System.out.println(">> processPathArgs() >> openFile() >> Getting file " + p.toString());
		index = WordIndexBuilder.buildIndex(p);
	}

	private static void processIndexArgs() throws IOException {
		System.out.println(">> processIndexArgs() >> start");

		if (arguments.hasFlag("-index") == false) {
			// no key or value pair found for -index do not save anything
			System.out.println(">> processIndexArgs() >> You did not enter -index file. No output file produced.");
		} else {
			String indexArgPayload = arguments.getString("-index");
			if (indexArgPayload == null) {
				// we were given -index argument with no value
				saveFile("index.json");
			} else {
				// we were given -index argument with value
				saveFile(indexArgPayload);
			}
		}
		System.out.println(">> processIndexArgs() >> end");
	}

	private static void saveFile(String p) throws IOException {
		System.out.println(">> processIndexArgs() >> saveFile() >> Saving file " + p);
		Path path = Paths.get(p);
		WordIndexWriter.writeIndex(index, path);
	}
}