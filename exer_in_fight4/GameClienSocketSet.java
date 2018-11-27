package exer_in_fight4;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class GameClienSocketSet extends JDialog implements ActionListener {
	private JLabel port = new JLabel("      端口号"), address = new JLabel(
			"     主机地址：");
	private JTextField text = new JTextField("01234"),
			addressText = new JTextField("127.0.0.1");
	private JButton ok = new JButton("完成");
	private JPanel panel = new JPanel(), panel1 = new JPanel();
	private int portNum = 8081;// 存储端口号
	private String addr = null;
	// private ServerSocket server = null;
	// private Socket socket = null;
	private ShowConnection menu;

	public GameClienSocketSet(final ShowConnection menu) {
		super(menu, "connect", true);
		this.menu = menu;
		setSize(400, 100);
		setResizable(false);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((scrSize.width - getWidth()) / 2,
				(scrSize.height - getHeight()) / 2);
		getContentPane().setLayout(new BorderLayout());//
		panel.setLayout(new GridLayout(1, 4));
		panel.add(address);
		panel.add(addressText);
		panel.add(port);
		panel.add(text);
		text.setText(new Integer(menu.getPort()).toString());
		portNum = menu.getPort();
		addr = menu.getAddress();
		panel1.setLayout(new FlowLayout());
		panel1.add(ok);
		getContentPane().add("North", panel);
		getContentPane().add("South", panel1);
		ok.addActionListener(this);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		setVisible(true);
	}
	public void actionPerformed(ActionEvent ae) {
		portNum = Integer.parseInt(text.getText());
		addr = addressText.getText();
		setVisible(false);
		try {
			Socket socketOut = new Socket(addr, portNum);
			System.out.println("建立端口成功。端口是：");
			System.out.println(portNum);
			System.out.println(addr);
			setVisible(false);
		} catch (IOException ie) {
			JOptionPane gameexit = new JOptionPane(ie.toString()
					+ "\nYou need to reset port-num.",
					JOptionPane.WARNING_MESSAGE);
			JDialog dialog = gameexit.createDialog(this, "");
			dialog.setVisible(true);
		}
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public int getPortNum() {
		return portNum;
	}
	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}
}
