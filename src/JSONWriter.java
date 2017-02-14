
/*
 * Jeremy Kerby
 * Imported from Lab: JSONWriter
 */

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class JSONWriter {

	/**
	 * Returns a String with the specified number of tab characters.
	 *
	 * @param times
	 *            number of tab characters to include
	 * @return tab characters repeated the specified number of times
	 */

	public static String indent(int times) {
		char[] tabs = new char[times];
		Arrays.fill(tabs, '\t');
		return String.valueOf(tabs);
	}

	/**
	 * Returns a quoted version of the provided text.
	 *
	 * @param text
	 *            text to surround in quotes
	 * @return text surrounded by quotes
	 */
	public static String quote(String text) {
		return String.format("\"%s\"", text);
	}

	/**
	 * Writes the set of elements as a JSON array at the specified indent level.
	 *
	 * @param writer
	 *            writer to use for output
	 * @param elements
	 *            elements to write as JSON array
	 * @param level
	 *            number of times to indent the array itself
	 * @throws IOException
	 */
	private static void asArray(Writer writer, TreeSet<Integer> elements, int level) throws IOException {
		/*
		 * TODO This is optional, but if you implement this method you can reuse
		 * it in several places in this class.
		 */
		writer.append("[\n");
		System.out.print("[\n");

		Integer data[] = elements.toArray(new Integer[elements.size()]);

		for (int i = 0; i < data.length; i++) {
			if (i != data.length - 1) {
				writer.append(indent(level + 1) + data[i] + ",\n");
				System.out.print(indent(level + 1) + data[i] + ",\n");
			} else {
				writer.append(indent(level + 1) + data[i] + "\n");
				System.out.print(indent(level + 1) + data[i] + "\n");
			}
		}

		writer.append(indent(level) + "]");
		System.out.print(indent(level) + "]");

	}

	/**
	 * Writes the set of elements as a JSON array to the path using UTF8.
	 *
	 * @param elements
	 *            elements to write as a JSON array
	 * @param path
	 *            path to write file
	 * @throws IOException
	 */
	public static void asArray(TreeSet<Integer> elements, Path path) throws IOException {
		// TODO
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

			int level = 1;

			asArray(writer, elements, level);

		}
	}

	/**
	 * Writes the map of elements as a JSON object to the path using UTF8.
	 *
	 * @param elements
	 *            elements to write as a JSON object
	 * @param path
	 *            path to write file
	 * @throws IOException
	 */
	public static void asObject(TreeMap<String, Integer> elements, Path path) throws IOException {
		// TODO Use try-with-resources (no catch block needed)
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

			int level = 1;

			// System.out.print("{\n");
			writer.write("{\n");

			int size = elements.size();
			int n = 1;

			Set<String> keys = elements.keySet();
			for (String key : keys) {
				if (n == size) {
					// System.out.print(indent(level) + quote(key) + ": " +
					// elements.get(key) + "\n");
					writer.write(indent(level) + quote(key) + ": " + elements.get(key) + "\n");
				} else {
					// System.out.print(indent(level) + quote(key) + ": " +
					// elements.get(key) + ",\n");
					writer.write(indent(level) + quote(key) + ": " + elements.get(key) + ",\n");
					n++;
				}
			}

			// System.out.print("}\n");
			writer.write("}");

		}

	}

	/**
	 * Writes the set of elements as a JSON object with a nested array to the
	 * path using UTF8.
	 *
	 * @param elements
	 *            elements to write as a JSON object with a nested array
	 * @param path
	 *            path to write file
	 * @throws IOException
	 */
	public static void asNestedObject(TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements, Path path)
			throws IOException {
		// TODO Use try-with-resources (no catch block needed)
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

			Set<String> keys = elements.keySet();

			int size = keys.size();
			int k = 1;
			System.out.print("{\n");
			writer.append("{\n");
			for (String key : keys) {
				System.out.print(indent(1) + quote(key) + ": {");
				writer.append(indent(1) + quote(key) + ": {");
				TreeMap<String, TreeSet<Integer>> innerData = elements.get(key);
				Set<String> innerKeys = innerData.keySet();
				int size2 = innerKeys.size();
				int k2 = 1;
				for (String ik : innerKeys) {
					if (k2 != size2) {
						System.out.print("\n"+indent(2) + quote(ik) + ": ");
						writer.append("\n"+indent(2) + quote(ik) + ": ");
						asArray(writer, innerData.get(ik), 2);
						System.out.print(",");
						writer.append(",");
						k2++;
					} else {
						System.out.print("\n"+indent(2) + quote(ik) + ": ");
						writer.append("\n"+indent(2) + quote(ik) + ": ");
						asArray(writer, innerData.get(ik), 2);
					}
				}
				if (k != size) {
					System.out.print("\n" + indent(1) + "},\n");
					writer.append("\n" + indent(1) + "},\n");
					k++;
				} else {
					System.out.print("\n" + indent(1) + "}\n");
					writer.append("\n" + indent(1) + "}\n");
				}
			}
			System.out.print("}");
			writer.append("}");
		}
	}
}
