package lympt.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LymptLogManager {

	private File logFile;
	private FileWriter logFileWriter;
	
	private final static String DEFAULT_PATH = "/var/log/lympt.log";
	
	public LymptLogManager() {
		this(DEFAULT_PATH);
	}
	
	public LymptLogManager(String path) {
		logFile = new File(path);
		
		try {
			logFileWriter = new FileWriter(logFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(String message) {
		write(message, true);
	}
	
	/**
	 * write to log file
	 * @param message	log message
	 * @param echo 		output to stdout
	 */
	public void write(String message, boolean echo) {
		if (echo) {
			System.out.println(message);
		}
		
		logFileWriter
	}
}
