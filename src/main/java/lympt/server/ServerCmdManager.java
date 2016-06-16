package lympt.server;

import lympt.utils.OSCommand;
import lympt.utils.OSOutputs;

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
		target.writerHandle(String.valueOf(LymptServer.activeUser));
	}
	
	static private void cmdMake(LymptThread target, int containerNum, String password, String ports) {
		boolean isSuccess = false;
		String serverIP = LymptServer.ipv4Addr.getHostAddress();
		String containerID = "";
		String containerPort = "";
		String requiredPortList = generateCommandFormatPortList(ports);

		try {
			String[] containerList = getContainerList();
			String requiredContainer = LymptServer.CONTAINER_HEADER + containerList[containerNum];
			
			LymptServer.LOG_MANAGER.write("Requested container:\t" + requiredContainer);
			
			containerID = OSCommand.exec(String.format(
					"docker run --privileged -P %s -d %s",
					requiredPortList, requiredContainer)).toBasicOutput();
			
			/* Set User's Password */
			OSCommand.exec(String.format(
					"docker exec -t %s /usr/bin/autopassword.sh %s",
					containerID, password));
			
			/* Get Container SSH port */
			containerPort = OSCommand.exec(String.format(
					"docker ps -a | grep %s | sed 's/\\s\\s\\s*/@/g' | cut -d @ -f6 | tr -d \" \" | tr \",\" \"\\n\" | cut -d : -f2 | cut -d / -f1 | grep -G \"^.*->22$\" | cut -d - -f1",
					containerID.substring(0, 12))).toBasicOutput();
			
			isSuccess = true;
			target.writerHandle((isSuccess ? "TRUE": "FALSE") + "," + serverIP + ":" + containerPort);
			
			target.setContainerID(containerID);
			LymptServer.containerQty ++;
			OSCommand.exec(String.format(
					"echo %s >> %s",
					containerID, LymptServer.TMP_FILE.getPath()));
			
			LymptServer.LOG_MANAGER.write("Making Complete!:\t" + target.getContainerID());
			LymptServer.LOG_MANAGER.write("ContainerQuantity:\t" + LymptServer.containerQty + "/" + LymptServer.CONTAINER_CAPACITY);
			
			String portList = OSCommand.exec(String.format(
					"docker ps -a | grep %s | sed 's/\\s\\s\\s*/@/g' | cut -d @ -f6 | tr -d \" \" | tr \",\" \"\\n\" | cut -d : -f2 | cut -d / -f1 | tr \"\\n\" \",\"",
					containerID.substring(0, 12))).toBasicOutput();
			
			target.writerHandle(portList);
		}
		catch (Exception e) {
			isSuccess = false;
			target.writerHandle(isSuccess ? "TRUE": "FALSE" + "," + serverIP + ":" + containerPort);
		}
	}
	
	static private String generateCommandFormatPortList(String ports) {
		// generate port list
		String commandFormatPortList = "";
		String[] portList = ports.split("@");
		for (String port: portList) {
			commandFormatPortList += " -p " + port;
		}
		
		// ex.) " -p 22 -p 80"
		return commandFormatPortList;
	}
	
	static private void cmdCanMake(LymptThread target) {
		if (LymptServer.containerQty < LymptServer.CONTAINER_CAPACITY) {
			target.writerHandle("TRUE");
		}
		else {
			target.writerHandle("FALSE");
		}
	}

	static private void cmdUsed(LymptThread target) {
		OSCommand.exec(String.format(
				"docker rm -f %s",
				target.getContainerID()));
		
		LymptServer.LOG_MANAGER.write("Delete Complete!:\t" + target.getContainerID());
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
		OSOutputs output = OSCommand.exec(String.format(
				"docker images | grep %s | sed 's/\\s\\s\\s*/@/g' | cut -d @ -f1,2 | cut -d / -f2- | tr @ : | tr \"\\n\" \",\"",
				LymptServer.CONTAINER_HEADER));
		
		String containerList = output.toBasicOutput();
		String[] containerListArray = containerList.split(",");

		return containerListArray;
	}
	
	static private void cmdExit(LymptThread target) {
		target.running = false;
	}
}
