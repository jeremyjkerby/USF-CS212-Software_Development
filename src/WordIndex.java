
/*
 * Jeremy Kerby
 * Imported from Lab: Word Index
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Nested data structure to store strings found and the files and positions they
 * were found.
 */
public class WordIndex {

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private Map<String, HashMap<String, Set<Integer>>> wordIndex;

	/**
	 * Initializes the index. For every key (word) of the outer HashMap the
	 * value is an inner HashMap. For every key (file) of inner HashMap the
	 * value is a Set of Integer.
	 */
	public WordIndex() {
		wordIndex = new HashMap<String, HashMap<String, Set<Integer>>>();
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
		HashMap<String, Set<Integer>> innerData;
		Set<Integer> positions;

		innerData = wordIndex.get(word); // grab inner hash map
		if (innerData == null) { // it doesn't exist so create it
			innerData = new HashMap<String, Set<Integer>>();
		}

		positions = innerData.get(file); // grab inner most set
		if (positions == null) { // it doesn't exist so create it
			positions = new HashSet<Integer>();
		}

		positions.add(position); // add index
		innerData.put(file, positions); // add updated list into innerData
		wordIndex.put(word, innerData); // add updated innerData into index
	}

	/**
	 * Adds the array of words at once, assuming the first word in the array is
	 * at position 1.
	 *
	 * @param words
	 *            array of words to add
	 *
	 * @see #addAll(String[], int)
	 */
	public void addAll(String[] words) {
		addAll(words, 1);
	}

	/**
	 * Adds the array of words at once, assuming the first word in the array is
	 * at the provided starting position
	 *
	 * @param words
	 *            array of words to add
	 * @param start
	 *            starting position
	 */
	public void addAll(String[] words, int start) {
		/*
		 * TODO: Add each word using the start position. (You can call your
		 * other methods here.)
		 */
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
		/*
		 * TODO: Return the count.
		 */
		if (wordIndex.get(word) != null)
			return wordIndex.get(word).size();
		else
			return 0;
	}

	/**
	 * Returns the number of words stored in the index.
	 *
	 * @return number of words
	 */
	public int words() {
		/*
		 * TODO: Return number of words. No counting is necessary!
		 */
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
		/*
		 * TODO: Return whether the word is in the index.
		 */
		return wordIndex.containsKey(word) ? true : false;
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
		/*
		 * TODO: Create a copy of the words in the index as a list, and sort
		 * before returning.
		 */
		// return null;
		List<String> words = new ArrayList<String>(wordIndex.keySet());
		words.sort(null);
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
		/*
		 * TODO: Create a copy of the positions for the word, and sort before
		 * returning.
		 */
		List<Integer> words = new ArrayList<Integer>(wordIndex.get(word).get(file));
		Collections.sort(words);
		return words;
	}

	/**
	 * Returns a string representation of this index.
	 */
	@Override
	public String toString() {
		return wordIndex.toString();
	}
}
