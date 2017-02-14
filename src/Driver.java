import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Driver {

	static ArgumentMap arguments;
	static WordIndex index;

	public static void main(String[] args) throws IOException {
		// TODO
		System.out.println(">> main() >> start");

		arguments = new ArgumentMap(args);

		processPathArgs(); // need to handle directories recursively

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
			Path path = Paths.get(pathArgPayload);
			File file = new File(path.toString());
			if (path.toString().matches("(?i).*html") || path.toString().matches("(?i).*htm")) {
				System.out.println(">> processPathArgs() >> You entered valid .html or .htm file.");
				System.out.println(">> processPathArgs() >> Getting file " + file.toPath().toString());
				index = WordIndexBuilder.buildIndex(file.toPath());
			} else if (file.isDirectory()) {
				System.out.println(">> processPathArgs() >> You entered a valid directory.");

				File directory = new File(pathArgPayload);
				File[] listOfFiles = directory.listFiles();

				determineFiles(directory);

				int count = 0;
				for (File master : masterListOfFiles) {

					if (count == 0) {
						System.out.println(">> processPathArgs() >> Getting file " + master.toPath().toString());
						index = WordIndexBuilder.buildIndex(master.toPath());
					} else {
						System.out.println(">> processPathArgs() >> Getting file " + master.toPath().toString());
						WordIndexBuilder.buildIndex(master.toPath(), index);
					}
					count++;
				}
			} else {
				System.out.println(">> processPathArgs() >> You did not enter a valid directory, .html or .htm file.");
			}
		}
		System.out.println(">> processPathArgs() >> end");
	}

	public static ArrayList<File> masterListOfFiles = new ArrayList<File>();

	// recursion to find all files
	private static void determineFiles(File d) {
		File[] listOfFiles = d.listFiles();
		for (File file : listOfFiles) {
			if (file.isDirectory()) {
				determineFiles(file);
			} else {
				if (file.toPath().toString().matches("(?i).*html") || file.toPath().toString().matches("(?i).*htm"))
					masterListOfFiles.add(file);
			}
		}

	}

	private static void openFile(Path p) throws IOException {
		System.out.println(">> processPathArgs() >> openFile() >> Getting file " + p.toString());
		WordIndexBuilder.buildIndex(p, index);
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