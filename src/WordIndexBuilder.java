
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
		System.out.println("\n>> buildIndex()");

		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line = null;
			int masterIndex = 0;
			String data;

			while ((line = reader.readLine()) != null) {
				// index.add(line, 0);
				// System.out.println(line);
				String words[] = line.split("[\\s?-]+");
				for (int i = 0; i < words.length; i++) {
					// every single index string

					if (!words[i].equals("") && !words[i].matches("\\d+")) {
						data = words[i].toLowerCase();
						data = data.replaceAll("[.]", "");
						/*
						// data, is the word to save
						System.out.println(data);
						// path, is data's location
						System.out.println(path.toString());
						// masterIndex, was the index in file where found
						System.out.println(masterIndex);
						*/
						index.add(data, path.toString(), masterIndex);
					}
					if (!words[i].equals(""))
						masterIndex++;

				}
			}

		}
	}
}
