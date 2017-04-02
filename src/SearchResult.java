/**
 * SearchResult class stores the information of result. This contains where the
 * result was found,the count of occurrence as well as the first index found.
 */
public class SearchResult implements Comparable<SearchResult> {

	private final String where;
	private int count;
	private int index;

	/**
	 * Initialize this SearchResult. Requires where, count, index.
	 * 
	 * @param where
	 *            filename where found
	 * @param count
	 *            number of occurrences
	 * @param index
	 *            first index of occurrences
	 */
	public SearchResult(String where, int count, int index) {
		this.where = where;
		this.count = count;
		this.index = index;
	}

	/**
	 * Returns the file name of this SearchResult.
	 * 
	 * @return String filename
	 */
	public String getWhere() {
		return this.where;
	}

	/**
	 * Returns the count of this SearchResult.
	 * 
	 * @return number count
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * Returns the index of this SearchResult.
	 * 
	 * @return number index
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * Sets the count of current SearchResult with given count.
	 * 
	 * @param count
	 *            value to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Adds the given count to current count. Used when similar SearchResult is
	 * found.
	 * 
	 * @param count
	 *            value to add
	 */
	public void addCount(int count) {
		this.count += count;
	}

	/**
	 * Updates the index of current SearchResult with given index.
	 * 
	 * @param index
	 *            value to set
	 */
	public void updateIndex(int index) {
		// only update if less than
		if (index < this.index) {
			this.index = index;
		}
	}

	/**
	 * Sets the index of current SearchResult with given index.
	 * 
	 * @param index
	 *            the value to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Compares given SearchResult to current SearchResult. Compares count,
	 * index and where.
	 * 
	 * @param sr
	 *            SearchResult to compare to
	 * @return number negative is less than positive if greater
	 */
	@Override
	public int compareTo(SearchResult sr) {
		if (this.count != sr.count) {
			return Integer.compare(sr.count, this.count);
		} else if (this.index != sr.index) {
			return Integer.compare(this.index, sr.index);
		} else {
			return this.where.compareTo(sr.where);
		}
	}

}
