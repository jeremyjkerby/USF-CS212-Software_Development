
/*
 * Jeremy Kerby
 * Imported from Lab: Word Index
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Convenience class to build a {@link WordIndex} from a text file.
 */
public class WordIndexBuilder {

	/**
	 * Creates and returns a new word index built from the file located at the
	 * path provided.
	 *
	 * @param path
	 *            path to file to parse
	 * @return word index containing words from the path
	 * @throws IOException
	 *
	 * @see {@link #buildIndex(Path, WordIndex)}
	 */
	public static WordIndex buildIndex(Path path) throws IOException {
		WordIndex index = new WordIndex();
		buildIndex(path, index);
		return index;
	}

	/**
	 * Opens the file located at the path provided, parses each line in the file
	 * into words, and stores those words in a word index.
	 *
	 * @param path
	 *            path to file to parse
	 * @param index
	 *            word index to add words
	 * @throws IOException
	 *
	 * @see WordParser#parseWords(String, int)
	 *
	 * @see Files#newBufferedReader(Path, java.nio.charset.Charset)
	 * @see StandardCharsets#UTF_8
	 */
	public static void buildIndex(Path path, WordIndex index) throws IOException {
		// System.out.println(">> buildIndex() >> start");
		// System.out.println(">> buildIndex() >> Working file " +
		// path.toString());
		// System.out.println(">> buildIndex() >> Current index " +
		// index.toString());

		HTMLCleaner htmlCleaner = new HTMLCleaner();

		// using the UTF-8 character encoding for all file processing
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line = "";
			int masterIndex = 1; // the positions should start at 1
			String masterString = "";
			int curInt;
			char curChar;

			while ((curInt = reader.read()) != -1) {
				curChar = (char) curInt;
				masterString += curChar;
			}

			// System.out.println(">> buildIndex() >> data before" +
			// masterString);

			// process masterString for entry
			masterString = htmlCleaner.stripHTML(masterString);

			// System.out.println(">> buildIndex() >> data after" +
			// masterString);

			// split the text into individual words by spaces
			String words[] = WordParser.parseWords(masterString);

			for (int i = 0; i < words.length; i++) {
				index.add(words[i], path.toString(), masterIndex);
				masterIndex++;
			}

			// System.out.println(">> buildIndex() >> Current index " +
			// index.toString());
		}
		// System.out.println(">> buildIndex() >> end");
	}

	public WordIndex processPathArgs(ArgumentMap arguments) throws IOException {
		// System.out.println(">> processPathArgs() >> start");

		// System.out.println(">> processPathArgs() >> current arguments >> " +
		// arguments.toString());

		WordIndex index = null;
		String pathArgPayload = arguments.getString("-path");

		if (pathArgPayload == null) {
			// no key or value pair found for -path
			// System.out.println(">> processPathArgs() >> You did not enter
			// -path file. Try again.");
		} else {
			Path path = Paths.get(pathArgPayload);
			File file = new File(path.toString());
			if (path.toString().matches("(?i).*html") || path.toString().matches("(?i).*htm")) {
				// System.out.println(">> processPathArgs() >> You entered valid
				// .html or .htm file.");
				// System.out.println(">> processPathArgs() >> Getting file " +
				// file.toPath().toString());
				index = WordIndexBuilder.buildIndex(file.toPath());
			} else if (file.isDirectory()) {
				// System.out.println(">> processPathArgs() >> You entered a
				// valid directory.");

				File directory = new File(pathArgPayload);

				determineFiles(directory);

				int count = 0;
				for (File master : masterListOfFiles) {

					if (count == 0) {
						// System.out.println(">> processPathArgs() >> Getting
						// file " + master.toPath().toString());
						index = WordIndexBuilder.buildIndex(master.toPath());
					} else {
						//System.out.println(">> processPathArgs() >> Getting file " + master.toPath().toString());
						WordIndexBuilder.buildIndex(master.toPath(), index);
					}
					count++;
				}
			} else {
				// System.out.println(">> processPathArgs() >> You did not enter
				// a valid directory, .html or .htm file.");
			}
		}
		// System.out.println(">> processPathArgs() >> end");
		return index;
	}

	public static ArrayList<File> masterListOfFiles = new ArrayList<File>();

	// recursion to find all files
	private static void determineFiles(File d) {
		File[] listOfFiles = d.listFiles();
		for (File file : listOfFiles) {
			if (file.isDirectory()) {
				determineFiles(file);
			} else {
				// only consider .html or .htm files
				if (file.toPath().toString().matches("(?i).*html") || file.toPath().toString().matches("(?i).*htm"))
					masterListOfFiles.add(file);
			}
		}

	}

}
