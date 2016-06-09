package lympt.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import lympt.utils.*;

public class FinishProcess extends Thread{

	@Override
	public void run() {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(LymptServer.TMP_FILE.getPath());
			br = new BufferedReader(fr);

			String line;
			/* delete all used container */
			while ((line = br.readLine()) != null) {
				OSCommand.exec("docker rm -f " + line); 
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
				LymptServer.TMP_FILE.delete();
				System.out.println(ServerCmdManager.getDate() + "Successful Completion!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
