package lympt.server;

import java.text.SimpleDateFormat;
import java.util.Date;

import lympt.utils.*;

public class ServerCmdManager {

	static void cmdManage(LymptThread target, String cmd) {
		String[] cmds = cmd.split(",");
		String operator = cmds[0];
		String operand = "";
		String password = "";
		String operands = "";
		if (cmds.length != 1) {
			operand = cmds[1];
			password = cmds[2];
			operands = cmds[3];
		}
		

		operator = operator.toUpperCase();
			
		switch (operator) {
			case "USERS":
				cmdUsers(target);
				break;
			case "MAKE": 
				int containerNum = Integer.parseInt(operand);
				String ports = operands;
				cmdMake(target, containerNum, password, ports);
				break;
			case "CANMAKE":
				cmdCanMake(target);
				break;
			case "SHOW":
				cmdShow(target);
				break;
			case "USED":
				cmdUsed(target);
				break;
			case "EXIT":
				cmdExit(target);
				break;
		}
	}
	
	static private void cmdUsers(LymptThread target) {
		target.writerHandle("" + LymptServer.activeUser);
	}
	
	static private void cmdMake(LymptThread target, int containerNum, String password, String ports) {
		boolean isSuccess = false;
		//TODO: IP をサーバーから直接取得するように
		String serverIP = "192.168.138.128";
		String containerID = "";
		String containerPort = "";
		String requiredPortList = generateCommandFormatPortList(ports);

		try {
			String[] containerList = getContainerList();
			String requiredContainer = LymptServer.PECULIAR_APP_HEADER + containerList[containerNum];
			
			System.out.println(getDate() + "Requested container:\t" + requiredContainer);
			
			containerID = OSCommand.exec("docker run --privileged -P" + requiredPortList + " -d " + requiredContainer).toBasicOutput();
			
			// Set User's Password
			String com = "docker exec -t " + containerID + " /usr/bin/autopassword.sh " + password;
//			System.out.println(com);
			OSOutputs oo = OSCommand.exec(com);
//			System.out.println("basic" + oo.toBasicOutput());
//			System.out.println("error" + oo.toErrorOutput());
			
			// Get Container SSH port
			containerPort = OSCommand.exec(
					"docker ps -a | grep " + containerID.substring(0, 12) + " | sed 's/\\s\\s\\s*/@/g' | cut -d @ -f6 | tr -d \" \" | tr \",\" \"\\n\" | cut -d : -f2 | cut -d / -f1 | grep -G \"^.*->22$\" | cut -d - -f1"
					).toBasicOutput();
			
			isSuccess = true;
			target.writerHandle((isSuccess ? "TRUE": "FALSE") + "," + serverIP + ":" + containerPort);
			
			
			target.setContainerID(containerID);
			LymptServer.containerQty ++;
			OSCommand.exec("echo " + containerID + " >> " + LymptServer.TMP_FILE.getPath());
			System.out.println(getDate() + "Making Complete!:\t" + target.getContainerID());
			System.out.println(getDate() + "ContainerQuantity:\t" + LymptServer.containerQty + "/" + LymptServer.containerCapacity);
			
			String portList = OSCommand.exec(
					"docker ps -a | grep " + containerID.substring(0, 12) + " | sed 's/\\s\\s\\s*/@/g' | cut -d @ -f6 | tr -d \" \" | tr \",\" \"\\n\" | cut -d : -f2 | cut -d / -f1 | tr \"\\n\" \",\""
					).toBasicOutput();
			target.writerHandle(portList);
		}
		catch (Exception e) {
			isSuccess = false;
			target.writerHandle(isSuccess ? "TRUE": "FALSE" + "," + serverIP + ":" + containerPort);
		}
	}
	
	static private String generateCommandFormatPortList(String ports) {
		//portリストを作成する
		String commandFormatPortList = "";
		String[] portList = ports.split("@");
		for (String port: portList) {
			commandFormatPortList += " -p " + port;
		}
		
		// ex.) " -p 22 -p 80"
		return commandFormatPortList;
	}
	
	static private void cmdCanMake(LymptThread target) {
		if (LymptServer.containerQty < LymptServer.containerCapacity) {
			target.writerHandle("TRUE");
		}
		else {
			target.writerHandle("FALSE");
		}
	}

	static private void cmdUsed(LymptThread target) {
		OSCommand.exec("docker rm -f " + target.getContainerID());
		System.out.println(getDate() + "Delete Complete!:\t" + target.getContainerID());
		LymptServer.containerQty --;
		target.setContainerID(null);
	}
	
	static private void cmdShow(LymptThread target) {
		String[] containerList = getContainerList();

		String containers = "";
		for (int i = 0; i < containerList.length; i ++) {
			if (i == 0) {
				containers += containerList[i];
			}
			else {
				containers += "," + containerList[i];
			}
		}
		
		target.writerHandle(containers);
	}
	
	static private String[] getContainerList() {
		OSOutputs output = OSCommand.exec(
				"docker images | grep " + LymptServer.PECULIAR_APP_HEADER + " | sed 's/\\s\\s\\s*/@/g' | cut -d @ -f1,2 | cut -d / -f2- | tr @ : | tr \"\\n\" \",\""
				);
		String containerList = output.toBasicOutput();
		String[] containerListArray = containerList.split(",");

		return containerListArray;
	}
	
	static private void cmdExit(LymptThread target) {
		target.running = false;
	}
	
	static String getDate() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("[MMM dd HH:mm:ss] ");

        return dateFormat.format(date);
	}
}
