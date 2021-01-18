package model;

import java.util.ArrayList;

public class Log {

	private static ArrayList<String> Log = new ArrayList<String>();
	
	public static void addLog(String log) {
		Log.add(log);
	}
	
	public static void reset() {
		Log.clear();
	}

	public static ArrayList<String> getLog() {
		return Log;
	}

}
