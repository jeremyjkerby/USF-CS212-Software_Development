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
		InvertedIndex index = new InvertedIndex();
		WorkQueue queue = null;
		int threadCount = 0;

		if (arguments.hasFlag("-thread")) {
			// TODO arguments.getInteger
			threadCount = Integer.parseInt(arguments.getString("-thread"));
			log.debug("Threads entered to create " + threadCount);
			// do something
			if (arguments.getString("-path") != null) {
				log.debug("Running synchronized code");
				// handle path argument
				Path path = Paths.get(arguments.getString("-path"));
				queue = new WorkQueue(threadCount); // TODO Move this outside the -path block
				SynchronizedInvertedIndexBuilder.buildIndex(path, index, queue);
				queue.finish();
				queue.shutdown(); // TODO Remove this from here and use the same queue in both places
			}
		} else if (arguments.getString("-path") != null) {
			log.debug("Running serial code");
			// handle path argument
			Path path = Paths.get(arguments.getString("-path"));
			InvertedIndexBuilder.buildIndex(path, index);
		}

		// handle index argument
		if (arguments.hasFlag("-index")) {
			log.debug("Writing data structure to file");
			// we were given -index argument with value
			Path path = Paths.get(arguments.getString("-index", "index.json"));
			index.toJSON(path);
		}

		// handle query argument
		QueryFileParser query = new QueryFileParser(index);
		if (arguments.hasFlag("-thread") && arguments.hasFlag("-query")) {
			String queryPath = arguments.getString("-query");
			if (queryPath != null) {
				// we were given -query argument with value
				Path path = Paths.get(queryPath);
				// determine search type
				queue = new WorkQueue(threadCount);
				log.debug("Running threaded query code");

				query.synchronizedParseQueryFile(path, arguments.hasFlag("-exact"), queue);
				queue.finish();
				queue.shutdown(); // there should be no more threading // TODO DO this at the end of Driver if your queue is not null
			}
		} else if (arguments.hasFlag("-query")) {
			String queryPath = arguments.getString("-query");
			if (queryPath != null) {
				// we were given -query argument with value
				Path path = Paths.get(queryPath);
				// determine search type
				log.debug("Running serial query code");

				query.parseQueryFile(path, arguments.hasFlag("-exact"));
			}
		}

		// handle results argument
		if (arguments.hasFlag("-results")) {
			// we were given -results argument with no value
			Path path = Paths.get(arguments.getString("-results", "results.json"));
			query.toJSON(path);
		}
	}

}