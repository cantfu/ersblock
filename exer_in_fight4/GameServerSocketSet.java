package exer_in_fight4;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class GameServerSocketSet extends JDialog implements ActionListener {
	private JLabel port = new JLabel("端口号");
	private JTextField text = new JTextField("01234");
	private JButton ok = new JButton("完成");
	private JTextArea tf = new JTextArea("请输入端口号（注意：请选择大于1024的数字）");
	private JPanel panel = new JPanel();
	private int portNum = 8081;
	private ServerSocket server = null;
	private Socket socket = null;
	private ShowConnection menu;

	public GameServerSocketSet(final ShowConnection menu) {
		super(menu, "set the PORT of socket", true);
		this.menu = menu;
		getContentPane().setLayout(new BorderLayout());
		setSize(350, 90);
		setResizable(false);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((scrSize.width - getWidth()) / 2,
				(scrSize.height - getHeight()) / 2);
		getContentPane().add("North", tf);
		getContentPane().add("South", panel);
		panel.setLayout(new GridLayout(1, 3, 10, 10));
		panel.add(port);
		panel.add(text);
		text.setText(new Integer(menu.getPort()).toString());
		portNum = menu.getPort();
		ok.requestFocus();
		panel.add(ok);
		ok.addActionListener(this);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		portNum = Integer.parseInt(text.getText());
		setVisible(false);
		try {
			server = new ServerSocket(portNum);
			System.out.println("建立端口成功。端口是：");
			System.out.println(portNum);
			setVisible(false);
		} catch (IOException ie) {
			JOptionPane gameexit = new JOptionPane(ie.toString()
					+ "\nYou need to reset port-num.",
					JOptionPane.WARNING_MESSAGE);
			JDialog dialog = gameexit.createDialog(this, "");
			dialog.setVisible(true);
		}
	}
	public int getPortNum() {
		return portNum;
	}
	public Socket getSocket() {
		return socket;
	}
	public ServerSocket getServer() {
		return server;
	}
}
