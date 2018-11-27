package exer_in_fight3;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/*
 * 2017年10月20日13:42:31
 * 网络两端同时显示游戏进展(传输对象为Data对象)
 * 用到的类：ShowSelfPanel、Data
 * 获取本地IP:
 * InetAddress address = InetAddress.getLocalHost();
 String IP = address.getHostAddress();
 System.out.println(IP);
 */
/** 服务端 */
public class ShowServer extends JFrame{
	private ShowSelfPanel showState = new ShowSelfPanel();
	private JTextField text = new JTextField("等待网络连接......");
	private int[][] recData;
	private boolean playing = true;

	public ShowServer() {
		super("服务端，显示客户端的实时情况");
		setSize(315, 392);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((scrSize.width - getSize().width) / 2, (scrSize.height - getSize().height) / 2);
		setLayout(new BorderLayout());
		text.setEditable(false);
		add("North", text);
		add("Center", showState);
		this.setVisible(true);
		showState.fanning();//首先给他一个boxWidth,boxHeight
		// 窗口监听
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				playing = false;
				System.exit(0);
			}
		});
		Thread thread = new Thread(new Game());
		thread.start();
	}

	private class Game implements Runnable {
		public void run() {
			Data data = null;
			ServerSocket server=null;
			Socket socketIn = null;
			ObjectInputStream in = null;
			try {
				server = new ServerSocket(8081);
				if (server != null) {
					socketIn = server.accept();
					Thread.sleep(1000 * 2);
					text.setText("端口已经连接成功");
					in = new ObjectInputStream(socketIn.getInputStream());
				}
				while (playing) {
					data = (Data) in.readObject();
//					if(data!=null){//说明连接成功
//						System.out.println("okokokokok");
//					}
					recData = data.getA();
//					for(int i=0;i<10;i++){//数据传输成功
//						System.out.print(recData[21][i]);
//					}
					showState.setData(recData);
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				System.out.println("ok");
			} finally {
				if(in!=null){
					try {in.close();} 
					catch (IOException e) {e.printStackTrace();}
				}
				if (socketIn != null){
					try {
						socketIn.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(server!=null){
					try {
						server.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	public static void main(String[] args) {
		new ShowServer();
//		new ShowClient();
	}
}
