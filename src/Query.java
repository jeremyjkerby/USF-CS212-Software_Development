import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

public class Query {

	public ArrayList<String> buildFromFile(Path path) throws IOException {

		ArrayList<String> queryStrings = new ArrayList<String>();

		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

			String line;
			while ((line = reader.readLine()) != null) {
				// for each line

				// clean and separate into words
				String temp = HTMLCleaner.stripHTML(line);
				String cleanedTemp[] = WordParser.parseWords(temp);
				ArrayList<String> tempQueryWords = new ArrayList<String>();
				for (int i = 0; i < cleanedTemp.length; i++) {
					tempQueryWords.add(cleanedTemp[i]);
				}
				Collections.sort(tempQueryWords);
				String queryString = "";
				for (int i = 0; i < tempQueryWords.size(); i++) {
					// queryWords.add(tempQueryWords.get(i));
					if (i != tempQueryWords.size() - 1) {
						queryString += tempQueryWords.get(i) + " ";
					} else {
						queryString += tempQueryWords.get(i);
					}
				}
				
				if (!queryStrings.contains(queryString) && (queryString.isEmpty() == false)) {
					queryStrings.add(queryString);
				}
			}

		}
		// pass those clean words to index
		Collections.sort(queryStrings);
		return queryStrings;
	}

}
