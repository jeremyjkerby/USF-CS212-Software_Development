import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConcurrentInvertedIndexBuilder {

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
	public static InvertedIndex buildIndex(Path path, WorkQueue queue) {
		SynchronizedInvertedIndex index = new SynchronizedInvertedIndex();
		buildIndex(path, index, queue);
		return index;
	}

	/**
	 * Determines if path is directory or file. If it is a file sends of to be
	 * processed.
	 * 
	 * @param path
	 *            path to file to parse
	 * @param index
	 *            inverted index to add words
	 */
	public static void buildIndex(Path path, SynchronizedInvertedIndex index, WorkQueue queue) {
		if (Files.isDirectory(path)) { // is directory
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
				for (Path p : stream) {
					buildIndex(p, index, queue); // recursive call, execute
				}
			} catch (IOException e) {
				System.out.println("Unable to read valid directory.");
			}
		} else { // is file
			if (path.toString().matches("(?i).*html?")) {
				queue.execute(new BuilderTask(path, index));
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
	 *            inverted word index to add words
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
			String masterString = null;
			String line;

			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line + " ");
			}

			masterString = sb.toString();

			// process masterString for entry
			masterString = HTMLCleaner.stripHTML(masterString);

			// split the text into individual words by spaces
			String words[] = WordParser.parseWords(masterString);

			index.addAll(words, path.toString());
		}
	}

	/**
	 * Runnable task that builds SynchronizedInvertedIndex from given path.
	 */
	private static class BuilderTask implements Runnable {

		private Path path;
		private final SynchronizedInvertedIndex index;

		public BuilderTask(Path path, SynchronizedInvertedIndex index) {
			this.path = path;
			this.index = index;
		}

		@Override
		public void run() {
			try {
				buildFromHTML(path, index);
			} catch (IOException e) {
				System.out.println("Task ended prematurely");
			}
		}

	}

}
