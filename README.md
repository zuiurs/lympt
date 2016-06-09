# lympt

lympt is SSH Docker Server for beginners in Linux.

This project contains a server and a client programs.

***Require version: after Java SE 6.***

## Installation

##### 1. Cloning this project.

	# git clone https://github.com/zuiurs/lympt
	# cd lympt

##### 2. Build .jar file.

	<Server Side>
	# gradle server

	<Client Side>
	# gradle client

##### 3. Execute .jar file.

	# java -jar build/libs/LymptServer.jar <MaxClients> <ContainerPrefix>
	# java -jar build/libs/LymptClient.jar

---
##### TODO

- refactoring
- make rpm file
- set the port, IP address etc. via a configuration file
- SSL Socket?
- study gradle
- improve build.gradle
- installation of gradle
