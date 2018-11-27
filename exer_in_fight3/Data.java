package exer_in_fight3;

import java.io.*;

/*��дһ���࣬�������DATA������Ϣ������ȡ������������Ϸ��������ʾ
 * data����˫�����紫��Է������ݵĶ���
 * ʵ��Serializable�󣬾Ϳ���ʹ�����Ķ���д�뵽ObjectOutputStream�У��ҿ���ObjectInputStream
 * ��ȡ��
 */
public class Data implements Serializable {
	private int[][] a;// ��ά���飬ÿ��Ԫ������Ϸ����з���һһ��Ӧ
	private int fail;
	private int numLose = 0;

	// ���췽��
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
