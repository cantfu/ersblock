package exer_in_fight3;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/*
 * 2017��10��20��13:42:31
 * ��������ͬʱ��ʾ��Ϸ��չ(�������ΪData����)
 * �õ����ࣺShowSelfPanel��Data
 * ��ȡ����IP:
 * InetAddress address = InetAddress.getLocalHost();
 String IP = address.getHostAddress();
 System.out.println(IP);
 */
/** ����� */
public class ShowServer extends JFrame{
	private ShowSelfPanel showState = new ShowSelfPanel();
	private JTextField text = new JTextField("�ȴ���������......");
	private int[][] recData;
	private boolean playing = true;

	public ShowServer() {
		super("����ˣ���ʾ�ͻ��˵�ʵʱ���");
		setSize(315, 392);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((scrSize.width - getSize().width) / 2, (scrSize.height - getSize().height) / 2);
		setLayout(new BorderLayout());
		text.setEditable(false);
		add("North", text);
		add("Center", showState);
		this.setVisible(true);
		showState.fanning();//���ȸ���һ��boxWidth,boxHeight
		// ���ڼ���
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
					text.setText("�˿��Ѿ����ӳɹ�");
					in = new ObjectInputStream(socketIn.getInputStream());
				}
				while (playing) {
					data = (Data) in.readObject();
//					if(data!=null){//˵�����ӳɹ�
//						System.out.println("okokokokok");
//					}
					recData = data.getA();
//					for(int i=0;i<10;i++){//���ݴ���ɹ�
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
