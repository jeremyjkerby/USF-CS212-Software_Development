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
		SynchronizedInvertedIndex threadSafe = null;
		WorkQueue queue = null;
		QueryFileParserInterface query = null;
		int threadCount = 0;

		// handle thread argument
		if (arguments.hasFlag("-thread")) {

			log.debug("Running main synchronized code");

			threadSafe = new SynchronizedInvertedIndex();
			index = threadSafe;
			threadCount = arguments.getInteger("-thread", 5);
			// TODO threadCount and check if it is >= 1

			queue = new WorkQueue(threadCount);
			query = new ConcurrentQueryFileParser(threadSafe, queue);
		} else {
			log.debug("Running main serial code");

			index = new InvertedIndex();
			query = new SerialQueryFileParser(index);
		}

		// handle path argument
		if (arguments.hasFlag("-path")) {
			if (queue != null) {
				Path path = Paths.get(arguments.getString("-path"));
				ConcurrentInvertedIndexBuilder.buildIndex(path, threadSafe, queue);
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

				log.debug("Running threaded query code");

				query.parseQueryFile(path, arguments.hasFlag("-exact"));

				queue.finish();
			}
		} else if (arguments.hasFlag("-query")) {
			String queryPath = arguments.getString("-query");
			if (queryPath != null) {
				Path path = Paths.get(queryPath);

				// determine search type
				log.debug("Running serial query code");

				query.parseQueryFile(path, arguments.hasFlag("-exact"));
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