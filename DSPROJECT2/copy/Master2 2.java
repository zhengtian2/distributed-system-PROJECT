package ssl;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class Master2 implements Runnable
{
	 private static final String DEFAULT_HOST  = "127.0.0.1";  
	 private static final int    DEFAULT_PORT                    = 7777; 
	 private static final String CLIENT_KEY_STORE_PASSWORD       = "654321";
	 private static final String CLIENT_TRUST_KEY_STORE_PASSWORD = "123456";  
	  private SSLSocket  sslSocket;  
  
	  public static void main(String args[]) throws InterruptedException
	  {
		  Master2 m=new Master2();
		  Thread t= new Thread(m);
		  t.start();
		  t.join();
		  
		  
	  }
	  public void run()
	  {
		  init();
		  process();
	  }
	  
	  public void process()
	  {
		  int length=0;
		  try {
	          OutputStream output = sslSocket.getOutputStream();  
	          DataOutputStream dos=new DataOutputStream(output);
	          DataInputStream dis=new DataInputStream(sslSocket.getInputStream());
	          //****transfer jar file
	           File file=new File("/Users/zheng/Desktop/DS PROJECT2/SampleJob/wordcount.jar");//jar file get the path from GUI
	           FileInputStream fIn = new FileInputStream(file);         
		          String fileSize;
		          fileSize=fIn.available()+"";
					System.out.println("fileSize====>"+fileSize);

		          dos.writeUTF(fileSize);
		          dos.flush();
		          dos.writeUTF(file.getName());
		          dos.flush();
	           byte[] sendBytes = new byte[1024];        
	           while ((length = fIn.read(sendBytes, 0, sendBytes.length)) > 0) {
	        	   System.out.println(length);
                   dos.write(sendBytes, 0, length);
                  // dos.flush();
               }
	           
	           //transfer input file
	         file=new File("/Users/zheng/Desktop/DS PROJECT2/SampleJob/sample-input.txt");//get from GUI
	          fIn = new FileInputStream(file);         
	           fileSize=null;
	          fileSize=fIn.available()+"";
				System.out.println("fileSizeOfInputfile====>"+fileSize);

	          dos.writeUTF(fileSize);
	          dos.flush();
	          dos.writeUTF(file.getName());
	          dos.flush();
          while ((length = fIn.read(sendBytes, 0, sendBytes.length)) > 0) {
       	  // System.out.println(length);
              dos.write(sendBytes, 0, length);
          }
	         
	         
	            dis=new DataInputStream(sslSocket.getInputStream());
	           fileSize=dis.readUTF();
	           File fileResult =new File("/users/zheng/receiveOput.txt");//the path should be specified by the user from the GUI
	           System.out.println("输出文件了");
	           FileOutputStream fos =new FileOutputStream(fileResult);
	           byte[] result=new byte[1024];
	           length=0;
	          int sizeCounter=0;
	           while((length=dis.read(result,0,result.length))>0)
	           {	          sizeCounter+=length;

	        	   fos.write(result, 0, length);
	        	   fos.flush();
	        	   if(fileSize.equals(Integer.toString(sizeCounter)))
	        	   {
	        		   break;
	        	   }
	           }
	           fos.close();
	           dis.close();
	           fIn.close();
	           output.close();
	           sslSocket.close();
	           
System.out.println("master  have received the output");

		} catch (Exception e) {
			e.printStackTrace();
		}  

	  }
	  public  void init()
	  {
		  try {
			SSLContext ctx = SSLContext.getInstance("SSL");
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");  
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");  
  
            KeyStore ks = KeyStore.getInstance("JKS");  
            KeyStore tks = KeyStore.getInstance("JKS");  
            ks.load(new FileInputStream("src/ssl/clientStore.jks"), CLIENT_KEY_STORE_PASSWORD.toCharArray());  
            tks.load(new FileInputStream("src/ssl/tclient.jks"), CLIENT_TRUST_KEY_STORE_PASSWORD.toCharArray());  
            kmf.init(ks, CLIENT_KEY_STORE_PASSWORD.toCharArray());  
            tmf.init(tks);  
            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);  
            sslSocket = (SSLSocket) ctx.getSocketFactory().createSocket(DEFAULT_HOST, DEFAULT_PORT);  
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	  }
	
}