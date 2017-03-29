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
	private final String where;
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

	/**
	 * 
	 * @param count
	 */
	public void addCount(int count) {
		this.count += count;
	}
	
	/**
	 * 
	 * @param index
	 */
	public void updateIndex(int index) {
		// only update if less than
		if (index < this.index) {
			this.index = index;
		}
	}

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

}
