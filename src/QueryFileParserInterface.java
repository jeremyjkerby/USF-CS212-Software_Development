import java.nio.file.Path;

// TODO Javadoc, don't have to Javadoc elsewhere (but still use the @Override annotation)

public interface QueryFileParserInterface {

	void toJSON(Path path);

	void parseQueryFile(Path path, boolean hasFlag, WorkQueue queue); // TODO Remove WorkQueue

}
