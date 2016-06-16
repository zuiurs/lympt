package lympt.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import lympt.utils.*;

public class LymptClient {
	
	public static void main(String[] args) {
		new LymptClient().start();
	}
	
	private static int PORT = 53600;
	
	private static BufferedReader reader = null;
	private static PrintWriter writer = null;
	private static Socket socket = null;
	private static String serverIP = null;
	
	public LymptClient() {
		while (true) {
			try {
				serverIP = OSCommand.inputConsole("ServerIP> ");
				String port_s = OSCommand.inputConsole("PORT(default: 53600)> ");
				if (!port_s.equals("")) {
					PORT = Integer.parseInt(port_s);
				}
				
				socket = new Socket(serverIP, PORT);
				break;
			} catch (Exception e) {
				continue;
			}
		}
	}
	
	void start() {
		correspondence();
	}
	
	/**
	 * basic process
	 */
	void correspondence() {
		try {
			reader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(
					new OutputStreamWriter(socket.getOutputStream()));
			
			System.out.println(
					"Welcome to Lympt!\n"
					+ "Please press \"HELP\", if you don't know other commands.");
			
			while (true) {
				String cmd = OSCommand.inputConsole(true);
				ClientCmdManager.cmdManage(cmd);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	static void reconnect() {
		try {
			System.out.println(
					"Connection lost...\n"
					+ "Building reconnection...");
			
			socket = null;
			socket = new Socket(serverIP, PORT);
			
			System.out.println("Reconnection Successful!");
			
			reader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(
					new OutputStreamWriter(socket.getOutputStream()));
			
			while (true) {
				String cmd = OSCommand.inputConsole(true);
				ClientCmdManager.cmdManage(cmd);
			}
		} catch (Exception e) {
			System.out.println("Reconnection failed.");
		}

	}
	
	static void writerHandle(String cmd) {
//		System.out.println("<----" + cmd + "----");
		writer.println(cmd);
		writer.flush();
	}
	
	static String readerHandle() {
		try {
			String r = reader.readLine();
//			System.out.println("----" + r + "--->");
			return r;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
