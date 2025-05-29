package ui;


import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFrame;

import process.Core;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;

import base.Member;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.ListSelectionModel;
import java.awt.Color;

class MannualModifyDialog extends JDialog {

	/**
	 * Launch the application.
	 */
	private final static int WIDTH = 200;
	private final static int HEIGHT = 200;
	private JList<String> list;
	private ArrayList<Member> members;
	private boolean state = true;
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					MannualModifyDialog dialog = new MannualModifyDialog(null,null,0,300,300);
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
	public MannualModifyDialog(MainWindow frame,Core core,int time,int x,int y) {
		super(frame,true);
		getContentPane().setBackground(new Color(240, 255, 255));
		this.setSize(WIDTH, HEIGHT);
		this.setLocation(x, y);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/temp.png")));
		setUndecorated(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JButton exit = new JButton("\u7ed3\u675f");
		exit.setBounds(133, 138, 57, 23);
		exit.addActionListener(e->{
			MannualModifyDialog.this.dispose();
		});
		getContentPane().add(exit);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(22, 12, 102, 149);
		getContentPane().add(scrollPane);
		
		list = new JList<>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		members = new ArrayList<>();
		Set<? extends Member> set = core.getMembers();
		Iterator<? extends Member> i = set.iterator();
		while(i.hasNext()) {
			Member m = i.next();
			if((m.getSpareTime()&time)>0) {
				members.add(m);
			}
		}
		ArrayList<String> names = getFromMembers(members);
		ListModel<String> jListModel = new DefaultComboBoxModel<>(names.toArray(new String[names.size()]));
		list.setModel(jListModel);
		scrollPane.setViewportView(list);
		
		JCheckBox viewAll = new JCheckBox("\u663E\u793A\u5168\u90E8\u4EBA\u5458");
		viewAll.setBackground(new Color(240, 255, 255));
		viewAll.addActionListener(e->{
			//System.out.println(viewAll.isSelected());
			members = new ArrayList<>();
			Set<? extends Member> setM = core.getMembers();
			Iterator<? extends Member> il = setM.iterator();
			while (il.hasNext()) {
				Member m = il.next();
				if (viewAll.isSelected() || (m.getSpareTime() & time) > 0) {
					members.add(m);
				}
			}
			ArrayList<String> namesA = getFromMembers(members);
			ListModel<String> jListModelA = new DefaultComboBoxModel<>(namesA.toArray(new String[namesA.size()]));
			list.setModel(jListModelA);
			MannualModifyDialog.this.setVisible(true);
			
		});
		viewAll.setBounds(22, 171, 103, 23);
		getContentPane().add(viewAll);
		
		JButton confirm = new JButton("\u786E\u5B9A");
		confirm.setBounds(133, 105, 57, 23);
		confirm.addActionListener(e->{
			int r = list.getSelectedIndex();
			if(r < 0) {
				JOptionPane.showMessageDialog(MannualModifyDialog.this, "请选定需要修改的人员。","提示",JOptionPane.WARNING_MESSAGE);
				return;
			}
			Member m = members.get(r);
			if(core.getByTime(time).contains(m)) {
				int re = JOptionPane.showConfirmDialog(MannualModifyDialog.this, "存在排班，是否删除？","提示",JOptionPane.YES_NO_OPTION);
				if(re == JOptionPane.YES_OPTION) {
					core.update(m, -time, true);
					state = false;
				}else {
					return;
				}
			}else {
				Core.Status status = core.update(m, time, false);
				int re = 0;
				switch(status) {
					case NORMAL:
						JOptionPane.showMessageDialog(MannualModifyDialog.this, "操作成功！","提示",JOptionPane.INFORMATION_MESSAGE);
						state = false;
						break;
					case CLASH:
						re = JOptionPane.showConfirmDialog(MannualModifyDialog.this, "人员不空闲，是否安排？","提示",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
						if(re == JOptionPane.YES_OPTION) {
							core.update(m, time, true);
							state = false;
							break;
						}
						return;
					case REPETITION:
						re = JOptionPane.showConfirmDialog(MannualModifyDialog.this, "人员已有排班，是否重复安排？","提示",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
						if(re == JOptionPane.YES_OPTION) {
							core.update(m, time, true);
							state = false;
							break;
						}
						return;
					case OCCUPIED:
						JOptionPane.showMessageDialog(MannualModifyDialog.this, "人员已安排！","提示",JOptionPane.WARNING_MESSAGE);
					default:
						break;
				}
			}
			frame.flush();
			MannualModifyDialog.this.dispose();
		});
		getContentPane().add(confirm);

	}
	
	boolean getState() {
		return state;
	}
	
	private static ArrayList<String> getFromMembers(ArrayList<Member> members){
		ArrayList<String> r = new ArrayList<>(members.size());
		for(Member m: members) {
			r.add(m.getName());
		}
		return r;
	}
}
