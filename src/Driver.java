/*
 * Jeremy Kerby
 * Project 1
 */
import java.io.IOException;

public class Driver {

	public static void main(String[] args) throws IOException {
		// System.out.println(">> main() >> start");

		ArgumentMap arguments;
		WordIndex index;
		WordIndexBuilder wib;
		WordIndexWriter wiw;
		WordIndexBuilder.masterListOfFiles.clear();

		arguments = new ArgumentMap(args);
		wib = new WordIndexBuilder();
		wiw = new WordIndexWriter();
		index = new WordIndex();

		// System.out.println(arguments.toString());

		index = wib.processPathArgs(arguments); // load

		wiw.processIndexArgs(index, arguments); // save 

		// System.out.println(">> main() >> end");

	}

}