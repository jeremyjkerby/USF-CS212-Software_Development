
/*
 * Jeremy Kerby
 * Imported from Lab: Word Index
 */

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Nested data structure to store strings found and the files and positions they
 * were found.
 */
public class InvertedIndex {

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> wordIndex;

	/**
	 * Initializes the index. For every key (word) of the outer HashMap the
	 * value is an inner HashMap. For every key (file) of inner HashMap the
	 * value is a Set of Integer.
	 */
	public InvertedIndex() {
		wordIndex = new TreeMap<String, TreeMap<String, TreeSet<Integer>>>();
	}

	/**
	 * Adds the word, file and the position it was found to the index.
	 *
	 * @param word
	 *            word to clean and add to index
	 * @param file
	 *            name of file word was found
	 * @param position
	 *            position word was found
	 */
	public void add(String word, String file, int position) {
		TreeMap<String, TreeSet<Integer>> innerData;
		TreeSet<Integer> positions = null;

		// get inner TreeMap for given word
		innerData = wordIndex.get(word);
		// inner TreeMap does not exist so create it
		if (innerData == null) {
			innerData = new TreeMap<String, TreeSet<Integer>>();

			// add updated list into innerData
			innerData.put(file, positions);
			// add updated innerData into index
			wordIndex.put(word, innerData);
		}

		// get inner most TreeSet
		positions = innerData.get(file);
		// inner TreeSet does not exist so create it
		if (positions == null) {
			positions = new TreeSet<Integer>();

			// add updated list into innerData
			innerData.put(file, positions);
			// add updated innerData into index
			wordIndex.put(word, innerData);
		}

		// add current position to set
		positions.add(position);

		/*
		 * if (wordIndex.get(word) == null) { wordIndex.put(word, new
		 * TreeMap<String, TreeSet<Integer>>()); }
		 * 
		 * if (wordIndex.get(word).get(file) == null) {
		 * wordIndex.get(word).put(word, new TreeSet<Integer>()); }
		 * 
		 * wordIndex.get(word).get(file).add(position);
		 */
	}

	/**
	 * Adds the array of words at once, assuming the first word in the array is
	 * at position 1.
	 * 
	 * @param words
	 *            words to add
	 * @param filename
	 *            origin of words
	 */
	public void addAll(String[] words, String filename) {
		addAll(words, 1, filename);
	}

	/**
	 * Adds the array of words at once, assuming the first word in the array is
	 * at the provided starting position.
	 * 
	 * @param words
	 *            words words to add
	 * @param start
	 *            where to start
	 * @param filename
	 *            filename origin of words
	 */
	public void addAll(String[] words, int start, String filename) {
		for (int i = 0; i < words.length; i++) {
			add(words[i], filename, start);
			start++;
		}
	}

	/**
	 * Returns the number of times a word was found (i.e. the number of
	 * positions associated with a word in the index).
	 *
	 * @param word
	 *            word to look for
	 * @return number of times the word was found
	 */
	public int count(String word) {
		if (wordIndex.get(word) != null) {
			return wordIndex.get(word).size();
		} else {
			return 0;
		}
	}

	/**
	 * Returns the number of words stored in the index.
	 *
	 * @return number of words
	 */
	public int words() {
		return wordIndex.size();
	}

	/**
	 * Tests whether the index contains the specified word.
	 *
	 * @param word
	 *            word to look for
	 * @return true if the word is stored in the index
	 */
	public boolean contains(String word) {
		return wordIndex.containsKey(word);
	}

	/**
	 * Returns a copy of the words in this index as a sorted list.
	 *
	 * @return sorted list of words
	 *
	 * @see ArrayList#ArrayList(java.util.Collection)
	 * @see Collections#sort(List)
	 */
	public List<String> copyWords() {
		List<String> words = new ArrayList<String>(wordIndex.keySet());
		return words;
	}

	/**
	 * Returns a copy of the positions for a specific word.
	 *
	 * @param word
	 *            to find in index
	 * @param file
	 *            to find in index
	 * @return sorted list of positions for that word
	 *
	 * @see ArrayList#ArrayList(java.util.Collection)
	 * @see Collections#sort(List)
	 */
	public List<Integer> copyPositions(String word, String file) {
		List<Integer> words = null;
		if (wordIndex.get(word) != null) {
			words = new ArrayList<Integer>(wordIndex.get(word).get(file));
		}
		return words;
	}

	/**
	 * Returns a string representation of this index.
	 * 
	 * @return string representation
	 */
	@Override
	public String toString() {
		return wordIndex.toString();
	}

	/**
	 * Write data structure to given path.
	 * 
	 * @param path
	 *            where to save
	 */
	public void toJSON(Path path) {
		try {
			JSONWriter.asNestedObject(wordIndex, path);
		} catch (IOException e) {
			System.out.println("Unable to write JSON to file");
		}
	}

	/**
	 * Perform exact search on InvertedIndex with given words
	 * 
	 * @param words
	 * @return sorted list search result
	 */
	public SearchResult exactSearch(String[] words) {
		SearchResult sr = new SearchResult(); // TODO revamp this
		int count = 0;

		TreeMap<String, TreeSet<Integer>> curResult = new TreeMap<String, TreeSet<Integer>>();

		// for each cleaned word build queries string
		String queries = "";
		for (int i = 0; i < words.length; i++) {
			if (i != words.length - 1) {
				queries += words[i] + " ";
			} else {
				queries += words[i];
			}
		}
		sr.setQueries(queries);

		ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();
		
		//ArrayList<HashMap<String, SearchResult>> testing = new ArrayList<HashMap<String, SearchResult>>();
		//ArrayList<SearchResult> testing = new  ArrayList<SearchResult>();
		
		// for each cleaned word
		for (int i = 0; i < words.length; i++) { // TODO enhanced for loop
			// verify it is in inverted index
			if (wordIndex.containsKey(words[i]) == true) {
				// it is so get me entire object
				curResult = wordIndex.get(words[i]); // TODO locationMap
				// files found in
				Set<String> curResultKeys = curResult.keySet(); // TODO paths
				// for each file of word
				for (String key : curResultKeys) {
					HashMap<String, Object> inner = new HashMap<String, Object>();
					inner.put("where", key);
					count = curResult.get(key).size();
					inner.put("count", count);
					inner.put("index", curResult.get(key).first());

					int modify = 0;
					if (results.isEmpty()) {
						results.add(inner);
					} else {
						int size = results.size();
						boolean flag = false;
						
						// TODO Can avoid needing a linear search
						for (int r = 0; r < size; r++) {
							if (key.compareTo(results.get(r).get("where").toString()) == 0) {
								flag = true;
								modify = r;
							}
						}
						if (flag == false) {
							// record does not exists so add it
							results.add(inner);
						} else {
							// record already exists so update it
							int tempcount = (int) results.get(modify).get("count");
							tempcount += count;
							// update index
							int tempindex = (int) results.get(modify).get("index");
							if (curResult.get(key).first() < tempindex)
								tempindex = curResult.get(key).first();
							HashMap<String, Object> inner2 = new HashMap<String, Object>();
							inner2.put("where", key);
							inner2.put("count", tempcount);
							inner2.put("index", tempindex);
							results.set(modify, inner2);
						}
						flag = false;
					}
				}
			}
		}
		sr.setResults(results);
		return sr;
		
		/*
		List<SearchResult> resultList = new ArrayList<>();
		Map<String, SearchResult> resultMap = new Map<>();
		
		for every query
			if exists in map
				if this location is in our resultMap...
					update an existing search result
				else
					add a new search result to our map
		
		
		Collection.sort(resultList);
		return resultList;
		*/
	}

	/**
	 * Perform partial search on InvertedIndex with given words
	 * 
	 * @param words
	 * @return sorted list search result
	 */
	public SearchResult partialSearch(String[] words) {
		SearchResult sr = new SearchResult();
		int count = 0;

		TreeMap<String, TreeSet<Integer>> curResult = new TreeMap<String, TreeSet<Integer>>();

		// for each cleaned word build queries string
		String queries = "";
		for (int i = 0; i < words.length; i++) {
			if (i != words.length - 1) {
				queries += words[i] + " ";
			} else {
				queries += words[i];
			}
		}
		sr.setQueries(queries);

		// get all the keys/words we have
		Set<String> keys = wordIndex.keySet();

		ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();
		// for each cleaned word build queries string
		for (int i = 0; i < words.length; i++) {
			// for each key check if it starts with word
			for (String key : keys) {
				if (key.startsWith(words[i])) {
					// it is so get me entire object
					curResult = wordIndex.get(key);

					// files found in
					Set<String> curResultKeys = curResult.keySet();

					for (String cur : curResultKeys) {
						HashMap<String, Object> inner = new HashMap<String, Object>();

						inner.put("where", cur);
						count = curResult.get(cur).size();
						inner.put("count", count);
						inner.put("index", curResult.get(cur).first());

						int modify = 0; // holds who to modify
						if (results.isEmpty()) {
							results.add(inner);
						} else {
							int size = results.size();
							boolean flag = false;
							for (int r = 0; r < size; r++) {
								if (cur.compareTo(results.get(r).get("where").toString()) == 0) {
									flag = true;
									modify = r;
								}
							}
							if (flag == false) {
								results.add(inner);
							} else {
								// update count
								int tempcount = (int) results.get(modify).get("count");
								tempcount += count;
								// update index
								int tempindex = (int) results.get(modify).get("index");
								if (curResult.get(cur).first() < tempindex)
									tempindex = curResult.get(cur).first();
								HashMap<String, Object> inner2 = new HashMap<String, Object>();
								inner2.put("where", cur);
								inner2.put("count", tempcount);
								inner2.put("index", tempindex);
								results.set(modify, inner2);
							}
							flag = false;
						}
					}
				}
			}
		}
		sr.setResults(results);
		return sr;
	}

	public void saveResults(ArrayList<SearchResult> output, Path path) {
		try {
			JSONWriter.searchResults(output, path);
		} catch (IOException e) {
			System.out.println("Unable to write JSON to file");
		}
	}
}
