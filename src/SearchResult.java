import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class SearchResult {

	/**
	 * Stores a mapping of results with some other information found.
	 */
	HashMap<String, Object> output;

	public SearchResult() {
		output = new HashMap<String, Object>();
	}

	public void setQueries(String data) {
		output.put("queries", data);
	}

	public void setResults(ArrayList<HashMap<String, Object>> data) {
		data = listByWhere((Collection<HashMap<String, Object>>) data);
		output.put("results", data);
	}

	public static final ArrayList<HashMap<String, Object>> listByWhere(Collection<HashMap<String, Object>> books) {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>(books);
		Collections.sort(list, new Comparator<HashMap<String, Object>>() {
			@Override
			public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
				int one = (int) o1.get("count");
				int two = (int) o2.get("count");
				if (one == two) {

					int one2 = (int) o1.get("index");
					int two2 = (int) o2.get("index");
					if (one2 == two2) {

						String one3 = o1.get("where").toString();
						String two3 = o2.get("where").toString();
						if (one3.compareTo(two3) < 0) {
							return -1;
						} else {
							return 1;
						}

					} else {
						if (one2 < two2) {
							return -1;
						} else {
							return 1;
						}
					}

				} else {
					if (one > two) {
						return -1;
					} else {
						return 1;
					}
				}

			}

		});
		return list;
	}
}
