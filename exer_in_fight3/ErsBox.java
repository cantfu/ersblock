package exer_in_fight3;

import java.awt.*;

/**
 * ������
 */
public class ErsBox implements Cloneable {
	private boolean isColor;
	private Dimension size = new Dimension();

	// ������Ĺ��캯��
	public ErsBox(boolean isColor) {
		this.isColor = isColor;
	}
	// �жϷ����Ƿ���ǰ��ɫչ�֣��˴����Ƿ���ʾ��ɫ������Ϸ����л������ʱ��ʹ�ã���ʼ����ErsBlock���н�������
	public boolean isColorBox() {
		return isColor;
	}
	// ���÷�����ɫ
	public void setColor(boolean isColor) {
		this.isColor = isColor;
	}
	// �õ�����ߴ�
	public Dimension getSize() {
		return size;
	}
	// ����Object���Object Clone(),ʵ�ֿ�¡
	public Object clone(){
		Object cloned=null;
		try{
			cloned=super.clone();
		}
 catch (Exception ex) {
			ex.printStackTrace();
		}
		return cloned;
	}
}
