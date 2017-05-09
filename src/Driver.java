import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Execute this file to run the entire program.
 * 
 * @author Jeremy Kerby
 *
 */
public class Driver {

	public static final Logger log = LogManager.getLogger();

	/**
	 * This is the main method to call functionality of application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ArgumentMap arguments = new ArgumentMap(args);
		InvertedIndex index = null;
		WorkQueue queue = null;
		
		// TODO QueryFileParserInterface query = null;
		// TODO SynchronizedInvertedIndex threadSafe = null;
		
		// something is weird about this
		// if I set query to null I can not pass SearchTest.Exceptions.07
		QueryFileParserInterface query = new SerialQueryFileParser(index);
		int threadCount = 0;

		// handle thread argument
		if (arguments.hasFlag("-thread")) {
			SynchronizedInvertedIndex threadSafe = new SynchronizedInvertedIndex(); // TODO threadSafe = new SynchronizedInvertedIndex();
			index = threadSafe;
			threadCount = arguments.getInteger("-thread", 5);
			// TODO threadCount and check if it is >= 1
			
			queue = new WorkQueue(threadCount);
			
			// TODO query = ConcurrentQueryFileParser(threadSafe, queue);
		} else {
			index = new InvertedIndex();
			// TODO query = new SerialQueryFileParser(index);
		}

		// TODO Add some debugging to verify multithreading is happening as expected
		
		// handle path argument
		if (arguments.hasFlag("-path")) {
			if (queue != null) {
				Path path = Paths.get(arguments.getString("-path"));
				// TODO threadSafe instead of index
				ConcurrentInvertedIndexBuilder.buildIndex(path, index, queue);
				queue.finish();
			} else {
				if (arguments.getString("-path") != null) {
					Path path = Paths.get(arguments.getString("-path"));
					InvertedIndexBuilder.buildIndex(path, index);
				}
			}
		}

		// TODO Simplify
		// handle query argument
		if (arguments.hasFlag("-query") && arguments.hasFlag("-thread")) {
			String queryPath = arguments.getString("-query");
			if (queryPath != null) {
				Path path = Paths.get(queryPath);
				query = new ConcurrentQueryFileParser(index);
				query.parseQueryFile(path, arguments.hasFlag("-exact"), queue);
				queue.finish();
			}
		} else if (arguments.hasFlag("-query")) {
			String queryPath = arguments.getString("-query");
			if (queryPath != null) {
				Path path = Paths.get(queryPath);
				query = new SerialQueryFileParser(index);
				query.parseQueryFile(path, arguments.hasFlag("-exact"), null);
			}
		}

		// handle index argument
		if (arguments.hasFlag("-index")) {
			Path path = Paths.get(arguments.getString("-index", "index.json"));
			index.toJSON(path);
		}

		// handle results argument
		if (arguments.hasFlag("-results")) {
			if (query != null) {
				Path path = Paths.get(arguments.getString("-results", "results.json"));
				query.toJSON(path);
			}
		}

		if (queue != null) {
			queue.shutdown();
		}
	}

}