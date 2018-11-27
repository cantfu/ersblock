package exer_in_fight3;

import java.io.*;

/*编写一个类，将传输的DATA对象信息单独抽取出来，并在游戏界面中显示
 * data类是双人网络传输对方的数据的对象
 * 实现Serializable后，就可以使这个类的对象写入到ObjectOutputStream中，且可用ObjectInputStream
 * 读取。
 */
public class Data implements Serializable {
	private int[][] a;// 二维数组，每个元素与游戏面板中方格一一对应
	private int fail;
	private int numLose = 0;

	// 构造方法
	public Data(int[][] b, int fail, int numLose) {
		a = new int[b.length][b[0].length];
		// a=b
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[i].length; j++) {
				a[i][j] = b[i][j];
			}
		}
		this.fail = fail;
		this.numLose = numLose;
	}
	public int[][] getA() {
		return a;
	}
	public void setA(int[][] a) {
		this.a = a;
	}
	public int getFail() {
		return fail;
	}
	public void setFail(int fail) {
		this.fail = fail;
	}
	public int getNumLose() {
		return numLose;
	}
	public void setNumLose(int numLose) {
		this.numLose = numLose;
	}
}
