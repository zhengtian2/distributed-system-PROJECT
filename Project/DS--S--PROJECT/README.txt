		Parameter format for worker.jar and master.jar

1.format of Master.jar
java -jar master.jar 

2.format of Worker.jar
java -jar worker.jar <port> <save path> <Server Keystore Path> <Client Keystore Path>
	//args[0] is DEFAULT_PORT
    	//args[1] is save path of server(vm)
    	//args[2] is myClienKeystore path
    	//args[3] is WorkerKey path 
	here is an example of format:
java -jar Worker.jar 4444 /users/zheng/desktop/Job/ /Users/zheng/Desktop/download/cer/myClienKeystore /Users/zheng/Desktop/download/WorkerKey

