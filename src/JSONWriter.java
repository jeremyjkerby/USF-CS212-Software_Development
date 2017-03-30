
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Writes valid JSON to file beautifully
 *
 */
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
		// special formating, editing the following may give you unwanted output
		Iterator<Integer> it = elements.iterator();
		Integer val;
		
		writer.append("[");
		
		if (it.hasNext()) {
			val = it.next();
			writer.append("\n");
			writer.append(indent(level + 1) + val);
		}
		
		while (it.hasNext()) {
			val = it.next();
			writer.append(",\n");
			writer.append(indent(level + 1) + val);
		}

		writer.append("\n");
		writer.append(indent(level) + "]");
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
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			int level = 1;
			writer.write("{\n");

			Set<String> keys = elements.keySet();
			Iterator<String> it = keys.iterator();

			while (it.hasNext()) {
				String data = it.next();
				writer.write(indent(level) + quote(data) + ": " + elements.get(data));
				if (it.hasNext()) {
					writer.append(",");
				}
				writer.append("\n");
			}

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
		// special formating, editing the following may give you unwanted output
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			writer.append("{\n");

			Set<String> keys = elements.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String data = it.next();
				writer.append(indent(1) + quote(data) + ": {");

				TreeMap<String, TreeSet<Integer>> innerData = elements.get(data);
				Set<String> innerKeys = innerData.keySet();
				Iterator<String> it2 = innerKeys.iterator();

				while (it2.hasNext()) {
					String data2 = it2.next();
					writer.append("\n" + indent(2) + quote(data2) + ": ");
					asArray(writer, innerData.get(data2), 2);
					if (it2.hasNext()) {
						writer.append(",");
					}
				}

				writer.append("\n" + indent(1) + "}");
				if (it.hasNext()) {
					writer.append(",");
				}
				writer.append("\n");
			}
			writer.append("}");
		}
	}

	public static void searchResults(Map<String, List<SearchResult>> map, Path path) throws IOException {
		System.out.println("writing json to file map of size " + map.size());
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			writer.append("[\n");

			// should probably use key set and use that as loop
			Set<String> keys = map.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String search = it.next();
				writer.append(indent(1) + "{\n");
				writer.append(indent(2) + "\"queries\": " + quote(search) + ",\n");
				writer.append(indent(2) + "\"results\": ");
				asArray(writer, map.get(search));
				writer.append("\n" + indent(1) + "}");
				if (it.hasNext()) {
					writer.append(",");
				}
				writer.append("\n");
			}
			writer.append("]");
		}
	}

	// TODO Javadoc and make the indent level a parameter
	private static void asArray(Writer writer, List<SearchResult> data) throws IOException {
		
		writer.append("[\n");

		for (int i = 0; i < data.size(); i++) {
			// TODO Print search result
			writer.append(indent(3) + "{\n");
			writer.append(indent(4) + "\"where\": " + quote(data.get(i).getWhere()) + ",\n");
			writer.append(indent(4) + "\"count\": " + data.get(i).getCount() + ",\n");
			writer.append(indent(4) + "\"index\": " + data.get(i).getIndex() + "\n");
			if (i != data.size() - 1) {
				writer.append(indent(3) + "},\n");
			} else {
				writer.append(indent(3) + "}\n");
			}
		}
		writer.append(indent(2) + "]");
	}
}
