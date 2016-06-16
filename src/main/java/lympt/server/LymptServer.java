package lympt.server;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/* Linux Attempt -> Lympt */
public class LymptServer {

	public static void main(String[] args) {
		new LymptServer(args[0], args[1], args[2], args[3]).start();
	}

	static int activeUser = 0;
	static int containerQty = 0;
	
	static ServerSocket lymptSocket;
	
	/* default settings */
	//TODO: set final modifier
	static int CONTAINER_CAPACITY	= 5;		/* the max number of container images */
	static String CONTAINER_HEADER 	= "lympt/";	/* identifier of container images */
	static String LOG_PATH			= "/var/log/lympt.log";	/* location of log file */
	static int PORT					= 53600;	/* sever port */
	
	static File TMP_FILE;
	
	static LymptLogManager LOG_MANAGER;
	
	static Inet4Address ipv4Addr;

	public LymptServer(String containerCapacity, String identifier, String logPath, String port) {
		try {
			LymptServer.CONTAINER_CAPACITY	= isDefault(containerCapacity)
												? CONTAINER_CAPACITY
												: Integer.parseInt(containerCapacity);

			LymptServer.CONTAINER_HEADER	= isDefault(identifier)
												? CONTAINER_HEADER
												: identifier;

			LymptServer.LOG_PATH			= isDefault(logPath)
												? LOG_PATH
												: logPath;
			
			LymptServer.PORT				= isDefault(port)
												? PORT
												: Integer.parseInt(port);
			
			LOG_MANAGER = new LymptLogManager(LOG_PATH);
			ipv4Addr = (Inet4Address) Inet4Address.getLocalHost();
			
		} catch (Exception e) {
			LOG_MANAGER.write("There are some problems in /etc/sysconfig/lympt.");
		}
		try {
			lymptSocket = new ServerSocket(PORT);
			LOG_MANAGER.write("Lympt Server is UP!");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {

		}
	}
	
	void start() {
		monitorResource();
	
//		managerConsole(); /* for debugging */
		
		makeTmpFile();
		
		Runtime.getRuntime().addShutdownHook(new FinishProcess());
		
		makeSeveralUserSocket();
		
	}
	
	/**
	 * accept loop
	 */
	void makeSeveralUserSocket() {
		while (true) {
			try {
				Socket severalSocket = lymptSocket.accept();
				activeUser ++;
				new LymptThread(severalSocket).start();
			}
			catch (IOException e) {
				break;
			}
		}
	}
	
	/* for debugging */
	void managerConsole() {
		new ManagerConsole().start();
	}
	
	/**
	 * generate temporary file
	 */
	private void makeTmpFile() {
		try {
			TMP_FILE = File.createTempFile("lympt-", ".tmp");	/* lympt-xxxxxxxxxxx.tmp */
			LOG_MANAGER.write("Generated temporary file: " + TMP_FILE.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void monitorResource() {
		//TODO
	}
	
	/**
	 * Check whether the String value is "default".
	 * The value's Upper/Lower case is ignored.
	 * @param s		check this value
	 * @return		if the value is "default", return true.
	 */
	private static boolean isDefault(String s) {
		if (s.equalsIgnoreCase("default")) {
			return true;
		}
		else {
			return false;
		}
	}
}
