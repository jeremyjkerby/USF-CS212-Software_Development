
/*
 * Jeremy Kerby
 * Imported from Lab: Word Index
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Convenience class to build a InvertedIndex from a file
 */
public class InvertedIndexBuilder {

	/**
	 * Creates and returns a new word index built from the file located at the
	 * path provided.
	 *
	 * @param path
	 *            path to file to parse
	 * @return word index containing words from the path
	 * @throws IOException
	 *
	 * @see {@link #buildIndex(Path, InvertedIndex)}
	 */
	public InvertedIndex buildIndex(Path path) throws IOException {
		InvertedIndex index = new InvertedIndex();
		buildIndex(path, index);
		return index;
	}

	/**
	 * TODO
	 * 
	 * @param path
	 * @param index
	 */
	public static void buildIndex(Path path, InvertedIndex index) {
		if (Files.isDirectory(path)) { // is directory
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
				for (Path p : stream) {
					buildIndex(p, index); // recursive call
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else { // is file
			if (path.toString().matches("(?i).*html") || path.toString().matches("(?i).*htm")) {
				try {
					buildFromHTML(path, index);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
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
	public static void buildFromHTML(Path path, InvertedIndex index) throws IOException {
		// using the UTF-8 character encoding for all file processing
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			int masterIndex = 1; // the positions should start at 1
			String masterString = "";
			int curInt;

			StringBuilder sb = new StringBuilder();

			// TODO Read line-by-line or read ALL characters at once
			while ((curInt = reader.read()) != -1) {
				sb.append((char) curInt);
			}

			masterString = sb.toString();

			// process masterString for entry
			masterString = HTMLCleaner.stripHTML(masterString);

			// split the text into individual words by spaces
			String words[] = WordParser.parseWords(masterString);

			// TODO index.addAll(words, path.toString());
			for (int i = 0; i < words.length; i++) {
				index.add(words[i], path.toString(), masterIndex);
				masterIndex++;
			}
		}
	}

}
