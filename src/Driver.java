import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver {

	static ArgumentMap arguments;
	static WordIndex index;

	public static void main(String[] args) {
		// TODO
		System.out.println(">> main() >> start\n");

		arguments = new ArgumentMap(args);
		index = new WordIndex();

		System.out.println(">> main() >> My stored args: " + arguments.toString());
		System.out.println(">> main() >> My stored index: " + index.toString());

		fileOpener();

		saveWordIndexToFile();
		
		System.out.println("\n>> main() >> end");
	}

	private static void saveWordIndexToFile() {
		System.out.println("\n>> saveWordIndexToFile()");
		
		if (arguments.hasFlag("-index") == false) { // no path given
			// {-path=html/hello.html}
			System.out.println(">> fileOpener() >> Do not save index file. " + arguments.toString());
			
		} else {
			if (arguments.getString("-index") == null) { // no path given
				// {-index=null, -path=html/hello.html}
				// index is there but no value
				System.out.println(">> fileOpener() >> Save index file. " + arguments.toString());
				File indexFile = new File("index.json");
				try {
					indexFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println(">> fileOpener() >> Save index file. " + arguments.toString());
				File indexFile = new File(arguments.getString("-index"));
				try {
					indexFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			// index is there with value
			
		}
	}

	private static void fileOpener() {
		System.out.println("\n>> fileOpener()");

		System.out.println(">> Getting file(s) " + arguments.getString("-path"));

		if (arguments.getString("-path") == null) { // no path given
			System.out.println(">> fileOpener() >> You did not enter a file/directory");

		} else { // path given
			System.out.println(">> fileOpener() >> You did enter a file/file path");

			String payload = arguments.getString("-path");
			Path path = Paths.get(payload);

			// check if was file or directory
			if (payload.endsWith(".html") || payload.endsWith(".htm")) { // file
				System.out.println(">> fileOpener() >> You gave a single HTML/HTM file");

				WordIndexBuilder wib = new WordIndexBuilder();
				try {
					System.out.println(">> fileOpener() >> Building word index");

					wib.buildIndex(path, index);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(">> fileOpener() >> Populated data structure:\n" + index.toString());
			} else if (payload.endsWith("/") || payload.endsWith("\\")) { // directory
				System.out.println(">> fileOpener() >> You gave a directory");
				
				File directory = new File(path.toString());
				File[] listOfFiles = directory.listFiles();
				
				for (int i = 0; i < listOfFiles.length; i++) {
					String file = path.toString() + "/" + listOfFiles[i].getName();
					//System.out.println("File " + file);
					WordIndexBuilder wib = new WordIndexBuilder();
					Path path2 = Paths.get(file);
					try {
						System.out.println(">> fileOpener() >> Building word index");
						
						wib.buildIndex(path2, index);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(">> fileOpener() >> Populated data structure:\n" + index.toString());
				}
				
			}

			// given directory
			// for each file in directory
			// Use WordIndex buildIndex
			// all else we do not know how to handle what you gave me

		}
	}
}