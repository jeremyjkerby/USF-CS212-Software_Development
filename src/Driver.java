import java.io.IOException;

public class Driver {

	public static void main(String[] args) throws IOException {
		// TODO
		System.out.println(">> main() >> start");
		
		ArgumentMap arguments;
		WordIndex index;
		WordIndexBuilder wib;
		WordIndexWriter wiw;
		WordIndexBuilder.masterListOfFiles.clear();

		arguments = new ArgumentMap(args);
		wib = new WordIndexBuilder();
		wiw = new WordIndexWriter();

		System.out.println(arguments.toString());
		index = new WordIndex();
		index = wib.processPathArgs(arguments);

		wiw.processIndexArgs(index, arguments);

		System.out.println(">> main() >> end");

	}

}