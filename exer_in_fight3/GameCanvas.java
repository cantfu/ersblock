package exer_in_fight3;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/*
 * 2017��10��19��09:17:28
 *  ��д�����(ɾ������)
 *  ���ܣ�
 */
/** ������:��Ϸ������ */
public class GameCanvas extends JPanel {
	// Ĭ�ϵķ�����ɫΪ�ٻ�ɫ������ɫΪ��ɫ
	private Color backColor = Color.black, frontColor = Color.orange;
	public int rows, cols;
	// score:�÷� scoreForLevelUpdate:��һ��������Ļ���
	private ErsBox[][] boxes;
	private int boxWidth = 20, boxHeight = 20;

	// ��Ӳ��֣����ڴ洢canvas�еĶ�ά��������,��getCanvasData()�õ�
	private int[][] canvasData;


	/**
	 * ������ĵ�һ���汾�Ĺ��췽��,��������
	 * 
	 * @param rows int,����������
	 * @param cols int,����������
	 */
	public GameCanvas(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		canvasData = new int[rows][cols];
		// ��ʼ����Ϸ���ÿ������
		boxes = new ErsBox[rows][cols];
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				boxes[i][j] = new ErsBox(false);
			}
		}
		// ���û����ı߽磺EtchedBorder
		setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(
				148, 145, 140)));

	}
	// ��Ӳ��֣���ȡ��ά�����ֵ
	public int[][] getCanvasData() {
		try {
			for (int i = 0; i < boxes.length; i++) {
				for (int j = 0; j < boxes[i].length; j++) {
					// if (boxes[i][j].isColorBox()) {
					// canvasData[i][j] = 1;
					// } else {
					// canvasData[i][j] = 0;
					// }
					canvasData[i][j] = boxes[i][j].isColorBox() ? 1 : 0;
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return canvasData;
	}
	/**
	 * ������ĵ�һ���汾�Ĺ��췽��,�������С�����ɫ��ǰ��ɫ
	 * 
	 * @param rows int,����������
	 * @param cols int,����������
	 * @param backColor Color,����ɫ
	 * @param frontColor Color,ǰ��ɫ
	 */
	public GameCanvas(int rows, int cols, Color backColor, Color frontColor) {
		this(rows, cols);
		this.backColor = backColor;
		this.frontColor = frontColor;
	}

	/**
	 * �õ�ĳһ��ĳһ�еķ��������
	 * 
	 * @param row int,��
	 * @param col int,��
	 * @return ErsBox,��������
	 */
	public ErsBox getBox(int row, int col) {
		if (row < 0 || row > boxes.length - 1 || col < 0
				|| col > boxes[0].length - 1) {
			return null;
		}
		return boxes[row][col];
	}
	/**
	 * ��дpaintComponent()������������Ϸ���
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(frontColor);
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				// ��ǰ��ɫ�򱳾�ɫ������Ϸ���ķ���
				g.setColor(boxes[i][j].isColorBox() ? frontColor : backColor);
				g.fill3DRect(j * boxWidth, i * boxHeight, boxWidth, boxHeight, true);// ע�����ʱi��j��˳�����
			}
		}
	}
	// ���ݴ��ڴ�С��������ߴ�
	public void fanning() {
		boxWidth = getSize().width / cols;
		boxHeight = getSize().height / rows;
	}
	// ���û������û���Ϊ0
	public void reset() {
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				boxes[i][j].setColor(false);
			}
		}
		repaint();
	}
	public synchronized void removeLine(int row) {
		for (int i = row; i > 0; i--) {
			for (int j = 0; j < cols; j++) {
				boxes[i][j] = (ErsBox) boxes[i - 1][j].clone();
			}
		}
		repaint();
	}
	// ����get��set����
	public Color getBackgroundColor() {
		return backColor;
	}
	public void setBackgroundColor(Color backColor) {
		this.backColor = backColor;
	}
	public Color getBlockColor() {
		return frontColor;
	}
	public void setBlockColor(Color frontColor) {
		this.frontColor = frontColor;
	}
	public int getRows() {
		return rows;
	}
	public int getCols() {
		return cols;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public void setCols(int cols) {
		this.cols = cols;
	}
}