import java.io.IOException;

// TODO Resolve your old TODO comments
// TODO Always resolve any warnings in your project

/**
 * 
 * @author Jeremy Kerby
 *
 */
public class Driver {

	// TODO Should never throw an exception... can throw EVERYWHERE else but
	// here
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// System.out.println(">> main() >> start");

		ArgumentMap arguments = new ArgumentMap(args);
		InvertedIndex index = new InvertedIndex();
		WordIndexBuilder wib = new WordIndexBuilder();
		WordIndexWriter wiw = new WordIndexWriter();
		WordIndexBuilder.masterListOfFiles.clear();

		// System.out.println(arguments.toString());

		index = wib.processPathArgs(arguments); // load

		wiw.processIndexArgs(index, arguments); // save

		// System.out.println(">> main() >> end");

	}

}