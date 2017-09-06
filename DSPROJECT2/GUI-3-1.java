package ssl;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import GUI.ButtonThread;

public class GUI {
	static JFrame frame = new JFrame();
	static String[] columnWorkerName = { "Worker Name", "IP Address", "Port",
			"Status" };
	static DefaultTableModel workersNameModel = new DefaultTableModel(
			getWorkersFromText(), columnWorkerName);
	static JTable workersNameTable = new JTable(workersNameModel);
	static JTextField jobsNameT = new JTextField(10);
	static JTextField PathOfJarT = new JTextField(50);
	static JTextField PathOfInputT = new JTextField(50);
	static JTextField PathOfOutputT = new JTextField(50);
	static String[] columnJobs = { "Jobs Number", "Jar File", "Input File",
			"Worker Name", "Status", "Path of Output" };
	static DefaultTableModel jobsModel = new DefaultTableModel(null, columnJobs);
	static JTable jobsTable = new JTable(jobsModel);
	
	private static final String InitialFilePath = "/Users/zheng/Documents/workspace/MasterandWorker/src/ssl/init.txt";

	public static void main(String[] args) throws Exception {

		Container contentPane = frame.getContentPane();
		Box baseBox = Box.createVerticalBox();// 先产生水平排列方式的Box组件，当作基底容器(BaseBox)
		contentPane.add(baseBox);
		baseBox.add(Box.createVerticalStrut(10));

		/*
		 * Title of workersName table
		 */

		Box TitleOfWorkersNameBox = Box.createHorizontalBox();
		JLabel TitleOfWorkersNameText = new JLabel("Workers Information");
		TitleOfWorkersNameBox.add(TitleOfWorkersNameText);
		baseBox.add(TitleOfWorkersNameBox);

		/*
		 * The table of workers
		 */

		JPanel workersNamePanel = new JPanel();

		// 创建包含表格的滚动窗格

		// String[] columnWorkerName = { "Worker Name", "IP Address", "Port",
		// "Status" };
		// 创建表格
		// DefaultTableModel workersNameModel = new
		// DefaultTableModel(getWorkersFromText(), columnWorkerName);
		// JTable workersNameTable = new JTable(workersNameModel);
		JScrollPane scrollPaneWorker = new JScrollPane(workersNameTable);
		scrollPaneWorker
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// 定义 topPanel 的布局为 BoxLayout，BoxLayout 为垂直排列
		workersNamePanel.setLayout(new BoxLayout(workersNamePanel,
				BoxLayout.Y_AXIS));
		// 先加入一个不可见的 Strut，从而使 topPanel 对顶部留出一定的空间
		workersNamePanel.add(Box.createVerticalStrut(10));
		// 加入包含表格的滚动窗格
		workersNamePanel.add(scrollPaneWorker);
		// 再加入一个不可见的 Strut，从而使 topPanel 和 middlePanel 之间留出一定的空间
		baseBox.add(workersNamePanel);

		/*
		 * addNewWorker
		 */

		Box addNewWorkerBox = Box.createHorizontalBox();
		JButton addNewWokerbotton = new JButton("Add A New Woker");
		// addNewWokerbotton.setMaximumSize(new Dimension(150, 80));
		addNewWokerbotton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addWorkerMethod();
			}

			public void addWorkerMethod() {
				// TODO Auto-generated method stub
				JFrame ANWFrame = new JFrame();

				Container contentPane = ANWFrame.getContentPane();
				Box baseBox = Box.createVerticalBox();// 先产生水平排列方式的Box组件，当作基底容器(BaseBox)
				contentPane.add(baseBox);
				baseBox.add(Box.createVerticalStrut(20));

				JLabel ANWtitle = new JLabel(
						"Please fill the infromation of a new worker");
				baseBox.add(ANWtitle);
				baseBox.add(Box.createVerticalStrut(10));

				JLabel ANWnameL = new JLabel("Worker Name:");
				baseBox.add(ANWnameL);
				baseBox.add(Box.createVerticalStrut(10));

				JTextField ANWnameT = new JTextField(30);
				baseBox.add(ANWnameT);
				baseBox.add(Box.createVerticalStrut(10));

				JLabel ANWaddressL = new JLabel("Address:");
				baseBox.add(ANWaddressL);
				baseBox.add(Box.createVerticalStrut(10));

				JTextField ANWaddressT = new JTextField(30);
				baseBox.add(ANWaddressT);
				baseBox.add(Box.createVerticalStrut(10));

				JLabel ANWportL = new JLabel("Port:");
				baseBox.add(ANWportL);
				baseBox.add(Box.createVerticalStrut(10));

				JTextField ANWportT = new JTextField(30);
				baseBox.add(ANWportT);
				baseBox.add(Box.createVerticalStrut(10));

				JButton ANWbotton = new JButton("Add");
				baseBox.add(ANWbotton);
				baseBox.add(Box.createVerticalStrut(10));
				ANWbotton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ANWbottonMethod();
					}

					public void ANWbottonMethod() {
						String sName = ANWnameT.getText();
						String sAddress = ANWaddressT.getText();
						String sPort = ANWportT.getText();

						ArrayList<String> ANWallAL = new ArrayList<String>();
						ANWallAL.add(sName);
						ANWallAL.add(sAddress);
						ANWallAL.add(sPort);
						String[] ANWall = new String[ANWallAL.size()];
						ANWall = ANWallAL.toArray(ANWall);
						workersNameModel.addRow(ANWall);

						ArrayList<String> oneAddressAndPortAL = new ArrayList<String>();
						oneAddressAndPortAL.add(sAddress);
						oneAddressAndPortAL.add(sPort);
						String[] oneAddressAndPort = new String[oneAddressAndPortAL
								.size()];
						oneAddressAndPort = oneAddressAndPortAL
								.toArray(oneAddressAndPort);

						// 建立一个连接
						Master2 addWorkerSetConnnetion = new Master2();
						addWorkerSetConnnetion.connectWorker(oneAddressAndPort);

						ANWFrame.setVisible(false);
					}

				});
				ANWFrame.pack();
				ANWFrame.setVisible(true);
			}
		});
		addNewWorkerBox.add(addNewWokerbotton);
		addNewWorkerBox.add(Box.createVerticalStrut(10));
		baseBox.add(addNewWorkerBox);

		/*
		 * jobsName
		 */
		Box jobsNameBox = Box.createHorizontalBox();

		// 让框框变段一点 名字要那么长干什么
		JLabel jobsNameL = new JLabel("Job Name");
		jobsNameL.setPreferredSize(new Dimension(130, 30));
		jobsNameBox.add(jobsNameL);

		// JTextField jobsNameT = new JTextField(10);
		// baseBox.setLayout(null);
		// jobsNameT.setPreferredSize(new Dimension(130,30));
		// jobsNameBox.setLayout(new BoxLayout(frame,BoxLayout.X_AXIS));;
		// JTextField.setMaximunSize(new Dimension(90,30));
		jobsNameBox.add(jobsNameT);

		JLabel jobsNameL2 = new JLabel("   ");
		jobsNameL2.setPreferredSize(new Dimension(800, 30));
		jobsNameBox.add(jobsNameL2);

		baseBox.add(jobsNameBox);
		// frame.setLayout (true);

		/*
		 * jar
		 */

		Box jarBox = Box.createHorizontalBox();

		JLabel PathOfJarL = new JLabel("Path of jar file");
		PathOfJarL.setPreferredSize(new Dimension(130, 30));
		jarBox.add(PathOfJarL);

		// JTextField PathOfJarT = new JTextField(50);
		jarBox.add(PathOfJarT);

		JButton findJarButton = new JButton("Browse My Computer");
		jarBox.add(findJarButton);
		findJarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FJmethod();
			}

			public void FJmethod() {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setFileFilter(new FileNameExtensionFilter(
						"Jar File (*.jar)", "jar"));
				jfc.showDialog(new JLabel(), "选择");
				File file = jfc.getSelectedFile();
				String pathOfJars = new String();
				pathOfJars = file.getAbsolutePath();
				PathOfJarT.setText(pathOfJars);
			}
		});

		addNewWorkerBox.add(Box.createVerticalStrut(10));
		baseBox.add(jarBox);

		/*
		 * input
		 */

		Box inputBox = Box.createHorizontalBox();

		JLabel PathOfInputL = new JLabel("Path of input file");
		PathOfInputL.setPreferredSize(new Dimension(130, 30));
		inputBox.add(PathOfInputL);

		// JTextField PathOfInputT = new JTextField(50);
		inputBox.add(PathOfInputT);

		JButton FindInputButton = new JButton("Browse My Computer");
		inputBox.add(FindInputButton);
		FindInputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FImethod();
			}

			public void FImethod() {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showDialog(new JLabel(), "选择");
				File file = jfc.getSelectedFile();
				String pathOfInputs = new String();
				pathOfInputs = file.getAbsolutePath();
				PathOfInputT.setText(pathOfInputs);
			}
		});

		baseBox.add(inputBox);

		/*
		 * output
		 */

		Box outputBox = Box.createHorizontalBox();

		JLabel PathOfOutputL = new JLabel("Path of output file");
		PathOfOutputL.setPreferredSize(new Dimension(130, 30));
		outputBox.add(PathOfOutputL);

		// JTextField PathOfOutputT = new JTextField(50);
		outputBox.add(PathOfOutputT);

		JButton FindOutputButton = new JButton("Browse My Computer");
		outputBox.add(FindOutputButton);
		FindOutputButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FOmethod();
			}

			public void FOmethod() {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.showDialog(new JLabel(), "选择");
				File file = jfc.getSelectedFile();
				String pathOfOutputs = new String();
				pathOfOutputs = file.getAbsolutePath();
				PathOfOutputT.setText(pathOfOutputs);
			}
		});

		baseBox.add(outputBox);

		/*
		 * jobs
		 */
		JPanel jobsPanel = new JPanel();
		String[] columnJobs = { "Jobs Number", "Jar File", "Input File",
				"Worker Name", "Status", "Path of Output" };
		DefaultTableModel jobsModel = new DefaultTableModel(null, columnJobs);
		JTable jobsTable = new JTable(jobsModel);

		JScrollPane scrollPaneJobs = new JScrollPane(jobsTable);
		scrollPaneJobs
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jobsPanel.setLayout(new BoxLayout(jobsPanel, BoxLayout.Y_AXIS));
		jobsPanel.add(Box.createVerticalStrut(10));
		jobsPanel.add(scrollPaneJobs);
		// 粘到basebox的工作 在粘sent按钮之后执行，这样可以让jobs表格在sent下边

		/*
		 * sent
		 */

		Box sentBox = Box.createHorizontalBox();
		JButton sentbotton = new JButton("sent");
		sentbotton.addActionListener(new ActionListener() {

			// in default it should be -1

			public void actionPerformed(ActionEvent e) {
				ButtonThread bt = new ButtonThread();
				Thread t = new Thread(bt);
				t.start();
			}

		});

		sentBox.add(sentbotton);
		baseBox.add(sentBox);

		baseBox.add(jobsPanel);

		
		//总属性
		frame.setTitle("MASTER");
		frame.pack();
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		
		//初始化连接所有的表格中的master
		Master2 masterSetInital = new Master2();

		// getAllAddressAndPort 换成从表里取数据
		for (int i = 0; i < workersNameModel.getRowCount(); i++) {
			String stringAddress = (String) workersNameModel.getValueAt(i, 1);
			String stringPort = (String) workersNameModel.getValueAt(i, 2);
			ArrayList<String> oneRowIpAndPortAL = new ArrayList<String>();
			oneRowIpAndPortAL.add(stringAddress);
			oneRowIpAndPortAL.add(stringPort);
			String[] oneRowIpAndPort = new String[oneRowIpAndPortAL.size()];
			oneRowIpAndPort = oneRowIpAndPortAL.toArray(oneRowIpAndPort);
			boolean isConnected = false;
			try {
				isConnected = masterSetInital.connectWorker(oneRowIpAndPort);
			} catch (Exception e) {
			}
			System.out.println(oneRowIpAndPort[0]);

			if (isConnected) {
				workersNameTable.setValueAt("connected", i, 3);
			} else {

				workersNameTable.setValueAt("disconnected", i, 3);

			}

		}

	}

	// ==========从text中读取workers的信息==============
	private static String[][] getWorkersFromText() throws IOException {
		File rowDataFile = new File(InitialFilePath);
		BufferedReader reader = null;

		reader = new BufferedReader(new FileReader(rowDataFile));

		String temp = null;
		ArrayList<String[]> all = new ArrayList<String[]>();
		while ((temp = reader.readLine()) != null) {
			ArrayList<String> al = new ArrayList<String>();
			String[] strarray = temp.split(" +");
			for (int i = 0; i < strarray.length; i++) {
				al.add(strarray[i]);
			}
			String[] als = new String[al.size()];
			als = al.toArray(als);
			all.add(als);
		}
		String[][] alls = new String[all.size()][];
		alls = all.toArray(alls);
		// System.out.println(alls[0][2]);
		return alls;
	}

//	// ==========从getWorkerFromText方法中提取出仅含有Address和Port的数组==========
//
//	public static String[][] getAllAddressAndPort() throws IOException {
//		String[][] workersall = getWorkersFromText();
//		ArrayList<String[]> allRow = new ArrayList<String[]>();
//
//		for (int i = 0; i < workersall.length; i++) {
//			String stringAddress = workersall[i][1];
//			String stringPort = workersall[i][2];
//			ArrayList<String> oneRow = new ArrayList<String>();
//			oneRow.add(stringAddress);
//			oneRow.add(stringPort);
//			String[] oneRows = new String[oneRow.size()];
//			oneRows = oneRow.toArray(oneRows);
//			allRow.add(oneRows);
//		}
//		String[][] allRows = new String[allRow.size()][];
//		allRows = allRow.toArray(allRows);
//		return allRows;
//	}

	/*
	 * public static void SentMethod() { ArrayList<String> ip_list = new
	 * ArrayList<String>(); ArrayList<String> port_list=new ArrayList<String>();
	 * ArrayList<String> workername_list=new ArrayList<String>(); String
	 * tempAddress = null; String tempPort = null; String tempworkername=""; for
	 * (int i =0; i<workersNameModel.getRowCount(); i++){ tempworkername=
	 * (String) workersNameModel.getValueAt(i, 0); tempAddress = (String)
	 * workersNameModel.getValueAt(i, 1); tempPort = (String)
	 * workersNameModel.getValueAt(i, 2); System.out.println(tempAddress);
	 * System.out.println(tempPort); ip_list.add(tempAddress);
	 * port_list.add(tempPort); workername_list.add(tempworkername); }
	 * 
	 * 
	 * if(flag<ip_list.size()-1) { flag++;//First time running send method, the
	 * flag will be 1 // if the flag is less than the number of the workers, it
	 * should increase itself by 1. } else { flag=0; } String newJobName =
	 * jobsNameT.getText();
	 * 
	 * String newPathOfJar = PathOfJarT.getText(); File newFilejar = new
	 * File(newPathOfJar); String newJarName = newFilejar.getName();
	 * 
	 * String newPathOfInput = PathOfInputT.getText(); File newFileInput = new
	 * File(newPathOfInput); String newInputName = newFileInput.getName();
	 * 
	 * String newPathOfOutput = PathOfOutputT.getText();
	 * 
	 * if (PathOfJarT.getText().trim().equals("")){
	 * JOptionPane.showMessageDialog(null,"Please choose a jar file."); }else
	 * if(PathOfInputT.getText().trim().equals("")){
	 * JOptionPane.showMessageDialog(null,"Please choose a input file."); }else
	 * if(PathOfOutputT.getText().trim().equals("")){
	 * JOptionPane.showMessageDialog(null,"Please choose a output path.");
	 * }else{
	 * 
	 * ArrayList newJobAL = new ArrayList(); newJobAL.add(newJobName);
	 * newJobAL.add(newJarName); newJobAL.add(newInputName); newJobAL.add(null);
	 * newJobAL.add(null); newJobAL.add(newPathOfOutput); String[] newJob = new
	 * String[newJobAL.size()]; newJob=(String[]) newJobAL.toArray(newJob);
	 * jobsModel.addRow(newJob);
	 * 
	 * //发送两个文件和收文件的接收地址 给master //144.6.227.163:5000 //115.146.86.185:5000
	 * 
	 * System.out.println(workername_list.get(flag));
	 * jobsTable.setValueAt(workername_list.get(flag),
	 * jobsTable.getRowCount()-1,3);
	 * 
	 * Master2 sentAJob = new
	 * Master2(newPathOfJar,newPathOfInput,newPathOfOutput
	 * ,ip_list.get(flag),Integer.parseInt(port_list.get(flag))); Thread t=new
	 * Thread (sentAJob); t.start(); System.out.println("8*****");
	 * //sentAJob.process(newPathOfJar,newPathOfInput,newPathOfOutput);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * }
	 */

	static class ButtonThread implements Runnable {
		public void run() {
			SentMethod();
		}

		public void SentMethod() {
			int flag = -1;// flag is used to circle the worker to assign jobs

			ArrayList<String> ip_list = new ArrayList<String>();
			ArrayList<String> port_list = new ArrayList<String>();
			ArrayList<String> workername_list = new ArrayList<String>();
			String tempAddress = null;
			String tempPort = null;
			String tempworkername = "";

			for (int i = 0; i < workersNameModel.getRowCount(); i++) {
				tempworkername = (String) workersNameModel.getValueAt(i, 0);
				tempAddress = (String) workersNameModel.getValueAt(i, 1);
				tempPort = (String) workersNameModel.getValueAt(i, 2);
				System.out.println(tempAddress);
				System.out.println(tempPort);
				workername_list.add(tempworkername);
				ip_list.add(tempAddress);
				port_list.add(tempPort);
				
			}

			if (flag < ip_list.size() - 1) {
				flag++;// First time running send method, the flag will be 1
						// if the flag is less than the number of the workers,
						// it should increase itself by 1.
			} else {
				flag = 0;
			}
			String newJobName = jobsNameT.getText();

			String newPathOfJar = PathOfJarT.getText();
			File newFilejar = new File(newPathOfJar);
			String newJarName = newFilejar.getName();

			String newPathOfInput = PathOfInputT.getText();
			File newFileInput = new File(newPathOfInput);
			String newInputName = newFileInput.getName();

			String newPathOfOutput = PathOfOutputT.getText();

			if (PathOfJarT.getText().trim().equals("")) {
				JOptionPane
						.showMessageDialog(null, "Please choose a jar file.");
			} else if (PathOfInputT.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(null,
						"Please choose a input file.");
			} else if (PathOfOutputT.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(null,
						"Please choose a output path.");
			} else {

				ArrayList newJobAL = new ArrayList();
				newJobAL.add(newJobName);
				newJobAL.add(newJarName);
				newJobAL.add(newInputName);
				newJobAL.add(null);
				newJobAL.add(null);
				newJobAL.add(newPathOfOutput);
				String[] newJob = new String[newJobAL.size()];
				newJob = (String[]) newJobAL.toArray(newJob);
				jobsModel.addRow(newJob);

				// 发送两个文件和收文件的接收地址 给master
				// 144.6.227.163:5000
				// 115.146.86.185:5000

				//读取已经选了的worker的name 写到表格中
				System.out.println(workername_list.get(flag));
				jobsTable.setValueAt(workername_list.get(flag),
						jobsTable.getRowCount() - 1, 3);

				//开启传输文件的线程
				Master2 sentAJob = new Master2(newPathOfJar, newPathOfInput,
						newPathOfOutput, ip_list.get(flag),
						Integer.parseInt(port_list.get(flag)));
				Thread t = new Thread(sentAJob);
				t.start();
				System.out.println("8*****");
				// sentAJob.process(newPathOfJar,newPathOfInput,newPathOfOutput);
				
				//监测jobs的状态 并且返回到表格中
				int mark=1;
				while(true){
					
					jobsTable.setValueAt(mark, jobsTable.getRowCount() - 1, 4);
					
					if (mark==3) {
						break;	
					}
					mark++;
				}
				
//				while (true) {
//					jobsTable.setValueAt(getStatus(sentAJob),
//							jobsTable.getRowCount() - 1, 4);
//					if (jobsTable.getValueAt(jobsTable.getRowCount() - 1, 4) == "finished") {
//						break;
//					}
//					if (jobsTable.getValueAt(jobsTable.getRowCount() - 1, 4) == "failed") {
//						break;
//					}
//				}
				
				
				// 从master获取jobs的状态
//				private Object getStatus(Master2 m) {
	//
//					// TODO Auto-generated method stub
//					String statusOfJobs = null;
//					while (true) {
//						statusOfJobs = m.status;
//						return statusOfJobs;
	//
//						if (statusOfJobs == "finished") {
//							break;
//						}
//						if (statusOfJobs == "failed") {
//							break;
//						}
//					}
//				}

			}
		}
	}
}