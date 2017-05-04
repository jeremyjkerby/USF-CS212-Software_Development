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
		/*
		TODO

		ArgumentMap arguments = new ArgumentMap(args);
		InvertedIndex index = null;
		WorkQueue queue = null;
		QueryFileParserInterface query = null;


		if (-threads) {
			SynchronizedInvertedIndex threadSafe = new SynchronizedInvertedIndex();
			index = threadSafe;
			
			
		}
		else {
			index = new InvertedIndex();
		}

		if (-path) {
			if (queue != null) {
				multithreaded version
			}
			else {
				single-threaded version
			}
		}


		if (-query) {
			query.parseQueries(...); (make sure your multithreaded version calls finish in this method)
		}

		if (-index) {
			index.toJSON();
		}
		
		if (-results) {
			query.toJSON();
		}

		 */
		
		
		
		
		ArgumentMap arguments = new ArgumentMap(args);
		InvertedIndex index = new InvertedIndex();
		SynchronizedInvertedIndex synchronizedIndex = new SynchronizedInvertedIndex();
		WorkQueue queue = null;
		int threadCount = 0;

		if (arguments.hasFlag("-thread")) {
			threadCount = arguments.getInteger("-thread", 5);
			log.debug("Threads entered to create " + threadCount);
			queue = new WorkQueue(threadCount);
			if (arguments.getString("-path") != null) {
				log.debug("Running synchronized code");
				// handle path argument
				Path path = Paths.get(arguments.getString("-path"));

				ConcurrentInvertedIndexBuilder.buildIndex(path, synchronizedIndex, queue);
				queue.finish();

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
		QueryFileParser query = new QueryFileParser(index, synchronizedIndex);
		if (arguments.hasFlag("-thread") && arguments.hasFlag("-query")) {
			String queryPath = arguments.getString("-query");
			if (queryPath != null) {
				// we were given -query argument with value
				Path path = Paths.get(queryPath);
				// determine search type
				log.debug("Running threaded query code");

				query.synchronizedParseQueryFile(path, arguments.hasFlag("-exact"), queue);
				queue.finish();

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

		if (queue != null) {
			queue.shutdown();
		}
	}

}