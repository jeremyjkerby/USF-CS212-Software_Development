
/*
 * Jeremy Kerby
 * Imported from Lab: Word Index
 */

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
	public List<SearchResult> exactSearch(String[] words) {
		String where = "";
		int count = 0;
		int index = 0;

		List<SearchResult> resultList = new ArrayList<>();
		Map<String, SearchResult> resultMap = new HashMap<>();

		// for every query
		for (String word : words) {
			// hello, world
			System.out.println("querying >> " + word);
			// verify it is in inverted index
			if (wordIndex.containsKey(word) == true) {
				// is hello, world in wordIndex?

				// get word index object
				TreeMap<String, TreeSet<Integer>> match = wordIndex.get(word);
				System.out.println("we found  >> " + match.toString());

				// get locations for this object
				Set<String> locations = match.keySet();

				// verify if location is in result
				for (String location : locations) {

					System.out.println("we found in file >> " + location);
					System.out.println("result Map keys >> " + resultMap.keySet().toString());
					where = location;
					count = match.get(location).size();
					index = match.get(location).first();

					if (resultMap.containsKey(location)) {
						// if this location is in our resultMap
						// update an existing search result
						System.out.println("we already have file ");

						SearchResult sr = resultMap.get(location);
						sr.addCount(count);
						sr.updateIndex(index);
					} else {
						// add a new search result to our map
						System.out.println("we dont have file ");

						SearchResult sr = new SearchResult(where, count, index);
						resultMap.put(where, sr);
					}
				}
			}

		}

		Set<String> keys = resultMap.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			resultList.add(resultMap.get(it.next()));
		}
		Collections.sort(resultList);
		return resultList;
	}

	/**
	 * Perform partial search on InvertedIndex with given words
	 * 
	 * @param words
	 * @return sorted list search result
	 */
	public List<SearchResult> partialSearch(String[] words) {
		String where = "";
		int count = 0;
		int index = 0;

		List<SearchResult> resultList = new ArrayList<>();
		Map<String, SearchResult> resultMap = new HashMap<>();

		Set<String> keys = wordIndex.keySet();
		
		// for every query
		for (String word : words) {

			for (String key : keys) {
				// hello, world
				// verify it is in inverted index
				if (key.startsWith(word)) {
					// does wordIndex word start with hello, world?

					// get word index object
					TreeMap<String, TreeSet<Integer>> match = wordIndex.get(key);
					//System.out.println("we found  >> " + match.toString());

					// get locations for this object
					Set<String> locations = match.keySet();

					// verify if location is in result
					for (String location : locations) {

						//System.out.println("we found in file >> " + location);
						//System.out.println("result Map keys >> " + resultMap.keySet().toString());
						where = location;
						count = match.get(location).size();
						index = match.get(location).first();

						if (resultMap.containsKey(location)) {
							// if this location is in our resultMap
							// update an existing search result
							//System.out.println("we already have file ");

							SearchResult sr = resultMap.get(location);
							sr.addCount(count);
							sr.updateIndex(index);
						} else {
							// add a new search result to our map
							//System.out.println("we dont have file ");

							SearchResult sr = new SearchResult(where, count, index);
							resultMap.put(where, sr);
						}
					}
				} // if starts with
			} // inner for
		} // out for

		Set<String> keys2 = resultMap.keySet();
		Iterator<String> it = keys2.iterator();
		while (it.hasNext()) {
			resultList.add(resultMap.get(it.next()));
		}
		Collections.sort(resultList);
		return resultList;
	}

}
