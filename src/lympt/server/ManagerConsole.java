package lympt.server;

import java.io.IOException;

import lympt.utils.*;

public class ManagerConsole extends Thread {

	public ManagerConsole() {
		System.out.println("Connected to ManagerConsole.");
	}
	
	@Override
	public void run() {
		super.run();
		
		while (true) {
			String input = OSCommand.inputConsole(false);
			if (input.toUpperCase().equals("EXIT")) {
				try {
					LymptServer.lymptSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
