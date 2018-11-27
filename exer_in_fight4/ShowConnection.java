package exer_in_fight4;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

//在系统菜单中，加入菜单项：建立新游戏、连接游戏，弹出对话框

public class ShowConnection extends JFrame implements ActionListener {
	private JMenuBar bar = new JMenuBar();
	private JMenu mGame = new JMenu("游戏"), mControl = new JMenu("控制"),
			mInfo = new JMenu("帮助");
	private JMenuItem miNewGame = new JMenuItem("建立新游戏"),
			miConnectGame = new JMenuItem("连接游戏"),
			miExit = new JMenuItem("退出"),
			miControlKey = new JMenuItem("控制键设置"), miAuthor = new JMenuItem(
					"作者：handle-lxx"), miSoureInfo = new JMenuItem("版本：v1.0");
	private Socket socket;
	private ServerSocket server = null;
	private int port = 8081;// 端口号
	private String address = null;// ip地址，等待输入

	public ShowConnection(String title) {
		super(title);
		setSize(315, 382);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((scrSize.width - getWidth()) / 2,
				(scrSize.height - getHeight()) / 2);
		bar.add(mGame);
		bar.add(mControl);
		bar.add(mInfo);
		mGame.add(miNewGame);
		miNewGame.addActionListener(this);
		mGame.addSeparator();
		mGame.add(miConnectGame);
		miConnectGame.addActionListener(this);
		mGame.addSeparator();
		mGame.add(miExit);

		mControl.add(miControlKey);
		mInfo.add(miAuthor);
		mInfo.addSeparator();
		mInfo.add(miSoureInfo);

		setJMenuBar(bar);
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == miNewGame) {
			// 设置端口号
			GameServerSocketSet newgameset = new GameServerSocketSet(this);
			port = newgameset.getPortNum();
			server = newgameset.getServer();
		} else if (e.getSource() == miConnectGame) {
			// 连接服务器
			GameClienSocketSet newgameset = new GameClienSocketSet(this);
			port = newgameset.getPortNum();
			address = newgameset.getAddr();
		} else if (e.getSource() == miExit) {
			System.exit(0);
		}

	}
	public int getPort() {
		return port;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public ServerSocket getServer() {
		return server;
	}
	public void setServer(ServerSocket server) {
		this.server = server;
	}
	public static void main(String[] args) {
		new ShowConnection("show the connection");
	}
}
