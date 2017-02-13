import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Convenience class to write a {@link WordIndex} to a text file.
 */
public class WordIndexWriter {

	public static void writeIndex(WordIndex index, Path path) throws IOException {

		TreeMap<String, TreeMap<String, TreeSet<Integer>>> data;
		data = index.get(); // now we has the word index as object not as string

		System.out.println(">> writeIndex() >> start");

		System.out.println(">> writeIndex() >> My data is still " + index.toString());
		
		System.out.println(">> writeIndex() >> end");

	}

}
