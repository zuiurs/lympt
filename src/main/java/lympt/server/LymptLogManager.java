package lympt.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LymptLogManager {

	private File logFile;
	private FileWriter logFileWriter;
	
	private final static String DEFAULT_PATH = "/var/log/lympt.log";
	
	/**
	 * Create a new LogManager instance by default path
	 */
	public LymptLogManager() {
		this(DEFAULT_PATH);
	}
	
	/**
	 * Create a new LogManager instance by path name
	 * @param path	log file path
	 */
	public LymptLogManager(String path) {
		logFile = new File(path);
		
		try {
			/* append mode */
			logFileWriter = new FileWriter(logFile, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write to log file (echo on)
	 * @param message	log message
	 */
	public void write(String message) {
		write(message, true);
	}
	
	/**
	 * Write to log file
	 * @param message	log message
	 * @param echo 		output to stdout
	 */
	public void write(String message, boolean echo) {
		if (echo) {
			System.out.println(getDate() + message);
		}
		
		try {
			logFileWriter.write(getDate() + message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * get current date
	 */
	static String getDate() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("[MMM dd HH:mm:ss] ");

        return dateFormat.format(date);
	}
}
