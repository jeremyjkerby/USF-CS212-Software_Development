import java.io.IOException;
import java.nio.file.Path;

/**
 * Convenience class to write a {@link WordIndex} to a text file.
 */
public class WordIndexWriter {

	public static void writeIndex(WordIndex index, Path path) throws IOException {

		System.out.println(">> writeIndex() >> start");

		System.out.println(">> writeIndex() >> My data is still " + index.toString());

		JSONWriter.asNestedObject(index.get(), path);

		System.out.println(">> writeIndex() >> end");

	}

}
