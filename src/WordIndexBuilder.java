
/*
 * Jeremy Kerby
 * Imported from Lab: Word Index
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

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
		/*
		 * TODO: Fill this in using a BufferedReader and try-with-resources
		 * block.
		 */
		System.out.println(">> buildIndex() >> start");
		System.out.println(">> buildIndex() >> Working file " + path.toString());
		System.out.println(">> buildIndex() >> Current index " + index.toString());

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

			System.out.println(">> buildIndex() >> data before" + masterString);

			// process masterString for entry
			masterString = htmlCleaner.stripHTML(masterString);

			System.out.println(">> buildIndex() >> data after" + masterString);

			// split the text into individual words by spaces
			String words[] = masterString.split(" ");
			for (int i = 0; i < words.length; i++) {
				// if word[i] is an actual word than add it and increase
				// masterIndex by one
				if (!words[i].equals(" ") && !words[i].equals("") && words[i].matches("\\w*")) {
					index.add(words[i], path.toString(), masterIndex);
					masterIndex++;
				}
			}
			System.out.println(">> buildIndex() >> Current index " + index.toString());
		}
		System.out.println(">> buildIndex() >> end");
	}
}
