
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

// TODO You need to restore your old Project 2 classes, because this is no longer appropriate to use for single threading

/**
 * Nested data structure to store strings found and the files and positions they
 * were found.
 */
public class InvertedIndex {

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> wordIndex;
	private ReadWriteLock lock;

	/**
	 * Initializes the index. For every key (word) of the outer HashMap the
	 * value is an inner HashMap. For every key (file) of inner HashMap the
	 * value is a Set of Integer.
	 */
	public InvertedIndex() {
		wordIndex = new TreeMap<String, TreeMap<String, TreeSet<Integer>>>();
		lock = new ReadWriteLock();
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
	public void add(String word, String file, int position) { // TODO Change this to a private void addHelper(...)
		// inner TreeMap does not exist so create it
		lock.lockReadWrite();
		try {

			if (wordIndex.get(word) == null) {
				wordIndex.put(word, new TreeMap<String, TreeSet<Integer>>());
			}
			// inner TreeSet does not exist so create it
			if (wordIndex.get(word).get(file) == null) {
				wordIndex.get(word).put(file, new TreeSet<Integer>());
			}

			// add current position to set
			wordIndex.get(word).get(file).add(position);

		} finally {
			lock.unlockReadWrite();
		}
	}
	
	// TODO Create a new add() that calls addHelper, and your addAll() method should also call addHelper

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
		// TODO This is locking inside a loop, which is very slow
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
		lock.lockReadOnly();
		try {
			if (wordIndex.get(word) != null) {
				return wordIndex.get(word).size();
			} else {
				return 0;
			}
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Returns the number of words stored in the index.
	 *
	 * @return number of words
	 */
	public int words() {
		lock.lockReadOnly();
		try {
			return wordIndex.size();
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Tests whether the index contains the specified word.
	 *
	 * @param word
	 *            word to look for
	 * @return true if the word is stored in the index
	 */
	public boolean contains(String word) {
		lock.lockReadOnly();
		try {
			return wordIndex.containsKey(word);
		} finally {
			lock.unlockReadOnly();
		}
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
		lock.lockReadOnly();
		try {
			List<String> words = new ArrayList<String>(wordIndex.keySet());
			return words;
		} finally {
			lock.unlockReadOnly();
		}
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
		lock.lockReadOnly();
		try {
			List<Integer> words = null;
			if (wordIndex.get(word) != null) {
				words = new ArrayList<Integer>(wordIndex.get(word).get(file));
			}
			return words;
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Returns a string representation of this index.
	 * 
	 * @return string representation
	 */
	@Override
	public String toString() {
		lock.lockReadOnly();
		try {
			return wordIndex.toString();
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Write data structure to given path.
	 * 
	 * @param path
	 *            where to save
	 */
	public void toJSON(Path path) {
		lock.lockReadOnly();
		try {
			try {
				JSONWriter.asNestedObject(wordIndex, path);
			} catch (IOException e) {
				System.out.println("Unable to write JSON to file");
			}
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Perform exact search on InvertedIndex with given words
	 * 
	 * @param words
	 *            words use to look for
	 * @return sorted list search result
	 */
	public List<SearchResult> exactSearch(String[] words) {
		lock.lockReadOnly();
		try {
			List<SearchResult> resultList = new ArrayList<SearchResult>();
			HashMap<String, SearchResult> resultMap = new HashMap<String, SearchResult>();

			// for every query
			for (String word : words) {
				// verify it is in inverted index
				if (wordIndex.containsKey(word) == true) {
					// get word index object
					TreeMap<String, TreeSet<Integer>> match = wordIndex.get(word);
					determineAddUpdate(match, resultList, resultMap);
				}
			}
			Collections.sort(resultList);
			return resultList;
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Perform partial search on InvertedIndex with given words
	 * 
	 * @param words
	 *            words use to look for
	 * @return sorted list search result
	 */
	public List<SearchResult> partialSearch(String[] words) {
		lock.lockReadOnly();
		try {
			List<SearchResult> resultList = new ArrayList<SearchResult>();
			HashMap<String, SearchResult> resultMap = new HashMap<String, SearchResult>();

			// for every query
			for (String word : words) {

				// get words greater than or equal to word
				Set<String> keys = wordIndex.tailMap(word).keySet();

				for (String key : keys) {
					// verify it is in inverted index
					if (key.startsWith(word)) {
						// get word index object
						TreeMap<String, TreeSet<Integer>> match = wordIndex.get(key);
						determineAddUpdate(match, resultList, resultMap);
					} else {
						break;
					}
				}
			}
			Collections.sort(resultList);
			return resultList;
		} finally {
			lock.unlockReadOnly();
		}
	}

	/**
	 * Determine if given match needs to be added or updated. This is a helper
	 * method used in both exact and partial search.
	 * 
	 * @param match
	 *            object to either add or updated into our results
	 * @param resultMap
	 * @param resultList
	 */
	private void determineAddUpdate(TreeMap<String, TreeSet<Integer>> match, List<SearchResult> resultList,
			HashMap<String, SearchResult> resultMap) {
		// get locations for this object

		Set<String> locations = match.keySet();

		// verify if location is in result
		for (String location : locations) {
			String where = location;
			int count = match.get(location).size();
			int index = match.get(location).first();

			if (resultMap.containsKey(location)) {
				// if this location is in our resultMap
				// update an existing search result
				SearchResult sr = resultMap.get(location);
				sr.addCount(count);
				sr.updateIndex(index);
			} else {
				// add a new search result to our map
				SearchResult sr = new SearchResult(where, count, index);
				resultMap.put(where, sr);
				resultList.add(sr);
			}
		}

	}

}
