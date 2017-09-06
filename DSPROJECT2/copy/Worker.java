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
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.TrustManagerFactory;


public class Worker implements Runnable {
	 private static final int	DEFAULT_PORT= 7777;  
	 private static final String SERVER_KEY_STORE_PASSWORD = "123456";  
	 private static final String SERVER_TRUST_KEY_STORE_PASSWORD = "654321";  
	 private static final String SAVE_PATH="/users/ZHENG/desktop/movetotrash/"
	 		+ "/";
	 private SSLServerSocket     serverSocket;  
	@Override
	public  void run() {
		Socket socket;
		try {
			socket = serverSocket.accept();
			InputStream in;

			in = socket.getInputStream();
			DataInputStream dis=new DataInputStream(in);	
		
			String fileSize;
			fileSize=dis.readUTF();
			System.out.println("fileSize====>"+fileSize);
			//get file name 
			String jarfilename=dis.readUTF();
				//save the jar file  
				File file = new File(SAVE_PATH+jarfilename);
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
					System.out.println("fileSize====>"+fileSize);
					//get file name 
					 String inputfilename=dis.readUTF();
						//save the  input file 
						 file = new File(SAVE_PATH+inputfilename);
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

                ProcessBuilder pb=new ProcessBuilder("java","-jar",jarfilename);
       		 pb.directory(new File(SAVE_PATH));
       		 File outputfile=new File(SAVE_PATH+"output.txt");//please use Final path to restore every out file
       		 pb.redirectOutput(outputfile);
       		pb.redirectErrorStream(true);
       		Process process =  pb.start();
            System.out.println("New process has been started...");

            process.waitFor();
            System.out.println("  process is over...");

              length=0;
             DataOutputStream dos =new DataOutputStream(socket.getOutputStream());
              FileInputStream fis=new FileInputStream(outputfile);
              fileSize=fis.available()+"";
              dos.writeUTF(fileSize);
              dos.flush();
              System.out.println("+++++++++=====>>>"+fileSize);
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
	public void init()
	{
		try {
			SSLContext ctx = SSLContext.getInstance("SSL");
			 KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");  
	         TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");  
	         try {
				KeyStore ks = KeyStore.getInstance("JKS");
		        KeyStore tks = KeyStore.getInstance("JKS");
		        ks.load(new FileInputStream("src/ssl/keystore.jks"),SERVER_KEY_STORE_PASSWORD.toCharArray());
		        tks.load(new FileInputStream("src/ssl/tserver.jks"),SERVER_KEY_STORE_PASSWORD.toCharArray());
		        kmf.init(ks, SERVER_KEY_STORE_PASSWORD.toCharArray());  
	            tmf.init(tks);  
	            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);  
	            serverSocket = (SSLServerSocket) ctx.getServerSocketFactory().createServerSocket(DEFAULT_PORT);  
	            serverSocket.setNeedClientAuth(true); 
			} 
	         catch (KeyStoreException e) {
				e.printStackTrace();
			} catch (CertificateException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
				e.printStackTrace();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			}  
			System.out.println(ctx);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}  
	}
	public static void main (String args[]) throws InterruptedException
	{
		Worker w=new Worker();
		w.init();
		Thread t=new Thread(w);
		t.start();
		t.join();
	}

	
}
