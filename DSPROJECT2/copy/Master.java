package ssl;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

public class Master
{
	 private static final String DEFAULT_HOST  = "127.0.0.1";  
	 private static final int    DEFAULT_PORT                    = 7777; 
	 private static final String CLIENT_KEY_STORE_PASSWORD       = "654321";
	 private static final String CLIENT_TRUST_KEY_STORE_PASSWORD = "123456";  
	  private SSLSocket  sslSocket;  
	  
	  
	  
	  public static void main(String args[])
	  {
		  Master m=new Master();
		  m.init();
		  m.process();
		  
	  }
	  
	  public void process()
	  {
		  try {
			InputStream input = sslSocket.getInputStream();
	          OutputStream output = sslSocket.getOutputStream();  
	          BufferedInputStream bis = new BufferedInputStream(input);  
	           BufferedOutputStream bos = new BufferedOutputStream(output);  
	           bos.write("1234567890".getBytes());  
	            bos.flush();  
	            byte[] buffer = new byte[20];  
	            bis.read(buffer);  
	            System.out.println(new String(buffer));  
	            sslSocket.close();  

		} catch (Exception e) {
			e.printStackTrace();
		}  

	  }
	  public void init()
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