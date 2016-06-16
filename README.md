# lympt

lympt is SSH Docker Server for beginners in Linux.

This project contains a server and a client programs.

***Require version: after Java SE 6.***

## Installation

###### 1. Clone this project.

	$ git clone https://github.com/zuiurs/lympt

### Server Side

###### 2. Build .jar file.

	$ cd lympt
	$ gradle server

###### (2.5. Execute .jar file.)

You can execute this program at this time.
	
	$ sudo java -jar build/libs/LymptServer.jar <MaxClients> <ContainerPrefix> <LogFilePath> <Port>

##### 3. Set up configuration, service and execute file.

	$ su -
	
Return the project root directory.

	# ./setup.sh

##### (3.5. Build docker images for this service.)

If you don't have any images for this service, you can use sample Dockerfiles.  
This script builds all docker images in ./misc/server/docker\_files.

	# systemctl start docker.service
	# ./setup-docker-image.sh

##### 4. Start the service

	# systemctl start lympt.service

### Client Side

###### 2. Build .jar file.

	$ cd lympt
	$ gradle client

###### 3. Execute .jar file.
	
	$ java -jar build/libs/LymptClient.jar

After this, please follow a HELP command.

---
##### TODO

- refactoring
- make rpm file
- SSL Socket?
- study gradle
- improve build.gradle
