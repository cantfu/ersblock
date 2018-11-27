package exer_in_fight3;

import java.awt.*;
import javax.swing.*;

/*用以图形方式展示自身图形的面板*/
public class ShowSelfPanel extends JPanel {
	// 数据信息
	private int[][] data = new int[22][13];
	//private ErsBox[][] boxes = new ErsBox[22][13];
	// 前景、背景色
	private Color backColor = Color.black, frontColor = Color.lightGray;
	// 方块大小
	private int boxWidth, boxHeight;

	// 构造函数
	ShowSelfPanel() {
//		for (int i = 0; i < boxes.length; i++) {
//			for (int j = 0; j < boxes[i].length; j++) {
//				boxes[i][j] = new ErsBox(false);
//			}
//		}
	}
	// 传递信息数组
	public void setData(int[][] data) {
		this.data = data;
		repaint();
	}
	// 绘制面板中的方块
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				Color color = (data[i][j] == 1) ? frontColor : backColor;
				g.setColor(color);
				g.fill3DRect(j * boxWidth, i * boxHeight, boxWidth, boxHeight, true);
			}
		}
	}
	// 根据窗口大小，调整方格大小
	public void fanning() {
		boxWidth = getSize().width / 13;
		boxHeight = getSize().height / 22;
	}


}
