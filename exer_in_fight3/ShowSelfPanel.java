package exer_in_fight3;

import java.awt.*;
import javax.swing.*;

/*����ͼ�η�ʽչʾ����ͼ�ε����*/
public class ShowSelfPanel extends JPanel {
	// ������Ϣ
	private int[][] data = new int[22][13];
	//private ErsBox[][] boxes = new ErsBox[22][13];
	// ǰ��������ɫ
	private Color backColor = Color.black, frontColor = Color.lightGray;
	// �����С
	private int boxWidth, boxHeight;

	// ���캯��
	ShowSelfPanel() {
//		for (int i = 0; i < boxes.length; i++) {
//			for (int j = 0; j < boxes[i].length; j++) {
//				boxes[i][j] = new ErsBox(false);
//			}
//		}
	}
	// ������Ϣ����
	public void setData(int[][] data) {
		this.data = data;
		repaint();
	}
	// ��������еķ���
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
	// ���ݴ��ڴ�С�����������С
	public void fanning() {
		boxWidth = getSize().width / 13;
		boxHeight = getSize().height / 22;
	}


}
