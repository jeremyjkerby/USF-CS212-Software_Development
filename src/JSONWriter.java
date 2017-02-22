
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
		// special formating, editing the following may give you unwanted output
		writer.append("[\n");
		//System.out.print("[\n");

		// TODO Try to avoid copying and converting our data
		Integer data[] = elements.toArray(new Integer[elements.size()]);

		for (int i = 0; i < data.length; i++) {
			if (i != data.length - 1) {
				writer.append(indent(level + 1) + data[i] + ",\n");
				//System.out.print(indent(level + 1) + data[i] + ",\n");
			} else {
				writer.append(indent(level + 1) + data[i] + "\n");
				//System.out.print(indent(level + 1) + data[i] + "\n");
			}
		}
		
		// TODO To track if you are at the last element:
		// use an iterator, for each with a counter, or use the methods in treeset

		writer.append(indent(level) + "]");
		//System.out.print(indent(level) + "]");

	}

	// TODO Actually implementing these methods makes your code more general
	
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
		// not used
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
		// not used
	}

	// TODO Try out the treemap methods here too
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

			int size = keys.size();
			int k = 1;
			//System.out.print("{\n");
			writer.append("{\n");
			for (String key : keys) {
				//System.out.print(indent(1) + quote(key) + ": {");
				writer.append(indent(1) + quote(key) + ": {");
				TreeMap<String, TreeSet<Integer>> innerData = elements.get(key);
				Set<String> innerKeys = innerData.keySet();
				int size2 = innerKeys.size();
				int k2 = 1;
				for (String ik : innerKeys) {
					if (k2 != size2) {
						//System.out.print("\n" + indent(2) + quote(ik) + ": ");
						writer.append("\n" + indent(2) + quote(ik) + ": ");
						asArray(writer, innerData.get(ik), 2);
						//System.out.print(",");
						writer.append(",");
						k2++;
					} else {
						//System.out.print("\n" + indent(2) + quote(ik) + ": ");
						writer.append("\n" + indent(2) + quote(ik) + ": ");
						asArray(writer, innerData.get(ik), 2);
					}
				}
				if (k != size) {
					//System.out.print("\n" + indent(1) + "},\n");
					writer.append("\n" + indent(1) + "},\n");
					k++;
				} else {
					//System.out.print("\n" + indent(1) + "}\n");
					writer.append("\n" + indent(1) + "}\n");
				}
			}
			//System.out.print("}");
			writer.append("}");
		}
	}
}
