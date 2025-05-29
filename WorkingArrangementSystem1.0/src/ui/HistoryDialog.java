package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import base.MemberSheet;
import input.HistoryReader;
import output.HistoryWritter;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.ScrollPaneConstants;
import javax.swing.ListSelectionModel;

public class HistoryDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4425839542475913008L;
	/**
	 * Launch the application.
	 */
	private final static int WIDTH = 400;
	private final static int HEIGHT = 600;
	private Coordination point = new Coordination();
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					HistoryDialog dialog = new HistoryDialog(null);
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
	public HistoryDialog(MainWindow frame) {
		super(frame,true);
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
		
		
		
		JButton delete = new JButton("\u5220\u9664\u8BB0\u5F55");
		delete.setBounds(153, 567, 89, 23);
		delete.setVisible(frame.getAuth());
		getContentPane().add(delete);
		
		JButton exit = new JButton("\u9000\u51FA");
		exit.setBounds(276, 567, 93, 23);
		exit.addActionListener(e->{
			HistoryDialog.this.dispose();
		});
		getContentPane().add(exit);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 69, 380, 475);
		getContentPane().add(scrollPane);
		
		JList<String> list = new JList<>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Vector<MemberSheet> data = HistoryReader.getInstance().getHistoryList();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
		String [] str = new String[data.size()];
		
		for(int i = 0; i < str.length;i++) {
			str[i] = (i+1)+"."+dtf.format((data.get(i).getModifiedTime()));
		}
		
		ListModel<String> jListModel = new DefaultComboBoxModel<>(str);
		list.setModel(jListModel);
		scrollPane.setViewportView(list);
		
		JButton check = new JButton("\u67E5\u770B");
		check.setBounds(30, 567, 93, 23);
		check.addActionListener(e->{
			int r = list.getSelectedIndex();
			if(r < 0) {
				JOptionPane.showMessageDialog(HistoryDialog.this, "请选定需要查看的记录。","提示",JOptionPane.WARNING_MESSAGE);
				return;
			}
			EventQueue.invokeLater(
					()->{
						MemberDialog dialog = new MemberDialog(HistoryDialog.this,data.get(r));
						dialog.setVisible(true);
					}
				);
		});
		getContentPane().add(check);
		
		delete.addActionListener(e->{
			int r = list.getSelectedIndex();
			if(r < 0) {
				JOptionPane.showMessageDialog(HistoryDialog.this, "请选定需要删除的记录。","提示",JOptionPane.WARNING_MESSAGE);
				return;
			}
			int re = JOptionPane.showConfirmDialog(HistoryDialog.this, "确认要删除吗？","删除",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			if(re !=JOptionPane.YES_OPTION) {
				return;
			}
			data.remove(r);
			String [] str1 = new String[data.size()];
			
			for(int i = 0; i < str1.length;i++) {
				str1[i] = dtf.format((data.get(i).getModifiedTime()));
			}
			
			ListModel<String> jListModel1 = new DefaultComboBoxModel<>(str1);
			list.setModel(jListModel1);
			HistoryWritter.getInstance().writeHistoryList(data);
			HistoryDialog.this.setVisible(true);
			JOptionPane.showMessageDialog(HistoryDialog.this, "删除成功！","提示",JOptionPane.INFORMATION_MESSAGE);
		});
		
		JLabel title = new JLabel("\u5386\u53F2\u8BB0\u5F55");
		title.setBounds(153, 25, 98, 29);
		title.setFont(MyFont.getFont(Font.PLAIN, 24));
		title.setForeground(MyFont.getColor());
		getContentPane().add(title);

	}
}
