package lympt.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Linux Attempt -> Lympt
public class LymptServer {

	public static void main(String[] args) {
		new LymptServer(args[0], args[1]).start();
	}
	
	/* 
	 * サーバー接続用ポート: 53600
	 */
	
	static final int PORT = 53600;
	
	static int activeUser = 0;
	static int containerCapacity = 5;
	static int containerQty = 0;
	
	static ServerSocket lymptSocket = null;
	
	// コンテナイメージの先頭固有名(識別子)
	static String PECULIAR_APP_HEADER = "lympt/";
	
	static File TMP_FILE;
	
	// サーバー用ソケットを開く
	public LymptServer(String containerCapacity, String Identifier) {
		try {
			LymptServer.containerCapacity = Integer.parseInt(containerCapacity);	// DEFAULT: 5
			LymptServer.PECULIAR_APP_HEADER = Identifier;							// DEFAULT: lympt/
		} catch (Exception e) {
			System.out.println("There are some problems in the unit file.");
		}
		try {
			lymptSocket = new ServerSocket(PORT);
			System.out.println(ServerCmdManager.getDate() + "Lympt Server is UP!");
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
	
	//本番環境では不要
	void managerConsole() {
		new ManagerConsole().start();
	}
	
	void makeTmpFile() {
		try {
			TMP_FILE = File.createTempFile("lympt", ".tmp");
			System.out.println(ServerCmdManager.getDate() + "Generated temporary file: " + TMP_FILE.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void monitorResource() {
		//各コンテナのリソースを監視して、閾値を越えればkill
	}
}
