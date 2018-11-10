package ersblocks;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/*
 * �������:
 * 	��Ϊ3���֣���3��JPanel��������ʾ��һ��������Ϣ��TipPanel.
 * 						����ʾ��ǰ��Ϸ��Ϣ��÷֣��Ѷȣ�InfoPanel.
 * 						�۴�ſ��ư�ť:ButtonPanel.
 */
public class ControlPanel extends JPanel {
	private JTextField tfLevel = new JTextField(""+ErsBlocksGame.DEFAULT_LEVEL),// ��Ϸ�Ѷ�
			tfScore = new JTextField("0");// ��ҵ÷�
	//���ư�ť
	private JButton btPlay = new JButton("��ʼ"), btPause = new JButton("��ͣ"),//
			btStop = new JButton("ֹͣ"), btTurnUpLevel = new JButton("�����Ѷ�"),
			btTurnDownLevel = new JButton("�����Ѷ�");
	// �������
	//��ʾ���
	private JPanel plTip = new JPanel(new BorderLayout());
	private TipPanel plTipBlock = new TipPanel();
	// ��Ϣ���
	private JPanel plInfo = new JPanel(new GridLayout(4, 1));
	// ���ư�ť���
	private JPanel plButton = new JPanel(new GridLayout(5, 1));

	private Timer timer;// ʱ�ӿ�����,��ʱˢ�µ÷�
	private ErsBlocksGame game;//////////////////��ǰ��Ϸ��
	private Border border = new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(148, 145,
			140));
	/**
	 * ���������Ĺ��췽��
	 * @param game ErsBlockGame,ErsBlockGame��һ��ʵ��.
	 */
	public ControlPanel(final ErsBlocksGame game) {
		this.game=game;
		//3������壬��Ϊһ��,ÿ�м��Ϊ4
		setLayout(new GridLayout(3, 1, 0, 4));

		plTip.add(new JLabel("��һ������"), BorderLayout.NORTH);
		plTip.add(plTipBlock);
		plTip.setBorder(border);
		
		//��Ϸ��Ϣ����������ǩ�������ı��򼰱߽�
		plInfo.add(new JLabel("��Ϸ�Ѷȼ���"));
		plInfo.add(tfLevel);
		plInfo.add(new JLabel("��ҵ÷�"));
		plInfo.add(tfScore);
		plInfo.setBorder(border);
		//���ɱ༭��ֻ������ʾ
		tfLevel.setEditable(false);
		tfScore.setEditable(false);
		
		//��ť����5����ť���߽�
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
		// ���Ӽ��̵ļ�����
		addKeyListener(new ControlKeyListener());
		// ���Ӱ�ť�ļ�����
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
				if (btPause.getText().equals(new String("��ͣ"))) {
					// btPause.setText("����");
					game.pauseGame();
				} else {
					// btPause.setText("��ͣ");
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
				// btPause.setText("��ͣ");
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
		// ���������
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ae) {
				plTipBlock.fanning();
			}
		});
		// ��ʱ��Timer,500ms��ʱˢ����ҵ÷ֺ��Ѷȼ���
		timer = new Timer(500, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// ��ʾ��ҵ÷�
				tfScore.setText("" + game.getScore());
				// ���÷ֽ����Ѷȼ���
				int scoreForLevelUpdate = game.getScoreForLevelUpdate();
				// ��ʾ���º���Ѷȼ���
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
		btPause.setText(pause ? "��ͣ" : "����");
	}
		
	/**
	 * ��ʾ����� ���ܣ�������ʾ��һ���������ֵķ���
	 */
	private class TipPanel extends JPanel {
		// tile:��ש
		boolean isTiled = false;
		// ��ʾ��Ϣ����ǰ���ͣ�������ɫ
		private Color backColor = Color.darkGray, frontColor = Color.white;// Color.lightGray;
		// �����趨�������������Ķ���˹����
		private ErsBox[][] boxes = new ErsBox[ErsBlock.BOXES_ROWS][ErsBlock.BOXES_COLS];//
		// �����̬���߶�
		private int style, boxWidth, boxHeight;

		// ��ʾ�����Ĺ��췽��
		public TipPanel() {
			for (int i = 0; i < boxes.length; i++) {
				for (int j = 0; j < boxes[i].length; j++) {
					boxes[i][j] = new ErsBox(false);
				}
			}
		}
		/**
		 * Ԥ��ʾ������Ĺ��췽��
		 * 
		 * @param backColor Color,���ڵı���ɫ
		 * @param frontColor Color,�������ǰ��ɫ
		 */
		public TipPanel(Color backColor, Color frontColor) {
			this();
			this.backColor = backColor;
			this.frontColor = frontColor;
		}
		/**
		 * ����Ԥ��ʾ���ڵķ�����ʽ���硰L����
		 * 
		 * @param style int,��ӦErsBlock���STYLES�е�28��ֵ
		 */
		public void setStyle(int style) {
			this.style = style;
			repaint();
		}
		// ����JComponent���paintComponent�������������
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (!isTiled)
				fanning();
			int key = 0x8000;
			for (int i = 0; i < boxes.length; i++) {
				for (int j = 0; j < boxes[i].length; j++) {
					Color color = ((key & style) != 0) ? frontColor : backColor;
					g.setColor(color);
					g.fill3DRect(j * boxWidth, i * boxHeight, boxWidth, boxHeight, true);// ����һ���õ�ǰ��ɫ����
																							// 3-D������ʾ���Ρ�

					key >>= 1;
				}
			}
		}
		// ���ݷ���Ĵ�С�Զ���������Ĵ�С
		public void fanning() {
			boxWidth = getSize().width / ErsBlock.BOXES_COLS;
			boxHeight = getSize().height / ErsBlock.BOXES_ROWS;
			isTiled = true;
		}
	}
	/** �����ӿ� */
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
