import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

// TODO Fill in your Javadoc

/**
 * 
 * @author Jeremy Kerby
 *
 */
public class SearchResult implements Comparable<SearchResult> {

	// data members that I am currently not using
	private String where; // TODO final
	private int count;
	private int index;

	/**
	 * 
	 * @param where
	 * @param count
	 * @param index
	 */
	public SearchResult(String where, int count, int index) {
		this.where = where;
		this.count = count;
		this.index = index;
	}

	/**
	 * 
	 * @return
	 */
	public String getWhere() {
		return this.where;
	}

	/**
	 * 
	 * @return
	 */
	public int getCount() {
		return this.count;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getIndex() {
		return this.index;
	}	

	/**
	 * 
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	// TODO 
//	public void addCount(int count) {
//		this.count += count;
//	}
//	
//	public void updateIndex(int index) {
//		only update if less than
//	}

	/**
	 * 
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * 
	 * @param sr
	 */
	@Override
	public int compareTo(SearchResult sr) {
		// TODO 
//		if (this.count != sr.count) {
//			return Integer.compare(this.count, sr.count);
//		} else if (...) else...
		
		
		if (this.count == sr.count) {
			if (this.index == sr.index) {
				if (this.where.compareTo(sr.where) < 0) {
					return -1;
				} else {
					return 1;
				}
			} else {
				if (this.index < sr.index) {
					return -1;
				} else {
					return 1;
				}
			}
		} else {
			if (this.count > sr.count) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	//
	// below is what is currently being used
	//
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
