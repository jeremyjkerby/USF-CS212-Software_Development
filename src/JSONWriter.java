
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
				if (it.hasNext()) { // TODO if inside of a while, use thte same approach as asArray
					writer.append(",");
				}
				writer.append("\n");
			}

			writer.write("}");
		}
	}
	
	// TODO Create a helper for TreeMap<String, TreeSet<Integer>>, this calls asArray

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

			// TODO No if inside loops
			
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

	/**
	 * Writes search result map to given path.
	 * 
	 * @param map
	 *            search results to write to file
	 * @param path
	 *            where to save
	 * @throws IOException
	 */
	public static void searchResults(Map<String, List<SearchResult>> map, Path path) throws IOException {
		// special formating, editing the following may give you unwanted output
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			writer.append("[\n");
			
			// TODO if inside the while
			
			int i = 1;
			Set<String> keys = map.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String search = it.next();
				writer.append(indent(i) + "{\n");
				i++;
				writer.append(indent(i) + "\"queries\": " + quote(search) + ",\n");
				writer.append(indent(i) + "\"results\": ");
				asArray(writer, map.get(search), i);
				--i;
				writer.append("\n" + indent(i) + "}");

				if (it.hasNext()) {
					writer.append(",");
				}
				writer.append("\n");
			}

			writer.append("]");
		}
	}

	/**
	 * Writes given List as human readable array. Helper method for
	 * searchResults().
	 * 
	 * @param writer
	 *            writer used to write
	 * @param data
	 *            writes the given data
	 * @param level
	 *            level of indent
	 * @throws IOException
	 */
	private static void asArray(Writer writer, List<SearchResult> data, int level) throws IOException {
		// special formating, editing the following may give you unwanted output
		writer.append("[\n");
		level++;
		for (int i = 0; i < data.size(); i++) {
			writer.append(indent(level) + "{\n");
			level++;
			writer.append(indent(level) + "\"where\": " + quote(data.get(i).getWhere()) + ",\n");
			writer.append(indent(level) + "\"count\": " + data.get(i).getCount() + ",\n");
			writer.append(indent(level) + "\"index\": " + data.get(i).getIndex() + "\n");
			--level;
			if (i != data.size() - 1) {
				writer.append(indent(level) + "},\n");
			} else {
				writer.append(indent(level) + "}\n");
			}
		}
		--level;
		writer.append(indent(level) + "]");
	}
}
