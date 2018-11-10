package ersblocks;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/*
 * 控制面板:
 * 	分为3部分（含3个JPanel）：①提示下一个方块信息的TipPanel.
 * 						②显示当前游戏信息如得分，难度：InfoPanel.
 * 						③存放控制按钮:ButtonPanel.
 */
public class ControlPanel extends JPanel {
	private JTextField tfLevel = new JTextField(""+ErsBlocksGame.DEFAULT_LEVEL),// 游戏难度
			tfScore = new JTextField("0");// 玩家得分
	//控制按钮
	private JButton btPlay = new JButton("开始"), btPause = new JButton("暂停"),//
			btStop = new JButton("停止"), btTurnUpLevel = new JButton("增加难度"),
			btTurnDownLevel = new JButton("降低难度");
	// 几个面板
	//提示面板
	private JPanel plTip = new JPanel(new BorderLayout());
	private TipPanel plTipBlock = new TipPanel();
	// 信息面板
	private JPanel plInfo = new JPanel(new GridLayout(4, 1));
	// 控制按钮面板
	private JPanel plButton = new JPanel(new GridLayout(5, 1));

	private Timer timer;// 时钟控制器,定时刷新得分
	private ErsBlocksGame game;//////////////////当前游戏局
	private Border border = new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(148, 145,
			140));
	/**
	 * 主控制面板的构造方法
	 * @param game ErsBlockGame,ErsBlockGame的一个实例.
	 */
	public ControlPanel(final ErsBlocksGame game) {
		this.game=game;
		//3个子面板，并为一列,每行间距为4
		setLayout(new GridLayout(3, 1, 0, 4));

		plTip.add(new JLabel("下一个方块"), BorderLayout.NORTH);
		plTip.add(plTipBlock);
		plTip.setBorder(border);
		
		//游戏信息面板的两个标签和两个文本域及边界
		plInfo.add(new JLabel("游戏难度级别"));
		plInfo.add(tfLevel);
		plInfo.add(new JLabel("玩家得分"));
		plInfo.add(tfScore);
		plInfo.setBorder(border);
		//不可编辑，只用于显示
		tfLevel.setEditable(false);
		tfScore.setEditable(false);
		
		//按钮面板的5个按钮及边界
		plButton.add(btPlay);
		plButton.add(btPause);
		plButton.add(btStop);
		plButton.add(btTurnUpLevel);
		plButton.add(btTurnDownLevel);
		plButton.setBorder(border);

		add(plTip);
		add(plInfo);
		add(plButton);
		// btPlay.setEnabled(true);
		// btPause.setEnabled(false);
		// btStop.setEnabled(false);
		// 增加键盘的监听器
		addKeyListener(new ControlKeyListener());
		// 增加按钮的监听器
		btPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				game.playGame();
				// btPlay.setEnabled(false);
				// btPause.setEnabled(true);
				// btStop.setEnabled(true);
			}
		});
		btPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (btPause.getText().equals(new String("暂停"))) {
					// btPause.setText("继续");
					game.pauseGame();
				} else {
					// btPause.setText("暂停");
					game.resumeGame();
				}
			}
		});
		btStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				game.stopGame();
				// btStop.setEnabled(false);
				// btPause.setEnabled(false);
				// btPause.setText("暂停");
				// btPlay.setEnabled(true);

			}
		});
		btTurnUpLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				try{
					int level=Integer.parseInt(tfLevel.getText());
					if(level<ErsBlocksGame.MAX_LEVEL){
						tfLevel.setText(""+(level+1));
					}
				} catch (NumberFormatException e) {
				}
				requestFocus();
			}
		});
		btTurnDownLevel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				try{
					int level=Integer.parseInt(tfLevel.getText());
					if(level>1){
						tfLevel.setText("" + (level - 1));
					}
				} catch (NumberFormatException e) {
				}
				requestFocus();
			}
		});
		// 组件适配器
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ae) {
				plTipBlock.fanning();
			}
		});
		// 定时器Timer,500ms定时刷新玩家得分和难度级别
		timer = new Timer(500, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 显示玩家得分
				tfScore.setText("" + game.getScore());
				// 按得分晋升难度级别
				int scoreForLevelUpdate = game.getScoreForLevelUpdate();
				// 显示更新后的难度级别
				if (scoreForLevelUpdate >= ErsBlocksGame.PER_LEVEL_SCORE && scoreForLevelUpdate > 0)
					game.levelUpdate();
			}
		});
		timer.start();
	}


	public int getLevel() {
		return Integer.parseInt(tfLevel.getText());
	}
	public void setLevel(int level) {
		tfLevel.setText("" + level);
	}
	public void setTipStyle(int style) {
		plTipBlock.setStyle(style);
	}


	public void setPlayButtonEnable(boolean enable) {
		btPlay.setEnabled(enable);
	}
	public void setPauseButtonLabel(boolean pause) {
		btPause.setText(pause ? "暂停" : "继续");
	}
		
	/**
	 * 提示面板类 功能：用于提示下一个即将出现的方块
	 */
	private class TipPanel extends JPanel {
		// tile:瓷砖
		boolean isTiled = false;
		// 提示信息面板的前景和，背景颜色
		private Color backColor = Color.darkGray, frontColor = Color.white;// Color.lightGray;
		// 创建设定的行数和列数的俄罗斯方块
		private ErsBox[][] boxes = new ErsBox[ErsBlock.BOXES_ROWS][ErsBlock.BOXES_COLS];//
		// 块的形态、高度
		private int style, boxWidth, boxHeight;

		// 提示面板类的构造方法
		public TipPanel() {
			for (int i = 0; i < boxes.length; i++) {
				for (int j = 0; j < boxes[i].length; j++) {
					boxes[i][j] = new ErsBox(false);
				}
			}
		}
		/**
		 * 预提示窗口类的构造方法
		 * 
		 * @param backColor Color,窗口的背景色
		 * @param frontColor Color,窗口类的前景色
		 */
		public TipPanel(Color backColor, Color frontColor) {
			this();
			this.backColor = backColor;
			this.frontColor = frontColor;
		}
		/**
		 * 设置预显示窗口的方块样式，如“L”型
		 * 
		 * @param style int,对应ErsBlock类的STYLES中的28个值
		 */
		public void setStyle(int style) {
			this.style = style;
			repaint();
		}
		// 覆盖JComponent类的paintComponent方法，绘制组件
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (!isTiled)
				fanning();
			int key = 0x8000;
			for (int i = 0; i < boxes.length; i++) {
				for (int j = 0; j < boxes[i].length; j++) {
					Color color = ((key & style) != 0) ? frontColor : backColor;
					g.setColor(color);
					g.fill3DRect(j * boxWidth, i * boxHeight, boxWidth, boxHeight, true);// 绘制一个用当前颜色填充的
																							// 3-D高亮显示矩形。

					key >>= 1;
				}
			}
		}
		// 根据方块的大小自动调整方格的大小
		public void fanning() {
			boxWidth = getSize().width / ErsBlock.BOXES_COLS;
			boxHeight = getSize().height / ErsBlock.BOXES_ROWS;
			isTiled = true;
		}
	}
	/** 监听接口 */
	private class ControlKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (!game.isPlaying())
				return;
			ErsBlock block = game.getCurBlock();
			switch (e.getKeyCode()) {
			case KeyEvent.VK_DOWN:
				block.moveDown();
				break;
			case KeyEvent.VK_LEFT:
				block.moveLeft();
				break;
			case KeyEvent.VK_RIGHT:
				block.moveRight();
				break;
			case KeyEvent.VK_UP:
				block.turnNext();
			default:
				break;
			}
		}
	}
}
