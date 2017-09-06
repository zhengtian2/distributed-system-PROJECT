package ssl;






import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;




public class Worker implements Runnable {
	 private static  int	DEFAULT_PORT= 4444;  
//	 private static final  String SAVE_PATH="/home/ubuntu/Job/";
	 private static final  String SAVE_PATH="/users/zheng/desktop/Job/";

	 private SSLServerSocket     serverSocket;  
	 Socket socket;
	 public Worker( Socket s)
		{
			socket=s;
		}
	 public Worker(){}

	 public  void run() {
		System.out.println("Running thread to receive jobs...");
		try {
			//socket = serverSocket.accept();
			InputStream in;
			in = socket.getInputStream();
			DataInputStream dis=new DataInputStream(in);	
		
			String fileSize;
			fileSize=dis.readUTF();
			System.out.println("jar fileSize is ====>"+fileSize);
			//get file name 
			String jarfilename=dis.readUTF();
				//save the jar file 
			String temp="";//this is the path fro current job process directory
			temp=SAVE_PATH+System.currentTimeMillis();
			File filepath=new File(temp); 
			if (!filepath.exists())
			{filepath.mkdir();
				}
			temp=temp+"/";
				File file = new File(temp+jarfilename);
				FileOutputStream fos=new FileOutputStream(file);
				byte [] inputByte=new byte[1024];
				int length=0;
				int sizeCounter=0;
				  while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
					  	sizeCounter+=length;
	                    fos.write(inputByte, 0, length);
 	                    if(fileSize.equals(Integer.toString(sizeCounter)))
 	                    {
 	                    	break;
 	                    }
	                   
	                }
				  //input file
				   fileSize="";
					fileSize=dis.readUTF();
					System.out.println("input fileSize is===>"+fileSize);
					//get file name 
					 String inputfilename=dis.readUTF();
						//save the  input file 
						 file = new File(temp+inputfilename);
						 fos=new FileOutputStream(file);
						 inputByte=new byte[1024];
						 length=0;
						 sizeCounter=0;
						  while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
							  	sizeCounter+=length;
			                    fos.write(inputByte, 0, length);
		 	                    if(fileSize.equals(Integer.toString(sizeCounter)))
		 	                    {
		 	                    	break;
		 	                    }
			                   
			                }
				  System.out.println("Worker has got the jar file and input file ,now will exe them in a process!..");
				
				  
          
                //If program succeeded in receiving the jar file transfered from the master nodes we will
                //run it here
				  String outputfilepath="";
				  outputfilepath=temp+"output.txt";
				  System.out.println("outputfilepath===>"+outputfilepath);
                ProcessBuilder pb=new ProcessBuilder("java","-jar",jarfilename,temp+inputfilename,outputfilepath);
       		 pb.directory(new File(temp));// set the process builder working environment directory
       		// pb.redirectOutput(outputfile);
       		pb.redirectErrorStream(true);
       		Process process =  pb.start();
       		
            System.out.println("New process has been started...");

            process.waitFor();
            System.out.println("  process is over...");
      		 File outputfile=new File(outputfilepath);//please use Final path to restore every out file

              length=0;
             DataOutputStream dos =new DataOutputStream(socket.getOutputStream());
              FileInputStream fis=new FileInputStream(outputfile);
              fileSize=fis.available()+"";
              dos.writeUTF(fileSize);
              dos.flush();
             // System.out.println("+++++++++=====>>>"+fileSize);
              byte[] outputbyte=new byte[1024];
              while((length=fis.read(outputbyte, 0, outputbyte.length))>0)
              {
            	  dos.write(outputbyte,0,length);
            	  dos.flush();
              }
              System.out.println("finish");
socket.close();
dis.close();
fos.close();
//fis.close();
in.close();
	
		
		}
		catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}  
		
	}
	public void process()
	{
		while(true){
		String response="";
		System.out.println("server started waiting for connnection..");
		Socket s;
	
			try {
				s = serverSocket.accept();
				this.socket=s;

			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("establishing connection now...");
			/****
			 * first get cpu info 
			 * then sent is to master
			 * then wait for master response
			 * **/
			/*DataOutputStream dos =new DataOutputStream(s.getOutputStream());
			dos.writeUTF("this is cpu info");
			DataInputStream	dis=new DataInputStream(s.getInputStream());
			response=dis.readUTF();*/
		
//		if(response.equals("ok"))
//		{
//			System.out.println("**********"+response);
    	Thread t1=new Thread(this);
    	t1.start();
    	try {
			t1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		}
//		else{continue;}
		}
		
	}
	public void init()
	{
		try {
			/*System.setProperty("javax.net.ssl.trustStore", "/home/ubuntu/myClienKeystore");
			System.setProperty("javax.net.ssl.trustStorePassword","123456");
			System.setProperty("javax.net.ssl.keyStore","/home/ubuntu/WorkerKey");
			System.setProperty("javax.net.ssl.keyStorePassword","123456");*/
			System.setProperty("javax.net.ssl.trustStore", "/Users/zheng/Desktop/download/cer/myClienKeystore");
			System.setProperty("javax.net.ssl.trustStorePassword","123456");
			System.setProperty("javax.net.ssl.keyStore","/Users/zheng/Desktop/download/WorkerKey");
			System.setProperty("javax.net.ssl.keyStorePassword","123456");
			
			ServerSocketFactory factory = SSLServerSocketFactory.getDefault();
			serverSocket = (SSLServerSocket)factory.createServerSocket(DEFAULT_PORT);
						System.out.println(serverSocket.isBound());
						/*boolean isConnected=serverSocket.isBound();
						while(!isConnected)
						{	int p=3000;
							p++;
							serverSocket = (SSLServerSocket)factory.createServerSocket(p);
							isConnected=serverSocket.isBound();
						}*/

		}
		catch(Exception e){}
	}
	public static void main (String args[]) throws InterruptedException
	{
		
		Worker w=new Worker();
		
		w.init();
		w.process();
		//GetCpuinfo getcpu=new GetCpuinfo(w);
	//	getcpu.run();
		
	}/*
	 private static void cpu() throws SigarException {
	        Sigar sigar = new Sigar();
	        CpuInfo infos[] = sigar.getCpuInfoList();
	        CpuPerc cpuList[] = null;
	        cpuList = sigar.getCpuPercList();
	        double totalCpuUsage=0.0;
	        for (int i = 0; i < infos.length; i++) {
	           
	            totalCpuUsage=totalCpuUsage+CpuPerc(cpuList[i]);
	        }
	        System.out.println(CpuPerc.format(totalCpuUsage));
	        
	    }
	   private static double CpuPerc(CpuPerc cpu) {
	           
double cpuUsage_double=0.0;
cpuUsage_double=cpu.getCombined();
		 
		   return cpuUsage_double;
	    }

	static class GetCpuinfo implements Runnable
	{
		 private SSLServerSocket     serverSocket;  
		 private Worker woker;
		 public GetCpuinfo(Worker object)
		 {
			 this.serverSocket=object.serverSocket;
			 woker=object;
		 }
		 public GetCpuinfo()
		 {
			 
		 }
		public void run() {
			//if master write ok then invoke process
			woker.process();
		}
		
	}*/
}
