
/*
 * Jeremy Kerby
 * Imported from Lab: Argument Parser
 */

import java.util.HashMap;
import java.util.Map;

/**
 * Parses command-line arguments into flag/value pairs, and stores those pairs
 * in a map for easy access.
 */
public class ArgumentMap {

	private final Map<String, String> arguments;

	/**
	 * Initializes the argument map.
	 */
	public ArgumentMap() {
		arguments = new HashMap<>();
	}

	/**
	 * Initializes the argument map and parses the specified arguments into
	 * key/value pairs.
	 *
	 * @param args
	 *            command line arguments
	 *
	 * @see #parse(String[])
	 */
	public ArgumentMap(String[] args) {
		this();
		parse(args);
	}

	/**
	 * Parses the specified arguments into key/value pairs and adds them to the
	 * argument map.
	 *
	 * @param args
	 *            command line arguments
	 */
	public void parse(String[] args) {
		boolean FLAG = false;
		for (int i = 0; i < args.length; i++) {
			if (isFlag(args[i]) == true) { // verify if flag
				// args[i] is a flag
				FLAG = true;
				arguments.put(args[i], null); // add flag to map
			} else {
				// args[i] is not a flag
				if (FLAG == true) { // prev arg was flag so this is its value
					arguments.putIfAbsent(args[i - 1], args[i]); // save value
																	// to prev
					FLAG = false;
				}
			}
		}
	}

	/**
	 *
	 * @param arg
	 * @return
	 */
	public static boolean isFlag(String arg) {
		if (arg == null)
			return false;
		return (arg.matches("-[[-]a-zA-Z0-9].*") ? true : false);
	}

	/**
	 *
	 * @param arg
	 * @return
	 */
	public static boolean isValue(String arg) {
		if (arg == null)
			return false;
		return (arg.matches("[^-\\s].*") ? true : false);
	}

	/**
	 * Returns the number of unique flags stored in the argument map.
	 *
	 * @return number of flags
	 */
	public int numFlags() {
		return arguments.size();
	}

	/**
	 * Determines whether the specified flag is stored in the argument map.
	 *
	 * @param flag
	 *            flag to test
	 *
	 * @return true if the flag is in the argument map
	 */
	public boolean hasFlag(String flag) {
		return arguments.containsKey(flag);
	}

	/**
	 * Determines whether the specified flag is stored in the argument map and
	 * has a non-null value stored with it.
	 *
	 * @param flag
	 *            flag to test
	 *
	 * @return true if the flag is in the argument map and has a non-null value
	 */
	public boolean hasValue(String flag) {
		return (arguments.containsKey(flag) && arguments.get(flag) != null) ? true : false;
	}

	/**
	 * Returns the value for the specified flag as a String object.
	 *
	 * @param flag
	 *            flag to get value for
	 *
	 * @return value as a String or null if flag or value was not found
	 */
	public String getString(String flag) {
		return arguments.get(flag);
	}

	/**
	 * Returns the value for the specified flag as a String object. If the flag
	 * is missing or the flag does not have a value, returns the specified
	 * default value instead.
	 *
	 * @param flag
	 *            flag to get value for
	 * @param defaultValue
	 *            value to return if flag or value is missing
	 * @return value of flag as a String, or the default value if the flag or
	 *         value is missing
	 */
	public String getString(String flag, String defaultValue) {
		String data = arguments.getOrDefault(flag, defaultValue);
		return (data == null) ? data = defaultValue : data;
	}

	/**
	 * Returns the value for the specified flag as an int value. If the flag is
	 * missing or the flag does not have a value, returns the specified default
	 * value instead.
	 *
	 * @param flag
	 *            flag to get value for
	 * @param defaultValue
	 *            value to return if the flag or value is missing
	 * @return value of flag as an int, or the default value if the flag or
	 *         value is missing
	 */
	public int getInteger(String flag, int defaultValue) {
		String data = arguments.get(flag);
		int answer;
		if (data == null || !data.matches("[0-9]"))
			answer = defaultValue;
		else
			answer = Integer.parseInt(data);
		return answer;
	}

	@Override
	public String toString() {
		return arguments.toString();
	}
}
