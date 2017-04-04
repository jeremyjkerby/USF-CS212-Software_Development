
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(Enclosed.class)
public class InvertedIndexTest {

	@Rule
	public Timeout globalTimeout = Timeout.seconds(30);

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public static class ExactSearchTests {

		private InvertedIndex ii;
		private String[] pokemon = { "eevee", "pikachu", "snorlax", "charmander", "squirtle" };
		private String[] characters = { "ash", "brock", "misty" };

		@Before
		public void setup() {
			ii = new InvertedIndex();
		}

		@Test
		public void test01OneWordOneFileExact() {
			String query[] = { "pikachu" };
			ii.addAll(pokemon, "simple/test.html");
			List<SearchResult> result = ii.exactSearch(query);
			int actual = result.size();
			int expected = 1;
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void test02TwoWordsOneFileExact() {
			String query[] = { "pikachu", "charmander" };
			ii.addAll(pokemon, "simple/test.html");
			List<SearchResult> result = ii.exactSearch(query);
			int actual = result.size();
			int expected = 1;
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void test03TwoWordsTwoFilesExact() {
			String query[] = { "pikachu", "misty" };
			ii.addAll(pokemon, "simple/test.html");
			ii.addAll(characters, "simple/exam.html");
			List<SearchResult> result = ii.exactSearch(query);
			int actual = result.size();
			int expected = 2;
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void test04OnePartialWordFailExact() {
			String query[] = { "pika" };
			ii.addAll(pokemon, "simple/test.html");
			List<SearchResult> result = ii.exactSearch(query);
			int actual = result.size();
			int expected = 0;
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void test05OneWordFailExact() {
			String query[] = { "bulbasaur" };
			ii.addAll(pokemon, "simple/test.html");
			List<SearchResult> result = ii.exactSearch(query);
			int actual = result.size();
			int expected = 0;
			Assert.assertEquals(expected, actual);
		}

	}

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public static class PartialSearchTests {

		private InvertedIndex ii;
		private String[] pokemon = { "eevee", "pikachu", "snorlax", "charmander", "squirtle" };
		private String[] characters = { "ash", "brock", "misty" };

		@Before
		public void setup() {
			ii = new InvertedIndex();
		}

		@Test
		public void test01OneWordOneFilePart() {
			String query[] = { "pika" };
			ii.addAll(pokemon, "simple/test.html");
			List<SearchResult> result = ii.partialSearch(query);
			int actual = result.size();
			int expected = 1;
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void test02TwoWordsOneFilePart() {
			String query[] = { "pika", "char" };
			ii.addAll(pokemon, "simple/test.html");
			List<SearchResult> result = ii.partialSearch(query);
			int actual = result.size();
			int expected = 1;
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void test03TwoWordsTwoFilesPart() {
			String query[] = { "pika", "mi" };
			ii.addAll(pokemon, "simple/test.html");
			ii.addAll(characters, "simple/exam.html");
			List<SearchResult> result = ii.partialSearch(query);
			int actual = result.size();
			int expected = 2;
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void test04InvalidWordsOneFilePart() {
			String query[] = { "", "$" };
			ii.addAll(pokemon, "simple/test.html");
			ii.addAll(characters, "simple/exam.html");
			List<SearchResult> result = ii.partialSearch(query);
			int actual = result.size();
			int expected = 0;
			Assert.assertEquals(expected, actual);
		}

		@Test
		public void test05OneWordFailPart() {
			String query[] = { "ikachu" };
			ii.addAll(pokemon, "simple/test.html");
			List<SearchResult> result = ii.partialSearch(query);
			int actual = result.size();
			int expected = 0;
			Assert.assertEquals(expected, actual);
		}
	}
}