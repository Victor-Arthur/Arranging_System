package ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import base.Member;
import base.MemberSheet;
import input.HistoryReader;
import output.HistoryWritter;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class MemberDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8045599006936430256L;
	private final static int WIDTH = 800;
	private final static int HEIGHT = 450;
	private boolean state;
	private Coordination point = new Coordination();
	private MyTextArea[] textAreas = new MyTextArea[21];
	

	/**
	 * Create the dialog.
	 */
	
//	public static void main(String [] args) {
//		EventQueue.invokeLater(
//			()->{
//				new MemberDialog(null,null,true).setVisible(true);
//			}
//		);
//	}
	public MemberDialog(MainWindow frame, MemberSheet sheet, boolean authorization) {
		super(frame, true);
		state = authorization;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		this.setSize(WIDTH, HEIGHT);
		this.setLocation((width - WIDTH) / 2, (height - HEIGHT) / 2);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/temp.png")));
		this.setUndecorated(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		getContentPane().add(panel, BorderLayout.CENTER);
		
		Color color = new Color(65,83,128);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
		
		ArrayList<String> [] list  = MainWindow.resolveSheet(sheet);
		for(int i = 0; i < 21; i++) {
			JScrollPane js = new JScrollPane();
			js.setBounds(106+(i/3)*91+i/9, 115+(i%3)*80, 85, 76-2*((i%3)/2));
			textAreas[i] = new MyTextArea(list[i]);
			textAreas[i].setEditable(false);
			textAreas[i].setLineWrap(true);
			js.setViewportView(textAreas[i]);
			panel.add(js);
		}
		
		
		JLabel ctime = new JLabel("记录生成时间："+dtf.format(sheet.getModifiedTime()));
		ctime.setFont(MyFont.getFont(Font.PLAIN, 16));
		ctime.setForeground(color);
		ctime.setLocation(37, 407);
		ctime.setSize(384, 36);
		panel.add(ctime);
		
		JButton exit = new JButton("\u5173\u95ED\u7A97\u53E3");
		exit.setBounds(660, 414, 93, 23);
		exit.addActionListener(
			e->{
				MemberDialog.this.dispose();
			}
		);
		
		JButton save = new JButton("\u4FDD\u5B58\u8BB0\u5F55");
		save.setBounds(557, 414, 93, 23);
		save.setVisible(state);
		save.addActionListener(
			e->{
				Vector<MemberSheet> tempList = HistoryReader.getInstance().getHistoryList();
				sheet.setNote(frame.getNote());
				tempList.add(sheet);
				HistoryWritter.getInstance().writeHistoryList(tempList);
				JOptionPane.showMessageDialog(MemberDialog.this, "保存成功！","提示",JOptionPane.INFORMATION_MESSAGE);
				frame.saveState = true;
			}	
				
		);
		
		panel.add(save);
		panel.add(exit);
		
		JLabel background = new JLabel("");
		ImageIcon image = new ImageIcon(MainWindow.class.getResource("/resources/dialogbg.png"));
		image.setImage(image.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH));
		background.setSize(WIDTH, HEIGHT);
		background.setIcon(image);
		panel.add(background);
		
		MainWindow.addDraggingFunction(this,this.point);

	}
	public MemberDialog(JDialog dialog, MemberSheet sheet) {
		super(dialog,true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		this.setSize(WIDTH, HEIGHT);
		this.setLocation((width - WIDTH) / 2, (height - HEIGHT) / 2);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/temp.png")));
		this.setUndecorated(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		getContentPane().add(panel, BorderLayout.CENTER);
		
		Color color = new Color(65,83,128);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
		
		ArrayList<String> [] list  = MainWindow.resolveSheet(sheet);
		for(int i = 0; i < 21; i++) {
			JScrollPane js = new JScrollPane();
			js.setBounds(106+(i/3)*91+i/9, 115+(i%3)*80, 85, 76-2*((i%3)/2));
			textAreas[i] = new MyTextArea(list[i]);
			textAreas[i].setEditable(false);
			textAreas[i].setLineWrap(true);
			js.setViewportView(textAreas[i]);
			panel.add(js);
		}
		
		
		JLabel ctime = new JLabel("记录生成时间："+dtf.format(sheet.getModifiedTime()));
		ctime.setFont(MyFont.getFont(Font.PLAIN, 16));
		ctime.setForeground(color);
		ctime.setLocation(37, 407);
		ctime.setSize(384, 36);
		panel.add(ctime);
		
		JButton exit = new JButton("\u5173\u95ED\u7A97\u53E3");
		exit.setBounds(660, 414, 93, 23);
		exit.addActionListener(
			e->{
				MemberDialog.this.dispose();
			}
		);
		panel.add(exit);
		
		JLabel background = new JLabel("");
		ImageIcon image = new ImageIcon(MainWindow.class.getResource("/resources/dialogbg.png"));
		image.setImage(image.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH));
		background.setSize(WIDTH, HEIGHT);
		background.setIcon(image);
		panel.add(background);
		
		MainWindow.addDraggingFunction(this,this.point);
	}
}

class MyTextArea extends JTextArea{
	/**
	 * 
	 */
	private static final long serialVersionUID = -454384869314379581L;
	private int count = 0;
	MyTextArea(){
		super();
	}
	MyTextArea(ArrayList<String> list){
		super();
		count = list.size();
		String text = "";
		for(int i = 0; i < count; i++) {
			if(i == 0)
				text = list.get(i);
			else
				text = text + "\n" + list.get(i);
		}
		this.setText(text);
	}
	void append(int n) {
		count = (count+n)<0?0:count+n;
	}
	int getCount() {
		return count;
	}
}
