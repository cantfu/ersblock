package ersblocks;

/*
 * 一个方块一个线程,可由键盘控制器移动、下落、变形等.
 * 注：方块并未被绘出，也未改变block本身的box颜色，而是改变游戏面板的box的颜色，通过绘制GameCanvas来显示
 */
/** 方块类，直接继承自线程类Thread */
public class ErsBlock extends Thread {
	// 一个方块站格为4行4列
	public final static int BOXES_ROWS = 4, BOXES_COLS = 4;
	// 让升级变化平缓的因子，避免最后几级的速度相差近一倍
	public final static int LEVEL_FLATNESS_GENE = 3;
	// 相近的快慢两级之间，块每下落一行的时间差别为多少（毫秒）
	public final static int BETWEEN_LEVELS_DEGRESS_TIME = 50;
	// 方块的样式数目为7
	private final static int BLOCK_KIND_NUMBER = 7;
	// 每个样式的方块的状态种类为4
	private final static int BLOCK_STATUS_NUMBER = 4;
	//对应的28种状态
	public final static int[][] STYLES = { { 0x00F0, 0x2222, 0x00F0, 0x2222 }, // I型
			{ 0x0072, 0x0262, 0x0270, 0x0232 }, // T型
			{ 0x0223, 0x0074, 0x0622, 0x0170 }, // L型
			{ 0x0226, 0x0470, 0x0322, 0x0071 }, // J型
			{ 0x0063, 0x0264, 0x0063, 0x0264 }, // Z型
			{ 0x006C, 0x0462, 0x006C, 0x0462 }, // S型
			{ 0x0660, 0x0660, 0x0660, 0x0660 } // O型
	};
	private GameCanvas canvas;// 游戏面板
	private ErsBox[][] boxes = new ErsBox[BOXES_ROWS][BOXES_COLS];// 一个ErsBlock占4x4
	private int style, y, x, level;
	private boolean pausing = false, moving = true;// 控制暂停、停止(含不可移动--线程结束)
	
	/**
	 * 构造方法，产生特定的块
	 * 
	 * @param canvas 画板
	 * @param style 块的样式，对应28中的一个
	 * @param y 起始位置，左上角在canvas中的行坐标，单位方格数
	 * @param x 起始位置，左上角在canvas中的列坐标，单位方格数
	 * @param level 游戏等级，控制块的下落速度
	 */
	public ErsBlock(GameCanvas canvas, int style, int y, int x, int level) {
		this.canvas = canvas;
		this.style = style;
		this.y = y;
		this.x = x;
		this.level = level;
		int key = 0x8000;
		for(int i=0;i<boxes.length;i++){
			for(int j=0;j<boxes[i].length;j++){
				boolean iscolor = (style & key) != 0;
				boxes[i][j]=new ErsBox(iscolor);
				key >>= 1;
			}
		}
		display();// 在游戏面板中显示

	}
	// 线程类的run()方法，下落块，知道块不能下落
	public void run() {
		// 此处利用while()循环控制，接收
		while (moving) {
			try{
				sleep(BETWEEN_LEVELS_DEGRESS_TIME * (5 - level + LEVEL_FLATNESS_GENE));
				// sleep(BETWEEN_LEVELS_DEGRESS_TIME*(10 -LEVEL_FLATNESS_GENE));

			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			//后面的moving表示在等待的100ms间，moving没被改变
			if(!pausing){
				moving = (moveTo(y + 1, x) && moving);
			}
		}
	}
	// 左移
	public void moveLeft() {
		moveTo(y, x - 1);
	}
	// 右移
	public void moveRight() {
		moveTo(y, x + 1);
	}
	// 下移
	public void moveDown() {
		moveTo(y + 1, x);
	}

	// 移动
	/**
	 * 将当前块移动到newRow，newCol所指定位置
	 * 
	 * @param newRow,目的地所在行
	 * @param newCol,目的地所在列
	 * @return boolean true-移动成功,false-移动失败
	 */
	private synchronized boolean moveTo(int newRow, int newCol) {
		// 如果在新的行不能移动或停止移动则返回假
		if (!isMoveable(newRow, newCol)||!moving) {
			return false;
		}
		earse();
		y=newRow;
		x=newCol;
		display();
		canvas.repaint();
		return true;
	}
	/**
	 * 判断能否将当前快移动到newRow，newCol所指定位置
	 * 
	 * @param newRow,目的地所在行
	 * @param newCol,目的地所在列
	 * @return boolean true-能移动,false-不能移动
	 */
	private boolean isMoveable(int newRow, int newCol) {
		earse();
		for(int i=0;i<boxes.length;i++){
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j].isColorBox()) {
					ErsBox box = canvas.getBox(newRow + i, newCol + j);
					if((box==null)||(box.isColorBox())){ 
						display();
						return false;
					}
				}
			}
		}
		display();
		return true;
	}
	
	// 变形,按键"上"
	public void turnNext() {
		for (int i = 0; i < BLOCK_KIND_NUMBER; i++) {
			for (int j = 0; j < BLOCK_STATUS_NUMBER; j++) {
				if (STYLES[i][j] == style) {
					int newStyle = STYLES[i][(j + 1) % 4];
					turnTo(newStyle);
					return;
				}
			}
		}
	}
	/**
	 * 当前块能否变为newStyle的形态，主要考虑边界、被其他块而挡住不能移动的情况
	 * 
	 * @param newStyle, 希望改变为的样式，28个中的一个
	 * @return boolean,true-能改变,false-不能改变
	 */
	private boolean isTurnable(int newStyle) {
		int key = 0x8000;
		earse();
		for(int i=0;i<boxes.length;i++){
			for(int j=0;j<boxes[i].length;j++){ 
				// 检查4x4的格子中，新形态的方块是否被已存在的格子阻挡
				if ((newStyle & key) != 0) {
					// 检测当前格子是否在
					ErsBox box = canvas.getBox(y + i, x + j);
					if (box.isColorBox() || box == null) {
						display();
						return false;
					}
				}
				key >>= 1;
			}
		}
		display();
		return true;
	}

	/**
	 * 将当前块变为newStyle指定的形态
	 * 
	 * @param newStyle int,28种形态的一个
	 * @return boolean,true-改变成功,false-改变失败
	 */
	private boolean turnTo(int newStyle) {
		if (!isTurnable(newStyle) || !moving) {
			return false;
		}
		earse();
		int key = 0x8000;
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				// 改变Block为newStyle
				boolean isColor = (key & newStyle) != 0;
				boxes[i][j].setColor(isColor);
				key >>= 1;
			}
		}
		style = newStyle;
		display();
		canvas.repaint();
		return true;
	}

	// 暂停块的下落,对应pausing，游戏暂停
	public void pauseMove() {
		pausing = true;
	}
	// 继续块的下落，继续游戏
	public void resumeMove() {
		pausing = false;
	}
	// 停止游戏
	public void stopMove() {
		moving = false;
	}
	// 将当前块从画布中对应位置抹去，改变画布中对应Box的isColor，等下次重画画布才能反映出来（消失）
	private void earse() {// private
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j].isColorBox()) {
					ErsBox box = canvas.getBox(i + y, j + x);// 此处并未改变线程类ErsBlock的颜色,而是改变游戏面板的颜色
					if (box == null)
						continue;
					box.setColor(false);
				}
			}
		}
	}
	// 让当前块在画布中显示出来，改变画布中对应Box的isColor，重画画布窗口时反应（出现）
	private void display() {// private
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j].isColorBox()) {
					ErsBox box = canvas.getBox(i + y, j + x);// 此处并未改变线程类ErsBlock的颜色,而是改变游戏面板的颜色
					if (box == null)
						continue;
					box.setColor(true);// 与earse()方法的不同处
				}
			}
		}
	}

}
