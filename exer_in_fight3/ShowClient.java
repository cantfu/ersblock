package exer_in_fight3;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/*
 * 2017��10��20��13:42:40
 * ��������ͬʱ��ʾ��Ϸ��չ(�������ΪData����)
 */
/** �ͻ��� */
public class ShowClient extends JFrame{
	public static int selfFail=0;
	private GameCanvas canvas;
	private ErsBlock block;
	private boolean playing = true;
	private JButton btPause = new JButton("��ͣ");
	
	ShowClient(){
		super("the client of ersblocksGame");
		setSize(315,392);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((scrSize.width - getSize().width) / 2, (scrSize.height - getSize().height) / 2);
		setLayout(new BorderLayout());
		canvas=new GameCanvas(22,13);
		add("North",btPause);
		add("Center",canvas);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we){
				playing=false;
				System.exit(0);
			}
		});
		//����������
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ce){
				canvas.fanning();
			}
		});
		//��ť����
		btPause.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (btPause.getText().equals(new String("��ͣ"))) {
					pauseGame();
				} else {
					resumeGame();
				}	
			}
		});
		setVisible(true);
		canvas.fanning();
		Thread thread=new Thread(new Game());
		thread.start();
	}
	//��Ϸ��ͣ
	public void pauseGame() {
		if (block != null)
			block.pauseMove();
		btPause.setText("����");
	}
	// ��Ϸ����
	public void resumeGame() {
		if (block != null)
			block.resumeMove();
		btPause.setText("��ͣ");
	}
	private class Game implements Runnable{

		@Override
		public void run() {
			// ������ɳ�ʼ�����״��λ��
			int col = (int) (Math.random() * (canvas.getCols() - 3)), style = ErsBlock.STYLES[(int) (Math
					.random() * 7)][(int) (Math.random() * 4)];
			//����Socket�������������
			Socket socketOut=null;
			ObjectOutputStream out=null;
			int numLose=0;
			String address="127.0.0.1";
			String address1="192.168.155.10";
			try {
				socketOut=new Socket(address,8081);
				out = new ObjectOutputStream(socketOut.getOutputStream());
	
			
			while (playing) {
				if (block != null) {
					if (block.isAlive()) {
						try {
							Thread.currentThread().sleep(100);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
						continue;
					}
				}
				if (isGameOver()) {
					canvas.reset();
				}
				block = new ErsBlock(canvas, style, -1, col, 1,out);
				block.start();
				// ���������һ�����״��λ��
				col = (int) (Math.random() * (canvas.getCols() - 3));
				style = ErsBlock.STYLES[(int) (Math.random() * 7)][(int) (Math.random() * 4)];
			}
			
			} 
			catch (UnknownHostException e) {e.printStackTrace();} 
			catch (IOException e) {e.printStackTrace();}
			finally{
			if(out!=null)
				try {
					out.close();
				} catch (IOException e) {e.printStackTrace();}
			if(socketOut!=null)
				try {
					socketOut.close();
				} catch (IOException e) {e.printStackTrace();}
			}
		
		}
		
		// �ж���Ϸ����
		public boolean isGameOver() {
			for (int i = 0; i < canvas.cols; i++) {
				ErsBox box = canvas.getBox(0, i);
				if (box.isColorBox())
					return true;
			}
			return false;
		}
		
	}
	public static void main(String[] args) {
		new ShowClient();
	}
}
