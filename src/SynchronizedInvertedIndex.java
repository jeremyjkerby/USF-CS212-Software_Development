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

	@Override
	public void add(String word, String file, int position) {
		lock.lockReadWrite();
		try {
			super.add(word, file, position);
		} finally {
			lock.unlockReadWrite();
		}
	}

	@Override
	public void addAll(String[] words, int start, String filename) {
		lock.lockReadWrite();
		for (int i = 0; i < words.length; i++) {
			super.add(words[i], filename, start);
			start++;
		}
		lock.unlockReadWrite();
	}

	@Override
	public void addAll(InvertedIndex other) {
		lock.lockReadWrite();
		try {
			super.addAll(other);
		} finally {
			lock.unlockReadWrite();
		}
	}

	@Override
	public int count(String word) {
		lock.lockReadOnly();
		try {
			return super.count(word);
		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public int words() {
		lock.lockReadOnly();
		try {
			return super.words();
		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public boolean contains(String word) {
		lock.lockReadOnly();
		try {
			return super.contains(word);
		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public List<String> copyWords() {
		lock.lockReadOnly();
		try {
			return super.copyWords();
		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public List<Integer> copyPositions(String word, String file) {
		lock.lockReadOnly();
		try {
			return super.copyPositions(word, file);
		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public String toString() {
		lock.lockReadOnly();
		try {
			return super.toString();
		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public void toJSON(Path path) {
		lock.lockReadOnly();
		try {
			super.toJSON(path);
		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public List<SearchResult> exactSearch(String[] words) {
		lock.lockReadOnly();
		try {
			return super.exactSearch(words);
		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public List<SearchResult> partialSearch(String[] words) {
		lock.lockReadOnly();
		try {
			return super.partialSearch(words);
		} finally {
			lock.unlockReadOnly();
		}
	}

}
