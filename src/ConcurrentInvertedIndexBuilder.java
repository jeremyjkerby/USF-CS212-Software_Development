import java.io.IOException;
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
	 * Runnable task that builds SynchronizedInvertedIndex from given path.
	 */
	private static class BuilderTask implements Runnable {

		private Path path;
		private final InvertedIndex index;

		public BuilderTask(Path path, InvertedIndex index) {
			this.path = path;
			this.index = index;
		}

		@Override
		public void run() {
			try {
				InvertedIndex local = new InvertedIndex();
				InvertedIndexBuilder.buildFromHTML(path, local);
				index.addAll(local);
			} catch (IOException e) {
				System.out.println("Task ended prematurely");
			}
		}

	}

}
