package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import base.Member;
import base.MemberSheet;
import base.Pattern;
import base.Schedule;
import process.Core;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JComboBox;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6841497333789115515L;
	
	private final static int WIDTH = 1120;
	private final static int HEIGHT = 630;
	
	private Coordination point = new Coordination();
	private JPanel contentPane;
	private JComboBox<String>[] members;
	private MyButton[] modifiers;
	private JTextArea textArea;
	private boolean state = false;
	private boolean scheduleState = true;
	boolean saveState = true;
	private Member user;
	private Core core;

	/**
	 * Create the frame.
	 */
	public MainWindow(Core core, Member user, boolean authorization) {
		
		this.state = authorization;
		
		
		try {
			Class.forName("ui.ArrangeDialog");
			scheduleState = true;
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		this.user = user;
		this.core = core;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		this.setSize(WIDTH, HEIGHT);
		this.setLocation((width - WIDTH) / 2, (height - HEIGHT) / 2);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/temp.png")));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		ImageIcon image = new ImageIcon(MainWindow.class.getResource("/resources/mainbg2.png"));
		image.setImage(image.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH));
		
		
		JButton exit = new JButton("\u9000\u51FA\u7CFB\u7EDF");
		exit.setLocation(1007, 597);
		exit.setSize(93, 23);
		
		exit.addActionListener(
			e->{
				if(!saveState) {
					int i = JOptionPane.showConfirmDialog(MainWindow.this, "未保存排班记录，本次排班将不会被记录，是否退出？", "提示", JOptionPane.YES_NO_OPTION);
					if(i == JOptionPane.YES_OPTION)
						System.exit(0);
					else
						return;
				}
				System.exit(0);
			}
		);
		contentPane.add(exit);
		
		members = new JComboBox[21];
		modifiers = new MyButton[21];
		updateRecord(null);
		
		synchronized (new Object()) {
			JScrollPane tarea = null;
			
			JLabel label = null;
			if (state) {
				tarea = new JScrollPane();
				textArea = new JTextArea();
				textArea.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
				textArea.setLineWrap(true);
				textArea.setEditable(state);
				tarea.setBounds(47, 143, 224, 300);
				tarea.setViewportView(textArea);
			} else {
				label = new JLabel("<html>\u7ba1\u7406\u5458\u6a21\u5f0f<br>\u6fc0\u6d3b\u5907\u6ce8\u529f\u80fd</html>");
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setFont(MyFont.getFont(Font.PLAIN, 25));
				label.setForeground(MyFont.getColor());
				label.setBounds(47, 143, 224, 300);
			}
			contentPane.add(state ? tarea : label);
		}
	
		
		
		JLabel cuser = new JLabel(user.getName()+(state?"(管理员)":""));
		cuser.setLocation(116, 545);
		cuser.setSize(124, 30);
		cuser.setFont(MyFont.getFont(Font.PLAIN, 16));
		cuser.setForeground(MyFont.getColor());
		
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
		JLabel ctime = new JLabel(dtf.format(ldt));
		ctime.setFont(MyFont.getFont(Font.PLAIN, 16));
		ctime.setForeground(MyFont.getColor());
		ctime.setLocation(116, 502);
		ctime.setSize(192, 36);
		
		contentPane.add(cuser);
		contentPane.add(ctime);
		
		JButton auto = new JButton("\u81EA\u52A8\u6392\u73ED");
		auto.setBounds(497, 550, 93, 23);
		auto.setVisible(state);
		auto.addActionListener(e -> {
			if(!scheduleState) {
				JOptionPane.showMessageDialog(MainWindow.this, "无法完成操作，请检查是否制定排班计划", "提示", JOptionPane.WARNING_MESSAGE);
				return;
			}
			Pattern p = new Pattern() {

				@Override
				public Priority tagsPattern(Member m, int time) {
					// TODO Auto-generated method stub
					if(m.hasTag(Member.Tag.THURSDAY)) {
						if(time == 1024) {
							return Priority.HIGH;
						}else {
							return Priority.LOW;
						}
					}else {
						return Priority.MEDIUM;
					}
				}
				
			};
			core.autoArrange(ArrangeDialog.getSchedule(),p);
			flush();
			this.saveState = false;
		});
		contentPane.add(auto);
		
		JButton check = new JButton("\u67E5\u770B\u6392\u73ED\u8BB0\u5F55");
		check.setBounds(661, 550, 124, 23);
		check.addActionListener(e -> {
			new MemberDialog(this, core.generateSheet(), state).setVisible(true);
		});
		contentPane.add(check);
		
		JButton modify = new JButton("\u4EBA\u5458\u7BA1\u7406");
		modify.setBounds(661, 509, 93, 23);
		modify.addActionListener(e->{
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						PersonnelDialog dialog = new PersonnelDialog(MainWindow.this,core,user);
						dialog.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		});
		modify.setVisible(state);
		
		JButton arrange = new JButton("\u6392\u73ED\u5B89\u6392");
		arrange.setBounds(497, 509, 93, 23);
		arrange.addActionListener(e->{
			ArrangeDialog.arrangeSchedule(MainWindow.this);
			scheduleState = true;
		});
		arrange.setVisible(state);
		contentPane.add(arrange);
		contentPane.add(modify);
		
		JButton history = new JButton("\u67E5\u770B\u5386\u53F2\u6392\u73ED");
		history.setBounds(831, 550, 124, 23);
		history.addActionListener(e->{
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						HistoryDialog dialog = new HistoryDialog(MainWindow.this);
						dialog.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		});
		contentPane.add(history);
		
		JButton minimize = new JButton("\u6700\u5C0F\u5316\u7A97\u53E3");
		minimize.setBounds(904, 597, 93, 23);
		minimize.addActionListener(
			e -> {
				MainWindow.this.setState(Frame.ICONIFIED);
			}
		);
		contentPane.add(minimize);
		
		for(MyButton b : modifiers) {
			if(b == null)
				break;
			b.addActionListener(e -> {
				EventQueue.invokeLater(() -> {
					MannualModifyDialog dialog = new MannualModifyDialog(MainWindow.this, core, 1 << b.getId(), b.getLocationOnScreen().x, b.getLocationOnScreen().y);

					dialog.setVisible(true);
					saveState = !dialog.getState();
				});
			});
		}
		
		JLabel background = new JLabel("");
		background.setBounds(0, 0, WIDTH, HEIGHT);
		background.setIcon(image);
		contentPane.add(background);
		flush();
		addDraggingFunction(this,this.point);
	}
	
	static void addDraggingFunction(Component component,Coordination point) {
		component.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if(point.button != MouseEvent.BUTTON1) {
					return;
				}
				int newX = e.getXOnScreen();
				int newY = e.getYOnScreen();
				component.setLocation(newX-point.mouseX, newY-point.mouseY);
			}
		});
		component.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				point.button = e.getButton();
				point.mouseX = e.getX();
				point.mouseY = e.getY();
			}
		});
	}
	
	private synchronized void updateRecord(ArrayList<String>[] str) {
		if (str == null) {
			for (int i = 0; i < members.length; i++) {
				int x = 412 + (i / 3) * 91 - i / 9;
				int y = 214 + (i % 3) * 72;
				members[i] = new JComboBox<String>();
				members[i].setBounds(x, y, 81, 23);
				contentPane.add(members[i]);
				if (state) {
					modifiers[i] = new MyButton("\u4FEE\u6539", i);
					modifiers[i].setBounds(x + 4, y + 26, 73, 23);
					contentPane.add(modifiers[i]);
				}
			}
		} else {
			for (int i = 0; i < 21; i++) {
				members[i].removeAllItems();
				for (String s : str[i]) {
					members[i].addItem(s);
				}
			}
		}
	}
	
	Member getUser() {
		return user;
	}
	
	void flush() {
		MemberSheet ms = core.generateSheet();
		ArrayList<String>[] str = resolveSheet(ms);
		updateRecord(str);
		setVisible(true);
	}
	
	String getNote() {
		return textArea==null?"":textArea.getText();
	}
	
	boolean getAuth() {
		return state;
	}
	static ArrayList<String>[] resolveSheet(MemberSheet sheet) {
		ArrayList<String>[] result = new ArrayList[21];
		for(int i = 0; i < 21; i++) {
			result[i] = new ArrayList<>();
		}
		HashMap<Member,Integer> map = sheet.getSheet();
		Iterator<Member> i = map.keySet().iterator();
		while(i.hasNext()) {
			Member m = i.next();
			int loc = map.get(m);
			for(int j = 0; j < 21; j++) {
				if(((1<<j)&loc)!=0) {
					result[j].add(m.getName());
				}
			}
		}
		return result;
	}
}

class Coordination{
	Coordination(){}
	int mouseX;
	int mouseY;
	int button;
}

class MyButton extends JButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5316672825938290860L;
	private int id;
	MyButton(String tag, int id){
		super(tag);
		this.id = id;
	}
	int getId(){
		return this.id;
	}
}

class MyFont{
	private static Font font;
	private static Color color;
	static {
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir")+"//property//FZYANZQKSJF.TTF"));
			color = new Color(65,83,128);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private MyFont() {}
	public static Font getFont(int style, int size) {
		Font f = font.deriveFont(style).deriveFont((float)size);
		return f;
	}
	public static Color getColor() {
		return color;
	}
}
