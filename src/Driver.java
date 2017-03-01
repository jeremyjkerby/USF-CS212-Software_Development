import java.io.IOException;

/**
 * Execute this file to run the entire program
 * @author Jeremy Kerby
 *
 */
public class Driver {

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		// System.out.println(">> main() >> start");

		ArgumentMap arguments = new ArgumentMap(args);
		InvertedIndex index = new InvertedIndex();
		WordIndexBuilder wib = new WordIndexBuilder();
		WordIndexWriter wiw = new WordIndexWriter();
		WordIndexBuilder.masterListOfFiles.clear();

		// System.out.println(arguments.toString());
		
		try {
			index = wib.processPathArgs(arguments); // load
			wiw.processIndexArgs(index, arguments); // save
		} catch (IOException e) {
			System.out.println("File not found");
		}

		// System.out.println(">> main() >> end");

	}

}