package exer_in_fight3;

import java.awt.*;

/**
 * 方格类
 */
public class ErsBox implements Cloneable {
	private boolean isColor;
	private Dimension size = new Dimension();

	// 方块类的构造函数
	public ErsBox(boolean isColor) {
		this.isColor = isColor;
	}
	// 判断方格是否是前景色展现，此处的是否显示颜色是在游戏面板中画方格的时候使用，初始化及ErsBlock类中进行设置
	public boolean isColorBox() {
		return isColor;
	}
	// 设置方格颜色
	public void setColor(boolean isColor) {
		this.isColor = isColor;
	}
	// 得到方格尺寸
	public Dimension getSize() {
		return size;
	}
	// 覆盖Object类的Object Clone(),实现克隆
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
