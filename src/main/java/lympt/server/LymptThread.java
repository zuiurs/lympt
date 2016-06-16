package lympt.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class LymptThread extends Thread {

	private BufferedReader reader = null;
	private PrintWriter writer = null;
	private Socket lymptSocket;
	private String CLIENT_ADDR;
	private String CONTAINER_ID;
	
	boolean running = true;
	
	//接続ユーザーを表示
	public LymptThread(Socket lymptSocket) {
		this.lymptSocket = lymptSocket;
		CLIENT_ADDR = this.lymptSocket.getRemoteSocketAddress().toString();
		
		LymptServer.LOG_MANAGER.write("Built Connection:\t" + CLIENT_ADDR);
	}
	
	@Override
	public void run() {
		try {
			reader = new BufferedReader(
					new InputStreamReader(lymptSocket.getInputStream()));
			writer = new PrintWriter(
					new OutputStreamWriter(lymptSocket.getOutputStream()));

			/* main process */
			while (running) {
				String cmd = reader.readLine();
				ServerCmdManager.cmdManage(this, cmd);
			}
		}
		catch (Exception e) {
			LymptServer.LOG_MANAGER.write("Connection lost:\t" + CLIENT_ADDR);
		}
		finally {
			try {
				lymptSocket.close();
				LymptServer.LOG_MANAGER.write("Threading exited:\t" + CLIENT_ADDR);
				LymptServer.activeUser --;
				if (CONTAINER_ID != null) {
					ServerCmdManager.cmdManage(this, "USED");
				}
			} 
			catch (IOException e) {

			}
		}
	}

	void writerHandle(String cmd) {
//		System.out.println("<----" + cmd + "----");
		writer.println(cmd);
		writer.flush();
	}
	
	String readerHandle() {
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
	
	void setContainerID(String containerID) {
		CONTAINER_ID = containerID;
	}
	
	String getContainerID() {
		return CONTAINER_ID;
	}
}
