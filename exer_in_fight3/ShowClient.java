package exer_in_fight3;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/*
 * 2017年10月20日13:42:40
 * 网络两端同时显示游戏进展(传输对象为Data对象)
 */
/** 客户端 */
public class ShowClient extends JFrame{
	public static int selfFail=0;
	private GameCanvas canvas;
	private ErsBlock block;
	private boolean playing = true;
	private JButton btPause = new JButton("暂停");
	
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
		//构件适配器
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ce){
				canvas.fanning();
			}
		});
		//按钮监听
		btPause.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (btPause.getText().equals(new String("暂停"))) {
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
	//游戏暂停
	public void pauseGame() {
		if (block != null)
			block.pauseMove();
		btPause.setText("继续");
	}
	// 游戏继续
	public void resumeGame() {
		if (block != null)
			block.resumeMove();
		btPause.setText("暂停");
	}
	private class Game implements Runnable{

		@Override
		public void run() {
			// 随机生成初始块的形状、位置
			int col = (int) (Math.random() * (canvas.getCols() - 3)), style = ErsBlock.STYLES[(int) (Math
					.random() * 7)][(int) (Math.random() * 4)];
			//创建Socket对象，输出流对象
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
				// 随机生成下一块的形状、位置
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
		
		// 判断游戏结束
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
