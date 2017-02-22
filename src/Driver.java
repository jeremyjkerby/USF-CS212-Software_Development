/*
 * Jeremy Kerby
 * Project 1
 */
import java.io.IOException;

// TODO Resolve your old TODO comments
// TODO Always resolve any warnings in your project
// TODO All classes and methods to have Javadoc comments (including Driver)

public class Driver {

	// TODO Should never throw an exception... can throw EVERYWHERE else but here
	public static void main(String[] args) throws IOException {
		// System.out.println(">> main() >> start");

		// TODO No need to split the declaration and definition for local variables
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