package ersblocks;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * 俄罗斯方块游戏的主类,继承自JFrame，控制全局.
 * 主界面分为一个菜单，两个窗口：一个Canvas，另一个是ControlPanel，之上是菜单.
 * 
 */
public class ErsBlocksGame extends JFrame {
	// 每消除一行加100分
	public final static int PER_LINE_SCORE = 100;
	// 玩家到达2000分就将游戏难度level加1
	public final static int PER_LEVEL_SCORE = PER_LINE_SCORE * 20;
	// 最大级数为10，默认为3:
	public final static int MAX_LEVEL=10;
	public final static int DEFAULT_LEVEL = 3;

	private GameCanvas canvas;// 画布类
	private ErsBlock block;
	private ControlPanel ctrlPanel;// 控制类
	private boolean playing = false;// 当前游戏是否进行

	private JMenuBar bar = new JMenuBar();
	// 菜单条bar包含4个菜单
	private JMenu mGame = new JMenu("游戏"), mControl = new JMenu("控制"), mWindowStyle = new JMenu(
			"窗口风格"),
			mInfo = new JMenu("帮助");
	// 菜单项
	private JMenuItem miNewGame = new JMenuItem("新游戏"), miSetBlockColor = new JMenuItem("设置方块颜色"),
			miSetBackColor = new JMenuItem("设置背景颜色"), miTurnHarder = new JMenuItem("增加难度"),
			miTurnEasier = new JMenuItem("降低难度"), miExit = new JMenuItem("退出"),
			miPlay = new JMenuItem("开始"), miPause = new JMenuItem("暂停"), miResume = new JMenuItem(
					"继续"), miStop = new JMenuItem("停止"), miAuthor = new JMenuItem("作者：HANDLE"),
			miSourceInfo = new JMenuItem("版本: 1.0");
	// 设置窗口风格菜单
	private JCheckBoxMenuItem miAsWindows = new JCheckBoxMenuItem("Winodws"),
			miAsMotif = new JCheckBoxMenuItem("Motif"), miAsMetal = new JCheckBoxMenuItem("Metal",
					false);

	// 主游戏类的构造方法
	public ErsBlocksGame(String title){
		super(title);
		setSize(315, 392);

		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		// 置于屏幕中央
		setLocation((scrSize.width - getSize().width) / 2, (scrSize.height - getSize().height) / 2);
		// 创建菜单
		creatMenu();
		Container container = getContentPane();
		// 设置的布局为BorderLayout
		// 布局的水平构件之间有6个像素的距离
		container.setLayout(new BorderLayout(6, 0));
		canvas = new GameCanvas(20, 12);// 画布20x12
		ctrlPanel = new ControlPanel(this);// 控制面板
		// 游戏画布和控制面板之间左右摆放
		container.add(canvas, BorderLayout.CENTER);
		container.add(ctrlPanel, BorderLayout.EAST);
		// 增加窗口监听器
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				// stopGame();
				System.exit(0);
			}
		});
		// 构件的适配器
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ce) {
				canvas.fanning();
			}
		});
		setVisible(true);
		canvas.fanning();
	}

	private void creatMenu() {
		bar.add(mGame); // 加入菜单
		bar.add(mControl);
		bar.add(mWindowStyle);
		bar.add(mInfo);
		mGame.add(miNewGame);// 加入菜单项
		mGame.addSeparator();// 分隔线
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

		setJMenuBar(bar);// 将菜单条挂到窗口

		// 控制菜单的响应
		// 开始游戏的菜单项
		miPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				playGame();

			}
		});
		// 暂停游戏的菜单项
		miPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pauseGame();

			}
		});
		// 恢复游戏的菜单项
		miResume.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resumeGame();

			}
		});
		// 停止游戏的菜单项
		miStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stopGame();

			}
		});
		// 游戏菜单的响应
		// 新游戏
		miNewGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stopGame();
				reset();
				setLevel(DEFAULT_LEVEL);
			}
		});
		// 设置方块颜色
		miSetBlockColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color newFrontColor = JColorChooser.showDialog(ErsBlocksGame.this, "设置方块颜色",
						canvas.getBlockColor());
				if (newFrontColor != null) {
					canvas.setBlockColor(newFrontColor);
				}
			}
		});
		// 设置背景颜色
		miSetBackColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color newFrontColor = JColorChooser.showDialog(ErsBlocksGame.this, "设置背景颜色",
						canvas.getBackgroundColor());
				if (newFrontColor != null) {
					canvas.setBackgroundColor(newFrontColor);
				}

			}
		});
		// 增加游戏难度
		miTurnHarder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int curLevel = getLevel();
				if (curLevel < MAX_LEVEL)
					setLevel(curLevel + 1);
			}
		});
		// 降低游戏难度
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
		// 3个窗口风格菜单的响应
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


		//****************快捷键的设置******************
		//****************快捷键的设置******************
		//****************快捷键的设置******************
		miPause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
		miResume.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
	}
	// 设置窗口外观
	private void setWindowStyle(String plaf) {
		try {
			// 设定用户界面外观
			UIManager.setLookAndFeel(plaf);
			// 将用户界面改成当前设定的外观
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
		}
	}
	// 游戏控制的实现
	// 游戏复位
	public void reset() {
		// ctrlPanel.reset();///////////////////////////////////////
		canvas.reset();
	}
	// 判断游戏是否还在进行
	public boolean isPlaying() {
		return playing;
	}
	// 获取当前活动块
	public ErsBlock getCurBlock() {
		return block;
	}
	// 得到当前画布
	public GameCanvas getCanvas() {
		return canvas;
	}
	// 得到当前游戏难度：1-10
	public int getLevel() {
		return ctrlPanel.getLevel();
	}
	// 设置难度系数
	public void setLevel(int level) {// /////////
		if (level < 11 && level > 0)
			ctrlPanel.setLevel(level);
	}
	// 得到游戏积分
	public int getScore() {
		if (canvas != null)
			return canvas.getScore();
		return 0;
	}
	// 得到升级以来的游戏积分，升级清零
	public int getScoreForLevelUpdate() {
		if (canvas != null)
			return canvas.getScoreForLevelUpdate();
		return 0;
	}
	// 几个控制
	// 开始游戏
	public void playGame() {
		play();
		miPlay.setEnabled(false);
		ctrlPanel.setPlayButtonEnable(false);
		ctrlPanel.requestFocus();
	}
	// 暂停游戏
	public void pauseGame() {
		if (block != null)
			block.pauseMove();
		ctrlPanel.setPauseButtonLabel(false);
		miPause.setEnabled(false);
		miResume.setEnabled(true);
	}
	// 继续游戏
	public void resumeGame() {
		if (block != null)
			block.resumeMove();
		ctrlPanel.setPauseButtonLabel(true);
		miPause.setEnabled(true);
		miResume.setEnabled(false);
		ctrlPanel.requestFocus();
	}
	// 用户停止游戏
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
	// 游戏开始
	private void play() {
		reset();
		playing = true;
		Thread thread = new Thread(new Game());
		thread.start();
	}
	// 报告游戏结束
	private void reportGameOver() {
		JOptionPane.showMessageDialog(this, "游戏结束");
	}
	// 自动升级，在ControlPanel类的定时器Timer对象的actionPerformed()方法中调用
	public boolean levelUpdate() {
		int curLevel = getLevel();
		if (curLevel < MAX_LEVEL) {
			setLevel(curLevel + 1);
			canvas.resetScoreForLevelUpdate();
			return true;
		}
		return false;
	}


	// 一轮游戏的过程,实现了Runable接口
	private class Game implements Runnable {
		public void run() {
			// 随机生成列,随机生成方块的形态，28之一
			int col = (int) (Math.random() * (canvas.getCols() - 3));
			int style = ErsBlock.STYLES[(int) (Math.random() * 7)][(int) (Math.random()) * 4];
			while (playing) {
				if (block != null) {
					// 第一次循环block为空
					if (block.isAlive()) {
						try {
							Thread.currentThread().sleep(100);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
						continue;
					}
				}
				checkFullLine();// 检查是否有全满的行
				if (isGameOver()) {
					miPlay.setEnabled(true);
					miPause.setEnabled(true);
					miResume.setEnabled(false);
					ctrlPanel.setPlayButtonEnable(true);
					ctrlPanel.setPauseButtonLabel(true);
					// canvas.repaint();//可让游戏结束时绘制顶层的方块
					reportGameOver();
					return;
				}
				// 创建一个游戏块
				block = new ErsBlock(canvas, style, -1, col, getLevel());
				block.start();
				// 随机生成列,随机生成方块的形态，28之一
				col = (int) (Math.random() * (canvas.getCols() - 3));
				style = ErsBlock.STYLES[(int) (Math.random() * 7)][(int) (Math.random()) * 4];
				// 在控制面板中提示下一块的形状
				ctrlPanel.setTipStyle(style);
			}
		}
		// 检查画布中是否有填满的行，如果有就删除
		public void checkFullLine() {
			for (int i = 0; i < canvas.getRows(); i++) {
				int row = -1;
				boolean isFull = true;
				for (int j = 0; j < canvas.getCols(); j++) {
					// 检查第i行、j列是否为彩色方块
					if (!canvas.getBox(i, j).isColorBox()) {
						isFull = false;
						break;
					}
				}
				// 输出是否满行信息
				if (isFull == true) {
					row = i;
					i--;
					canvas.removeLine(row);
				}
			}
		}
		// 根据最顶行是否被占，判断游戏是否已经结束
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
		new ErsBlocksGame("俄罗斯方块游戏");
	}

}
