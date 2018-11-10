package ersblocks;

/*
 * һ������һ���߳�,���ɼ��̿������ƶ������䡢���ε�.
 * ע�����鲢δ�������Ҳδ�ı�block�����box��ɫ�����Ǹı���Ϸ����box����ɫ��ͨ������GameCanvas����ʾ
 */
/** �����ֱ࣬�Ӽ̳����߳���Thread */
public class ErsBlock extends Thread {
	// һ������վ��Ϊ4��4��
	public final static int BOXES_ROWS = 4, BOXES_COLS = 4;
	// �������仯ƽ�������ӣ�������󼸼����ٶ�����һ��
	public final static int LEVEL_FLATNESS_GENE = 3;
	// ����Ŀ�������֮�䣬��ÿ����һ�е�ʱ����Ϊ���٣����룩
	public final static int BETWEEN_LEVELS_DEGRESS_TIME = 50;
	// �������ʽ��ĿΪ7
	private final static int BLOCK_KIND_NUMBER = 7;
	// ÿ����ʽ�ķ����״̬����Ϊ4
	private final static int BLOCK_STATUS_NUMBER = 4;
	//��Ӧ��28��״̬
	public final static int[][] STYLES = { { 0x00F0, 0x2222, 0x00F0, 0x2222 }, // I��
			{ 0x0072, 0x0262, 0x0270, 0x0232 }, // T��
			{ 0x0223, 0x0074, 0x0622, 0x0170 }, // L��
			{ 0x0226, 0x0470, 0x0322, 0x0071 }, // J��
			{ 0x0063, 0x0264, 0x0063, 0x0264 }, // Z��
			{ 0x006C, 0x0462, 0x006C, 0x0462 }, // S��
			{ 0x0660, 0x0660, 0x0660, 0x0660 } // O��
	};
	private GameCanvas canvas;// ��Ϸ���
	private ErsBox[][] boxes = new ErsBox[BOXES_ROWS][BOXES_COLS];// һ��ErsBlockռ4x4
	private int style, y, x, level;
	private boolean pausing = false, moving = true;// ������ͣ��ֹͣ(�������ƶ�--�߳̽���)
	
	/**
	 * ���췽���������ض��Ŀ�
	 * 
	 * @param canvas ����
	 * @param style �����ʽ����Ӧ28�е�һ��
	 * @param y ��ʼλ�ã����Ͻ���canvas�е������꣬��λ������
	 * @param x ��ʼλ�ã����Ͻ���canvas�е������꣬��λ������
	 * @param level ��Ϸ�ȼ������ƿ�������ٶ�
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
		display();// ����Ϸ�������ʾ

	}
	// �߳����run()����������飬֪���鲻������
	public void run() {
		// �˴�����while()ѭ�����ƣ�����
		while (moving) {
			try{
				sleep(BETWEEN_LEVELS_DEGRESS_TIME * (5 - level + LEVEL_FLATNESS_GENE));
				// sleep(BETWEEN_LEVELS_DEGRESS_TIME*(10 -LEVEL_FLATNESS_GENE));

			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			//�����moving��ʾ�ڵȴ���100ms�䣬movingû���ı�
			if(!pausing){
				moving = (moveTo(y + 1, x) && moving);
			}
		}
	}
	// ����
	public void moveLeft() {
		moveTo(y, x - 1);
	}
	// ����
	public void moveRight() {
		moveTo(y, x + 1);
	}
	// ����
	public void moveDown() {
		moveTo(y + 1, x);
	}

	// �ƶ�
	/**
	 * ����ǰ���ƶ���newRow��newCol��ָ��λ��
	 * 
	 * @param newRow,Ŀ�ĵ�������
	 * @param newCol,Ŀ�ĵ�������
	 * @return boolean true-�ƶ��ɹ�,false-�ƶ�ʧ��
	 */
	private synchronized boolean moveTo(int newRow, int newCol) {
		// ������µ��в����ƶ���ֹͣ�ƶ��򷵻ؼ�
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
	 * �ж��ܷ񽫵�ǰ���ƶ���newRow��newCol��ָ��λ��
	 * 
	 * @param newRow,Ŀ�ĵ�������
	 * @param newCol,Ŀ�ĵ�������
	 * @return boolean true-���ƶ�,false-�����ƶ�
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
	
	// ����,����"��"
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
	 * ��ǰ���ܷ��ΪnewStyle����̬����Ҫ���Ǳ߽硢�����������ס�����ƶ������
	 * 
	 * @param newStyle, ϣ���ı�Ϊ����ʽ��28���е�һ��
	 * @return boolean,true-�ܸı�,false-���ܸı�
	 */
	private boolean isTurnable(int newStyle) {
		int key = 0x8000;
		earse();
		for(int i=0;i<boxes.length;i++){
			for(int j=0;j<boxes[i].length;j++){ 
				// ���4x4�ĸ����У�����̬�ķ����Ƿ��Ѵ��ڵĸ����赲
				if ((newStyle & key) != 0) {
					// ��⵱ǰ�����Ƿ���
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
	 * ����ǰ���ΪnewStyleָ������̬
	 * 
	 * @param newStyle int,28����̬��һ��
	 * @return boolean,true-�ı�ɹ�,false-�ı�ʧ��
	 */
	private boolean turnTo(int newStyle) {
		if (!isTurnable(newStyle) || !moving) {
			return false;
		}
		earse();
		int key = 0x8000;
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				// �ı�BlockΪnewStyle
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

	// ��ͣ�������,��Ӧpausing����Ϸ��ͣ
	public void pauseMove() {
		pausing = true;
	}
	// ����������䣬������Ϸ
	public void resumeMove() {
		pausing = false;
	}
	// ֹͣ��Ϸ
	public void stopMove() {
		moving = false;
	}
	// ����ǰ��ӻ����ж�Ӧλ��Ĩȥ���ı仭���ж�ӦBox��isColor�����´��ػ��������ܷ�ӳ��������ʧ��
	private void earse() {// private
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j].isColorBox()) {
					ErsBox box = canvas.getBox(i + y, j + x);// �˴���δ�ı��߳���ErsBlock����ɫ,���Ǹı���Ϸ������ɫ
					if (box == null)
						continue;
					box.setColor(false);
				}
			}
		}
	}
	// �õ�ǰ���ڻ�������ʾ�������ı仭���ж�ӦBox��isColor���ػ���������ʱ��Ӧ�����֣�
	private void display() {// private
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j].isColorBox()) {
					ErsBox box = canvas.getBox(i + y, j + x);// �˴���δ�ı��߳���ErsBlock����ɫ,���Ǹı���Ϸ������ɫ
					if (box == null)
						continue;
					box.setColor(true);// ��earse()�����Ĳ�ͬ��
				}
			}
		}
	}

}
