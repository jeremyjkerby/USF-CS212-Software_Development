import java.nio.file.Path;
import java.util.List;

public class SynchronizedInvertedIndex extends InvertedIndex {

	private ReadWriteLock lock;

	/**
	 * A thread-safe version of InvertedIndex
	 */
	public SynchronizedInvertedIndex() {
		super();
		lock = new ReadWriteLock();
	}
	
	// TODO Use the @Override annotation

	public void add(String word, String file, int position) {
		lock.lockReadWrite();
		try {
			super.add(word, file, position);
		} finally {
			lock.unlockReadWrite();
		}
	}
	
	// TODO Don't override unless you want to change the implementation
	// TODO Remove this
	public void addAll(String[] words, String filename) {
		super.addAll(words, filename);
	}

	// TODO Keep this but change the implmentation 
	public void addAll(String[] words, int start, String filename) {
		super.addAll(words, start, filename);

		// TODO
//		lock
//		for (int i = 0; i < words.length; i++) {
//			super.add(words[i], filename, start);
//			start++;
//		}	
//		unlock
	}

	public int count(String word) {
		lock.lockReadOnly();
		try {
			return super.count(word);
		} finally {
			lock.unlockReadOnly();
		}
	}

	public int words() {
		lock.lockReadOnly();
		try {
			return super.words();
		} finally {
			lock.unlockReadOnly();
		}
	}

	public boolean contains(String word) {
		lock.lockReadOnly();
		try {
			return super.contains(word);
		} finally {
			lock.unlockReadOnly();
		}
	}

	public List<String> copyWords() {
		lock.lockReadOnly();
		try {
			return super.copyWords();
		} finally {
			lock.unlockReadOnly();
		}
	}

	public List<Integer> copyPositions(String word, String file) {
		lock.lockReadOnly();
		try {
			return super.copyPositions(word, file);
		} finally {
			lock.unlockReadOnly();
		}
	}

	public String toString() {
		lock.lockReadOnly();
		try {
			return super.toString();
		} finally {
			lock.unlockReadOnly();
		}
	}

	public void toJSON(Path path) {
		lock.lockReadOnly();
		try {
			super.toJSON(path);
		} finally {
			lock.unlockReadOnly();
		}
	}

	public List<SearchResult> exactSearch(String[] words) {
		lock.lockReadOnly();
		try {
			return super.exactSearch(words);
		} finally {
			lock.unlockReadOnly();
		}
	}

	public List<SearchResult> partialSearch(String[] words) {
		lock.lockReadOnly();
		try {
			return super.partialSearch(words);
		} finally {
			lock.unlockReadOnly();
		}
	}

}
