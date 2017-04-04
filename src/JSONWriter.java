
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
			writer.append("{\n");

			Set<String> keys = elements.keySet();
			Iterator<String> it = keys.iterator();

			if (it.hasNext()) {
				String data = it.next();
				writer.append(indent(level) + quote(data) + ": " + elements.get(data));
			}

			while (it.hasNext()) {
				String data = it.next();
				writer.append(",\n");
				writer.append(indent(level) + quote(data) + ": " + elements.get(data));
			}

			writer.append("\n}");
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
			Set<String> keys = elements.keySet();
			Iterator<String> it = keys.iterator();
			int level = 1;

			writer.append("{");

			if (it.hasNext()) {
				String val = it.next();
				writer.append("\n");

				writer.append(indent(level) + quote(val) + ": {");
				level++;
				asNestedObjectHelper(writer, elements.get(val), level);
				--level;
				writer.append("\n" + indent(level) + "}");
			}

			while (it.hasNext()) {
				String val = it.next();
				writer.append(",\n");
				writer.append(indent(level) + quote(val) + ": {");
				level++;
				asNestedObjectHelper(writer, elements.get(val), level);
				--level;
				writer.append("\n" + indent(level) + "}");
			}

			writer.append("\n}");
		}
	}

	/**
	 * Writes given TreeMap as human readable. Helper method for
	 * asNestedObject().
	 * 
	 * @param writer
	 * @param data
	 * @param level
	 * @throws IOException
	 */
	private static void asNestedObjectHelper(Writer writer, TreeMap<String, TreeSet<Integer>> data, int level)
			throws IOException {
		Set<String> innerKeys = data.keySet();
		Iterator<String> it = innerKeys.iterator();

		if (it.hasNext()) {
			String val = it.next();
			writer.append("\n");
			writer.append(indent(level) + quote(val) + ": ");
			asArray(writer, data.get(val), level);

		}

		while (it.hasNext()) {
			String val = it.next();
			writer.append(",\n");
			writer.append(indent(level) + quote(val) + ": ");
			asArray(writer, data.get(val), level);
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
			int level = 1;
			Set<String> keys = map.keySet();
			Iterator<String> it = keys.iterator();
			writer.append("[\n");

			if (it.hasNext()) {
				String val = it.next();
				writer.append(indent(level) + "{\n");
				level++;
				writer.append(indent(level) + "\"queries\": " + quote(val) + ",\n");
				writer.append(indent(level) + "\"results\": ");
				asArray(writer, map.get(val), level);
				--level;
				writer.append("\n" + indent(level) + "}");

			}

			while (it.hasNext()) {
				String val = it.next();
				writer.append(",\n");
				writer.append(indent(level) + "{\n");
				level++;
				writer.append(indent(level) + "\"queries\": " + quote(val) + ",\n");
				writer.append(indent(level) + "\"results\": ");
				asArray(writer, map.get(val), level);
				--level;
				writer.append("\n" + indent(level) + "}");
			}
			writer.append("\n]");
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
