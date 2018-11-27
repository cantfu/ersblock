package exer_in_fight3;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/*
 * 2017年10月19日09:17:28
 *  改写、添加(删除分数)
 *  功能：
 */
/** 画布类:游戏主窗口 */
public class GameCanvas extends JPanel {
	// 默认的方块颜色为橘黄色，背景色为黑色
	private Color backColor = Color.black, frontColor = Color.orange;
	public int rows, cols;
	// score:得分 scoreForLevelUpdate:上一次升级后的积分
	private ErsBox[][] boxes;
	private int boxWidth = 20, boxHeight = 20;

	// 添加部分，用于存储canvas中的二维数组数据,由getCanvasData()得到
	private int[][] canvasData;


	/**
	 * 画布类的第一个版本的构造方法,设置行列
	 * 
	 * @param rows int,画布的行数
	 * @param cols int,画布的列数
	 */
	public GameCanvas(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		canvasData = new int[rows][cols];
		// 初始化游戏面板每个方格
		boxes = new ErsBox[rows][cols];
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				boxes[i][j] = new ErsBox(false);
			}
		}
		// 设置画布的边界：EtchedBorder
		setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(
				148, 145, 140)));

	}
	// 添加部分，获取二维数组的值
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
	 * 画布类的第一个版本的构造方法,设置行列、背景色、前景色
	 * 
	 * @param rows int,画布的行数
	 * @param cols int,画布的列数
	 * @param backColor Color,背景色
	 * @param frontColor Color,前景色
	 */
	public GameCanvas(int rows, int cols, Color backColor, Color frontColor) {
		this(rows, cols);
		this.backColor = backColor;
		this.frontColor = frontColor;
	}

	/**
	 * 得到某一行某一列的方格的引用
	 * 
	 * @param row int,行
	 * @param col int,列
	 * @return ErsBox,方格引用
	 */
	public ErsBox getBox(int row, int col) {
		if (row < 0 || row > boxes.length - 1 || col < 0
				|| col > boxes[0].length - 1) {
			return null;
		}
		return boxes[row][col];
	}
	/**
	 * 重写paintComponent()方法，绘制游戏面板
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(frontColor);
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				// 用前景色或背景色绘制游戏面板的方格
				g.setColor(boxes[i][j].isColorBox() ? frontColor : backColor);
				g.fill3DRect(j * boxWidth, i * boxHeight, boxWidth, boxHeight, true);// 注意绘制时i、j的顺序别反了
			}
		}
	}
	// 根据窗口大小调整方格尺寸
	public void fanning() {
		boxWidth = getSize().width / cols;
		boxHeight = getSize().height / rows;
	}
	// 重置画布，置积分为0
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
	// 几个get和set方法
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