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

	public void add(String word, String file, int position) {
		lock.lockReadWrite();
		try {
			super.add(word, file, position);
		} finally {
			lock.unlockReadWrite();
		}
	}

	public void addAll(String[] words, String filename) {
		super.addAll(words, filename);
	}

	public void addAll(String[] words, int start, String filename) {
		super.addAll(words, start, filename);
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
