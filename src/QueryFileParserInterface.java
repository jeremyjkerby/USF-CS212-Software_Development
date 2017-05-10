import java.nio.file.Path;

public interface QueryFileParserInterface {

	/**
	 * Writes map of results to given path.
	 * 
	 * @param path
	 *            where the result map will be written to
	 */
	void toJSON(Path path);

	/**
	 * Read from file line by line. Clean and sort words. Determine then perform
	 * query.
	 * 
	 * @param path
	 *            location of where query file is
	 * @param exact
	 *            true if exact false if partial
	 */
	void parseQueryFile(Path path, boolean hasFlag);

}
