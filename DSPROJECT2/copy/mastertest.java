package ssl;

import java.io.File;
import java.util.Date;

public class mastertest {

	public static void main(String[] args) {
		/*Master2 m=new Master2();
		 Thread t = new Thread(m);
		 t.start();
			Master2 m2=new Master2();
			String a[]={"130.220.210.48","4444"};
			m.connectWorker(a);*/
		int possibleport[]=new int[6001];
		for(int i=0;i<5000;i++)
		{
			possibleport[i]=i+4000;
		}
		int aa=(int) (Math.random()*10000);
		System.out.println(aa);
 
	}

	
}
