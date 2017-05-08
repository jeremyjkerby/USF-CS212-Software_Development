import java.nio.file.Path;

public interface QueryFileParserInterface {

	void toJSON(Path path);

	void parseQueryFile(Path path, boolean hasFlag, WorkQueue queue);

}
