package ersblocks;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * ����˹������Ϸ������,�̳���JFrame������ȫ��.
 * �������Ϊһ���˵����������ڣ�һ��Canvas����һ����ControlPanel��֮���ǲ˵�.
 * 
 */
public class ErsBlocksGame extends JFrame {
	// ÿ����һ�м�100��
	public final static int PER_LINE_SCORE = 100;
	// ��ҵ���2000�־ͽ���Ϸ�Ѷ�level��1
	public final static int PER_LEVEL_SCORE = PER_LINE_SCORE * 20;
	// �����Ϊ10��Ĭ��Ϊ3:
	public final static int MAX_LEVEL=10;
	public final static int DEFAULT_LEVEL = 3;

	private GameCanvas canvas;// ������
	private ErsBlock block;
	private ControlPanel ctrlPanel;// ������
	private boolean playing = false;// ��ǰ��Ϸ�Ƿ����

	private JMenuBar bar = new JMenuBar();
	// �˵���bar����4���˵�
	private JMenu mGame = new JMenu("��Ϸ"), mControl = new JMenu("����"), mWindowStyle = new JMenu(
			"���ڷ��"),
			mInfo = new JMenu("����");
	// �˵���
	private JMenuItem miNewGame = new JMenuItem("����Ϸ"), miSetBlockColor = new JMenuItem("���÷�����ɫ"),
			miSetBackColor = new JMenuItem("���ñ�����ɫ"), miTurnHarder = new JMenuItem("�����Ѷ�"),
			miTurnEasier = new JMenuItem("�����Ѷ�"), miExit = new JMenuItem("�˳�"),
			miPlay = new JMenuItem("��ʼ"), miPause = new JMenuItem("��ͣ"), miResume = new JMenuItem(
					"����"), miStop = new JMenuItem("ֹͣ"), miAuthor = new JMenuItem("���ߣ�HANDLE"),
			miSourceInfo = new JMenuItem("�汾: 1.0");
	// ���ô��ڷ��˵�
	private JCheckBoxMenuItem miAsWindows = new JCheckBoxMenuItem("Winodws"),
			miAsMotif = new JCheckBoxMenuItem("Motif"), miAsMetal = new JCheckBoxMenuItem("Metal",
					false);

	// ����Ϸ��Ĺ��췽��
	public ErsBlocksGame(String title){
		super(title);
		setSize(315, 392);

		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		// ������Ļ����
		setLocation((scrSize.width - getSize().width) / 2, (scrSize.height - getSize().height) / 2);
		// �����˵�
		creatMenu();
		Container container = getContentPane();
		// ���õĲ���ΪBorderLayout
		// ���ֵ�ˮƽ����֮����6�����صľ���
		container.setLayout(new BorderLayout(6, 0));
		canvas = new GameCanvas(20, 12);// ����20x12
		ctrlPanel = new ControlPanel(this);// �������
		// ��Ϸ�����Ϳ������֮�����Ұڷ�
		container.add(canvas, BorderLayout.CENTER);
		container.add(ctrlPanel, BorderLayout.EAST);
		// ���Ӵ��ڼ�����
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				// stopGame();
				System.exit(0);
			}
		});
		// ������������
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ce) {
				canvas.fanning();
			}
		});
		setVisible(true);
		canvas.fanning();
	}

	private void creatMenu() {
		bar.add(mGame); // ����˵�
		bar.add(mControl);
		bar.add(mWindowStyle);
		bar.add(mInfo);
		mGame.add(miNewGame);// ����˵���
		mGame.addSeparator();// �ָ���
		mGame.add(miSetBlockColor);
		mGame.add(miSetBackColor);
		mGame.addSeparator();
		mGame.add(miTurnHarder);
		mGame.add(miTurnEasier);
		mGame.addSeparator();
		mGame.add(miExit);

		mControl.add(miPlay);
		mControl.add(miPause);
		mControl.add(miResume);
		mControl.add(miStop);

		mWindowStyle.add(miAsWindows);
		mWindowStyle.add(miAsMotif);
		mWindowStyle.add(miAsMetal);

		mInfo.add(miAuthor);
		mInfo.add(miSourceInfo);

		setJMenuBar(bar);// ���˵����ҵ�����

		// ���Ʋ˵�����Ӧ
		// ��ʼ��Ϸ�Ĳ˵���
		miPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				playGame();

			}
		});
		// ��ͣ��Ϸ�Ĳ˵���
		miPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pauseGame();

			}
		});
		// �ָ���Ϸ�Ĳ˵���
		miResume.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resumeGame();

			}
		});
		// ֹͣ��Ϸ�Ĳ˵���
		miStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stopGame();

			}
		});
		// ��Ϸ�˵�����Ӧ
		// ����Ϸ
		miNewGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stopGame();
				reset();
				setLevel(DEFAULT_LEVEL);
			}
		});
		// ���÷�����ɫ
		miSetBlockColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color newFrontColor = JColorChooser.showDialog(ErsBlocksGame.this, "���÷�����ɫ",
						canvas.getBlockColor());
				if (newFrontColor != null) {
					canvas.setBlockColor(newFrontColor);
				}
			}
		});
		// ���ñ�����ɫ
		miSetBackColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color newFrontColor = JColorChooser.showDialog(ErsBlocksGame.this, "���ñ�����ɫ",
						canvas.getBackgroundColor());
				if (newFrontColor != null) {
					canvas.setBackgroundColor(newFrontColor);
				}

			}
		});
		// ������Ϸ�Ѷ�
		miTurnHarder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int curLevel = getLevel();
				if (curLevel < MAX_LEVEL)
					setLevel(curLevel + 1);
			}
		});
		// ������Ϸ�Ѷ�
		miTurnEasier.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int curLevel = getLevel();
				if (curLevel > 1)
					setLevel(curLevel - 1);
			}
		});
		miExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		// 3�����ڷ��˵�����Ӧ
		miAsWindows.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String plaf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
				setWindowStyle(plaf);
				canvas.fanning();
				// ctrlPanel.fanning();///////////////////////////////////////////////////
				miAsWindows.setState(true);
				miAsMetal.setState(false);
				miAsMotif.setState(false);
			}
		});
		miAsMotif.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String plaf = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
				setWindowStyle(plaf);
				canvas.fanning();
				// ctrlPanel.fanning();////////////////////////////////////////
				miAsWindows.setState(false);
				miAsMetal.setState(false);
				miAsMotif.setState(true);
			}
		});
		miAsMetal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String plaf = "com.sun.java.swing.plaf.metal.MetalLookAndFeel";
				setWindowStyle(plaf);
				canvas.fanning();
				// ctrlPanel.fanning();
				miAsWindows.setState(false);
				miAsMetal.setState(true);
				miAsMotif.setState(false);
			}
		});


		//****************��ݼ�������******************
		//****************��ݼ�������******************
		//****************��ݼ�������******************
		miPause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
		miResume.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
	}
	// ���ô������
	private void setWindowStyle(String plaf) {
		try {
			// �趨�û��������
			UIManager.setLookAndFeel(plaf);
			// ���û�����ĳɵ�ǰ�趨�����
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
		}
	}
	// ��Ϸ���Ƶ�ʵ��
	// ��Ϸ��λ
	public void reset() {
		// ctrlPanel.reset();///////////////////////////////////////
		canvas.reset();
	}
	// �ж���Ϸ�Ƿ��ڽ���
	public boolean isPlaying() {
		return playing;
	}
	// ��ȡ��ǰ���
	public ErsBlock getCurBlock() {
		return block;
	}
	// �õ���ǰ����
	public GameCanvas getCanvas() {
		return canvas;
	}
	// �õ���ǰ��Ϸ�Ѷȣ�1-10
	public int getLevel() {
		return ctrlPanel.getLevel();
	}
	// �����Ѷ�ϵ��
	public void setLevel(int level) {// /////////
		if (level < 11 && level > 0)
			ctrlPanel.setLevel(level);
	}
	// �õ���Ϸ����
	public int getScore() {
		if (canvas != null)
			return canvas.getScore();
		return 0;
	}
	// �õ�������������Ϸ���֣���������
	public int getScoreForLevelUpdate() {
		if (canvas != null)
			return canvas.getScoreForLevelUpdate();
		return 0;
	}
	// ��������
	// ��ʼ��Ϸ
	public void playGame() {
		play();
		miPlay.setEnabled(false);
		ctrlPanel.setPlayButtonEnable(false);
		ctrlPanel.requestFocus();
	}
	// ��ͣ��Ϸ
	public void pauseGame() {
		if (block != null)
			block.pauseMove();
		ctrlPanel.setPauseButtonLabel(false);
		miPause.setEnabled(false);
		miResume.setEnabled(true);
	}
	// ������Ϸ
	public void resumeGame() {
		if (block != null)
			block.resumeMove();
		ctrlPanel.setPauseButtonLabel(true);
		miPause.setEnabled(true);
		miResume.setEnabled(false);
		ctrlPanel.requestFocus();
	}
	// �û�ֹͣ��Ϸ
	public void stopGame() {
		playing = false;
		if (block != null)
			block.stopMove();
		miPlay.setEnabled(true);
		miPause.setEnabled(true);
		miResume.setEnabled(false);
		ctrlPanel.setPlayButtonEnable(true);
		ctrlPanel.setPauseButtonLabel(true);
	}
	// ��Ϸ��ʼ
	private void play() {
		reset();
		playing = true;
		Thread thread = new Thread(new Game());
		thread.start();
	}
	// ������Ϸ����
	private void reportGameOver() {
		JOptionPane.showMessageDialog(this, "��Ϸ����");
	}
	// �Զ���������ControlPanel��Ķ�ʱ��Timer�����actionPerformed()�����е���
	public boolean levelUpdate() {
		int curLevel = getLevel();
		if (curLevel < MAX_LEVEL) {
			setLevel(curLevel + 1);
			canvas.resetScoreForLevelUpdate();
			return true;
		}
		return false;
	}


	// һ����Ϸ�Ĺ���,ʵ����Runable�ӿ�
	private class Game implements Runnable {
		public void run() {
			// ���������,������ɷ������̬��28֮һ
			int col = (int) (Math.random() * (canvas.getCols() - 3));
			int style = ErsBlock.STYLES[(int) (Math.random() * 7)][(int) (Math.random()) * 4];
			while (playing) {
				if (block != null) {
					// ��һ��ѭ��blockΪ��
					if (block.isAlive()) {
						try {
							Thread.currentThread().sleep(100);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
						continue;
					}
				}
				checkFullLine();// ����Ƿ���ȫ������
				if (isGameOver()) {
					miPlay.setEnabled(true);
					miPause.setEnabled(true);
					miResume.setEnabled(false);
					ctrlPanel.setPlayButtonEnable(true);
					ctrlPanel.setPauseButtonLabel(true);
					// canvas.repaint();//������Ϸ����ʱ���ƶ���ķ���
					reportGameOver();
					return;
				}
				// ����һ����Ϸ��
				block = new ErsBlock(canvas, style, -1, col, getLevel());
				block.start();
				// ���������,������ɷ������̬��28֮һ
				col = (int) (Math.random() * (canvas.getCols() - 3));
				style = ErsBlock.STYLES[(int) (Math.random() * 7)][(int) (Math.random()) * 4];
				// �ڿ����������ʾ��һ�����״
				ctrlPanel.setTipStyle(style);
			}
		}
		// ��黭�����Ƿ����������У�����о�ɾ��
		public void checkFullLine() {
			for (int i = 0; i < canvas.getRows(); i++) {
				int row = -1;
				boolean isFull = true;
				for (int j = 0; j < canvas.getCols(); j++) {
					// ����i�С�j���Ƿ�Ϊ��ɫ����
					if (!canvas.getBox(i, j).isColorBox()) {
						isFull = false;
						break;
					}
				}
				// ����Ƿ�������Ϣ
				if (isFull == true) {
					row = i;
					i--;
					canvas.removeLine(row);
				}
			}
		}
		// ��������Ƿ�ռ���ж���Ϸ�Ƿ��Ѿ�����
		public boolean isGameOver() {
			for (int i = 0; i < canvas.getCols(); i++) {
				ErsBox box = canvas.getBox(0, i);
				if (box.isColorBox())
					return true;
			}
			return false;
		}
	}

	public static void main(String[] args) {
		new ErsBlocksGame("����˹������Ϸ");
	}

}
