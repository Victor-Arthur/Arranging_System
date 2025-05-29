package ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import base.Member;
import input.FileReader;
import output.Record;
import process.Core;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PersonnelDialog extends JDialog implements process.Input{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4800126684984927968L;

	/**
	 * Launch the application.
	 */
	static {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private final static int WIDTH = 400;
	private final static int HEIGHT = 600;
	private Coordination point = new Coordination();
	private boolean state = true;
	private JPanel panel;


	private Set<MemberCheckBox> members;

//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					PersonnelDialog dialog = new PersonnelDialog(null, new Core(new FileReader()),null);
//
//					dialog.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the dialog.
	 */
	public PersonnelDialog(MainWindow frame, Core core, Member user) {
		super(frame, true);
		getContentPane().setBackground(new Color(240, 255, 255));
		getContentPane().setLayout(null);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		this.setSize(WIDTH, HEIGHT);
		this.setLocation((width - WIDTH) / 2, (height - HEIGHT) / 2);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/temp.png")));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setUndecorated(true);
		MainWindow.addDraggingFunction(this, point);

		members = MemberCheckBox.convertToCheckBoxSet(core.getMembers());

		JLabel title = new JLabel("\u4EBA\u5458\u7BA1\u7406\u7CFB\u7EDF");
		title.setBounds(124, 10, 152, 45);
		title.setFont(MyFont.getFont(Font.PLAIN, 24));
		title.setForeground(MyFont.getColor());
		getContentPane().add(title);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 65, 380, 494);
		getContentPane().add(scrollPane);

		JPanel hPanel = new JPanel();

		hPanel.setLayout(new GridLayout(1, 4, 5, 10));

		JLabel name = new JLabel("姓名");
		JLabel spare = new JLabel("空闲时段");
		JLabel tags = new JLabel("备注");

		name.setFont(MyFont.getFont(Font.PLAIN, 18));
		name.setForeground(MyFont.getColor());
		spare.setFont(MyFont.getFont(Font.PLAIN, 18));
		spare.setForeground(MyFont.getColor());
		tags.setFont(MyFont.getFont(Font.PLAIN, 18));
		tags.setForeground(MyFont.getColor());

		hPanel.add(new JLabel(""));
		hPanel.add(name);
		hPanel.add(spare);
		hPanel.add(tags);
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(0, 4, 5, 10));
		flush();
		scrollPane.setViewportView(panel);

		scrollPane.setColumnHeaderView(hPanel);

		JButton exit = new JButton("\u5173\u95ED");
		exit.addActionListener(e -> {
			if(!state) {
				int i = JOptionPane.showConfirmDialog(PersonnelDialog.this, "未保存修改，是否退出？", "提示", JOptionPane.YES_NO_OPTION);
				if(i == JOptionPane.YES_OPTION)
					PersonnelDialog.this.dispose();
				else
					return;
			}
			PersonnelDialog.this.dispose();
		});
		exit.setBounds(333, 569, 57, 23);
		getContentPane().add(exit);

		JButton add = new JButton("\u65B0\u589E");
		add.setBounds(138, 569, 57, 23);
		add.addActionListener(e->{
			EventQueue.invokeLater(() -> {
				AddDialog dialog = new AddDialog(PersonnelDialog.this,MemberCheckBox.convertToMemberSet(members));
				dialog.setVisible(true);
				if(dialog.getMember() == null) {
					return;
				}
				members.add(new MemberCheckBox(dialog.getMember()));
				flush();
				PersonnelDialog.this.setVisible(true);
				state = false;
			});
		});
		getContentPane().add(add);

		JButton delete = new JButton("\u5220\u9664");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		delete.setBounds(203, 569, 57, 23);
		delete.addActionListener(e->{
			Iterator<MemberCheckBox> i = members.iterator();
			HashSet<MemberCheckBox> set = new HashSet<>();
			while(i.hasNext()) {
				MemberCheckBox m = i.next();
				if(m.isSelected()&&(m.getMember()!=user)) {
					set.add(m);
				}else if(m.getMember()==user) {
					m.setSelected(false);
				}
			}
			if(set.isEmpty()) {
				JOptionPane.showMessageDialog(PersonnelDialog.this, "请选择待删除人员。","提示",JOptionPane.WARNING_MESSAGE);
				return;
			}
			int r = JOptionPane.showConfirmDialog(PersonnelDialog.this, "确认删除吗？","删除",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			if(r == JOptionPane.YES_OPTION) {
				members.removeAll(set);
				flush();
				PersonnelDialog.this.setVisible(true);
				state = false;
			}else {
				Iterator<MemberCheckBox> is = set.iterator();
				while(is.hasNext()) {
					is.next().setSelected(false);
				}
			}
		});
		getContentPane().add(delete);
		
		JButton input = new JButton("\u5BFC\u5165");
		input.setBounds(8, 569, 57, 23);
		input.addActionListener(e->{
			int r = JOptionPane.showConfirmDialog(PersonnelDialog.this, "导入新表格会导致当前排班表被替换。","提示",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
			if(r!=JOptionPane.OK_OPTION) {
				return;
			}
			JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV 数据表(*.csv)", "csv");
			chooser.setFileFilter(filter);
			r = chooser.showOpenDialog(PersonnelDialog.this);
			if(r == JFileChooser.APPROVE_OPTION) {
				File f = chooser.getSelectedFile();
				try {
					PersonnelDialog.this.members = MemberCheckBox.convertToCheckBoxSet(FileReader.getFromFile(f));
					flush();
					PersonnelDialog.this.setVisible(true);
					state = false;
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		getContentPane().add(input);
		
		JButton save = new JButton("\u4FDD\u5B58");
		save.setBounds(268, 569, 57, 23);
		save.addActionListener(e->{
			try {
				Record.saveMembers(new HashSet<Member>(MemberCheckBox.convertToMemberSet(members)));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			core.link(PersonnelDialog.this);
			frame.flush();
			JOptionPane.showMessageDialog(PersonnelDialog.this, "保存成功！","提示",JOptionPane.INFORMATION_MESSAGE);
			state = true;
		});
		getContentPane().add(save);
		
		JButton modify = new JButton("\u4FEE\u6539");
		modify.setBounds(73, 569, 57, 23);
		modify.addActionListener(e->{
			ArrayList<MemberCheckBox> list = new ArrayList<>(members);
			int r = -1;
			for(int i = 0 ;i < list.size();i++) {
				if(list.get(i).isSelected()) {
					r = i;
					list.get(i).setSelected(false);
					break;
				}
			}
			if(r == -1) {
				JOptionPane.showMessageDialog(PersonnelDialog.this, "请选择待修改人员。","提示",JOptionPane.WARNING_MESSAGE);
				return;
			}
			final Member mem = list.get(r).getMember();
			EventQueue.invokeLater(()->{
				ModifyDialog dialog = new ModifyDialog(PersonnelDialog.this, mem);
				dialog.setVisible(true);
				
				flush();
				PersonnelDialog.this.setVisible(true);
				state = !dialog.getResult();
			});
		});
		getContentPane().add(modify);

	}

	private void flush() {
		panel.removeAll();
		Iterator<MemberCheckBox> i = members.iterator();
		int count = 1;
		while (i.hasNext()) {
			MemberCheckBox m = i.next();
			m.setText(count+"");
			JLabel n = new JLabel(m.getMember().getName());
			MyLabel s = new MyLabel(this,m.getMember().getSpareTime());
			String[] t = m.getMember().getDescription();

			panel.add(m);
			count++;
			panel.add(n);
			panel.add(s);
			if (t.length == 1) {
				panel.add(new JLabel(t[0]));
			} else if (t.length > 1) {
				JComboBox<String> b = new JComboBox<>(t);
				panel.add(b);
			} else {
				panel.add(new JLabel(""));
			}
		}
		
	}
//	void showSpare(int x, int y, int sheet) {
//		EventQueue.invokeLater(new Runnable(){
//			public void run() {
//			try {
//				dialog = new SpareDialog(x, y, sheet,PersonnelDialog.this);
//				//dialog.setParent();
//			}catch(Exception e1) {
//				e1.printStackTrace();
//			}}
//		});
//	}
//	void moveSpare(int x,int y) {
//		if (dialog == null) {
//			return;
//		}
//		dialog.setLocation(x, y);
//	}
//	void deleteSpare() {
//		dialog.dispose();
//		dialog = null;
//	}

	@Override
	public Set<? extends Member> getMemberSet() {
		// TODO Auto-generated method stub
		return MemberCheckBox.convertToMemberSet(members);
	}
}

class MyLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7867760427092986524L;
	private int sheet;
	private SpareDialog dialog;
	public MyLabel(PersonnelDialog pdialog,int sheet) {
		super(sheet + "");

		this.sheet = sheet;
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				if (dialog == null) {
					return;
				}
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				dialog.setLocation(x+20, y);
				
			}
		});
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();

				dialog = new SpareDialog(x+20, y, MyLabel.this.sheet);
				dialog.setVisible(true);

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				dialog.dispose();
				dialog = null;
			}
		});
	}
}

class SpareDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6663336412466072379L;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					SpareDialog dialog = new SpareDialog(null,400,400,1753127);
//					dialog.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	JRadioButton jrb[] = new JRadioButton[21];
	

	/**
	 * Create the dialog.
	 */
	SpareDialog(JDialog dialog, int x, int y) {
		super(dialog,true);
		
		getContentPane().setBackground(new Color(240, 255, 255));
		this.setLocation(x, y);
		this.setSize(193, 99);
		getContentPane().setLayout(null);
		this.setUndecorated(true);
		this.setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
	}
	
	SpareDialog(int x, int y, int sheet){
		super();
		getContentPane().setBackground(new Color(240, 255, 255));
		this.setLocation(x, y);
		this.setSize(193, 99);
		getContentPane().setLayout(null);
		this.setUndecorated(true);
		this.setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		addChooser(0, 0, jrb,sheet);
	}
	
	void addChooser(int x,int y, JRadioButton[] jrb, int sheet) {
		JLabel l1 = new JLabel("\u65E9");
		l1.setBounds(x+10, y+28, 18, 15);
		getContentPane().add(l1);

		JLabel l2 = new JLabel("\u5348");
		l2.setBounds(x+10, y+53, 18, 15);
		getContentPane().add(l2);

		JLabel l3 = new JLabel("\u665A");
		l3.setBounds(x+10, y+78, 18, 15);
		getContentPane().add(l3);

		JLabel u1 = new JLabel("\u4E00");
		u1.setBounds(x+32, y+10, 18, 15);
		getContentPane().add(u1);

		JLabel u2 = new JLabel("\u4E8C");
		u2.setBounds(x+55, y+10, 18, 15);
		getContentPane().add(u2);

		JLabel u3 = new JLabel("\u4E09");
		u3.setBounds(x+78, y+10, 18, 15);
		getContentPane().add(u3);

		JLabel u4 = new JLabel("\u56DB");
		u4.setBounds(x+101, y+10, 18, 15);
		getContentPane().add(u4);

		JLabel u5 = new JLabel("\u4E94");
		u5.setBounds(x+124, y+10, 18, 15);
		getContentPane().add(u5);

		JLabel u6 = new JLabel("\u516D");
		u6.setBounds(x+147, y+10, 18, 15);
		getContentPane().add(u6);

		JLabel u7 = new JLabel("\u65E5");
		u7.setBounds(x+170, y+10, 18, 15);
		getContentPane().add(u7);
		
		for (int i = 0; i < jrb.length; i++) {
			jrb[i] = new JRadioButton();
			jrb[i].setBackground(new Color(240, 255, 255));
			//jrb[i].addActionListener(e -> {});
			if ((sheet & (1 << i)) > 0) {
				jrb[i].setSelected(true);
			}
			jrb[i].setBounds(x+28 + (i / 3) * 23, y+23 + (i % 3) * 25, 21, 23);
			getContentPane().add(jrb[i]);
		}
	}
	
	void addChooser(int x,int y, JRadioButton[] jrb) {
		addChooser(x, y, jrb, 0);
	}
	int getValue() {
		if(jrb == null) {
			return 0;
		}
		int r = 0;
		for(int i =0; i < jrb.length; i++) {
			if(jrb[i].isSelected()) {
				r += 1 << i;
			}
		}
		return r;
	}
	
	void setValue(int sheet) {
		for (int i = 0; i < jrb.length; i++) {
			if ((sheet & (1 << i)) > 0) {
				jrb[i].setSelected(true);
			}else
				jrb[i].setSelected(false);
		}
	}
	
}

class MemberCheckBox extends JCheckBox {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2906630319609193296L;
	private Member member;

	MemberCheckBox(Member member) {
		super();
		this.member = member;
	}

	Member getMember() {
		return member;
	}

	static Set<Member> convertToMemberSet(Set<MemberCheckBox> source) {
		HashSet<Member> s = new HashSet<>();
		Iterator<MemberCheckBox> i = source.iterator();
		while (i.hasNext()) {
			s.add(i.next().getMember());
		}
		return s;
	}

	static Set<MemberCheckBox> convertToCheckBoxSet(Set<? extends Member> set) {
		HashSet<MemberCheckBox> s = new HashSet<>();
		Iterator<? extends Member> i = set.iterator();
		while (i.hasNext()) {
			s.add(new MemberCheckBox(i.next()));
		}
		return s;
	}
}