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
	boolean running = true;
	private String CLIENT_ADDR;
	private String CONTAINER_ID;
	
	
	//接続ユーザーを表示
	public LymptThread(Socket lymptSocket) {
		this.lymptSocket = lymptSocket;
		CLIENT_ADDR = this.lymptSocket.getRemoteSocketAddress().toString();
		
		System.out.println(ServerCmdManager.getDate() + "Built Connection:\t" + CLIENT_ADDR);
		
	}
	
	@Override
	public void run() {
		super.run();

		// 最終的にこのスレッド用のソケットは閉じる
		try {
			reader = new BufferedReader(
					new InputStreamReader(lymptSocket.getInputStream()));
			writer = new PrintWriter(
					new OutputStreamWriter(lymptSocket.getOutputStream()));

			while (running) {
				String cmd = reader.readLine();
				ServerCmdManager.cmdManage(this, cmd);
			}
		}
		catch (Exception e) {
			System.out.println(ServerCmdManager.getDate() + "Connection lost:\t" + CLIENT_ADDR);
		}
		finally {
			try {
				lymptSocket.close();
				System.out.println(ServerCmdManager.getDate() + "Threading exited:\t" + CLIENT_ADDR);
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
//			return reader.readLine();
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
