package lympt.client;

import lympt.utils.*;

public class ClientCmdManager {

	private static final int DEFAULT_CONTAINER_NUMBER = 0;
	
	static void cmdManage(String cmd) {
		cmd = cmd.toUpperCase();
		
		switch (cmd) {
			case "USERS":
				cmdUsers();
				break;
			case "MAKE": 
				cmdMake();
				break;
			case "SHOW":
				cmdShow();
				break;
			case "HELP":
				showHelp();
				break;
			case "EXIT":
				cmdExit();
				System.exit(0);
				break;
			default: 
				System.out.println("The command is not found.");
		}
	}
	
	static private void showHelp() {
		System.out.println(
				"USERS:\tShow Number of Logon Users.\n"
				+ "MAKE\tBuild new Container for you.\n"
				+ "SHOW:\tShow OS list of containers.\n"
				+ "HELP:\tShow this HELP.\n"
				+ "EXIT:\tSee you...");
	}

	static private void cmdUsers() {
		LymptClient.writerHandle("USERS");

		String output = LymptClient.readerHandle(); //形式: 数値
		if (output == null) {
			LymptClient.reconnect();
		}
		else {
			int userQty = Integer.parseInt(output);

			String HEADER = "Logon Users: ";

			if (userQty == 0) {
				System.out.println(HEADER + "Nobody");
			}
			else if (userQty == 1) {
				System.out.println(HEADER + userQty + " person");
			}
			else {
				System.out.println(HEADER + userQty + " people");
			}
		}		
	}
	
	static private void cmdMake() {
		if (!canMake()) {
			System.out.println("Server capacity of container is over.\n"
					+ "Please try again after some times.");
			return;
		}
		
		int CONTAINER_NUMBER = selectNumber();
		String userPasseord = inputUserPassword();
		String portList = generatePortList();
		
		// MAKE,[container number],[user password],[port@port@port@] 
		LymptClient.writerHandle("MAKE" + "," + CONTAINER_NUMBER + "," + userPasseord + "," + portList);
		// MAKE,1 -> require container number 1
		
		String output = LymptClient.readerHandle(); //形式: boolean,ip:port
		String[] outputs = output.split(",");
		
		boolean isMakeSuccess = outputs[0].equals("TRUE") ? true: false;
		String IPAndPort = outputs[1];
		
		if (isMakeSuccess) {
			System.out.println(
					"\nMaking container is Successful!!\n"
					+ "You can access to " + IPAndPort + " as root with SSH.\n"
					+ "EX.) ssh root@ServerIP -p port");
			
			showPortList();
			
			// 以下終了処理
			System.out.println("\nPlease type \"EXIT\", you finished using the container.");
			while (true) {
				String cmd = OSCommand.inputConsole(false);
				if (cmd.toUpperCase().equals("EXIT")) {
					LymptClient.writerHandle("USED");
					break;
				}
				else {
					continue;
				}
			}
		}
		else {
			System.out.println("\nOops! Making container is Failed...");
		}
	}
	
	static private boolean canMake() {
		LymptClient.writerHandle("CANMAKE");
		String output = LymptClient.readerHandle();
		boolean canMake = (output.equals("TRUE")) ? true: false;
		return canMake;
	}
	
	static private int selectNumber() {
		cmdShow();
		String input = OSCommand.inputConsole("SelectNumber >");

		int selectedNumber;
		try {
			selectedNumber = Integer.parseInt(input);
		}
		catch (NumberFormatException e) {
			selectedNumber = DEFAULT_CONTAINER_NUMBER;	
		}

		return selectedNumber;
	}
	
	static private String inputUserPassword() {
		String password = "";
		while (true) {
			String pass1 = OSCommand.inputPassword("Type Your Password>");
			String pass2 = OSCommand.inputPassword("Retype Your Password>");
			if (pass1.equals(pass2)) {
				password = pass1;
				break;
			}
			else {
				System.out.println("Both passwords don't matching.");
				continue;
			}
		}
		return password;
	}
	
	// ex.) 300@22@40@200@
	static String generatePortList() {
		String portList = "22@";
		
		System.out.println("");
		System.out.println("Type port number you want to use.\nIf you would like finishing this process, please type \".\"");
		while (true) {
			try {
				String input = OSCommand.inputConsole("PORT>");
				if (input.equals(".")) {
					break;
				}
				Integer.parseInt(input);
				portList += input + "@";
			} catch (Exception e) {
				System.out.println("It's bad format. Try again.");
				continue;
			}
		}
		return portList;
	}
	
	static private void showPortList() {
		String output = LymptClient.readerHandle();
		String[] portList = output.split(",");

		System.out.println("===PORT-LIST===");
		for (String port: portList) {
			System.out.println(port);
		}
		System.out.println("===============");
	}
	
	static private void cmdShow() {
		LymptClient.writerHandle("SHOW");
		
		String output = LymptClient.readerHandle(); // 形式: containerName, containerName
		String[] outputs = output.split(",");

		for (int i = 0; i < outputs.length; i ++) {
			System.out.printf("[%d]\t%s\n", i, outputs[i]);
		}
	}
	
	static private void cmdExit() {
		LymptClient.writerHandle("EXIT");
	}
}
