package ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import base.Member;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

class ModifyDialog extends SpareDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 503331494943766938L;
	/**
	 * 
	 */

	private static final int WIDTH=300;
	private static final int HEIGHT=280;
	private Coordination point = new Coordination();
	private JTextField name;
	private JTextField id;
	private JList<String> work;
	private boolean flag = false;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			ModifyDialog dialog = new ModifyDialog(null,null);
//			
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public ModifyDialog(JDialog dialog, Member member) {
		super(dialog,0,0);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		this.setSize(WIDTH, HEIGHT);
		this.setLocation((width - WIDTH) / 2, (height - HEIGHT) / 2);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/temp.png")));
		
		//this.addChooser(0, 0, jrb);
		
		JButton exit = new JButton("\u53D6\u6D88");
		exit.setBounds(190, 245, 57, 23);
		exit.addActionListener(e->{
			ModifyDialog.this.dispose();
		});
		this.getContentPane().add(exit);
		
		JButton commit = new JButton("\u63d0\u4ea4");
		commit.setBounds(40, 245, 57, 23);
		commit.addActionListener(e->{
			int [] t = work.getSelectedIndices();
			member.removeAllTags();
			if(t.length > 0) {
				Member.Tag[] ts = new Member.Tag[t.length];
				for (int j = 0; j < ts.length; j++) {
					switch (t[j]) {
					case 0:
						ts[j] = Member.Tag.ADMIN;
						break;
					case 1:
						ts[j] = Member.Tag.THURSDAY;
						break;
					default:
						ts[j] = Member.Tag.NONE;
					}
				}
				member.addTags(ts);
			}
			member.setSpareTime(getValue());
			
			JOptionPane.showMessageDialog(ModifyDialog.this, "修改成功！","提示",JOptionPane.INFORMATION_MESSAGE);
			flag = true;
			ModifyDialog.this.dispose();
			//System.out.println(mem.getName()+mem.getId()+mem.getSpareTime()+mem.getTags()[0]);
		});
		getContentPane().add(commit);
		
		JButton advanced = new JButton("\u9ad8\u7ea7");
		advanced.setBounds(115, 245, 57, 23);
		advanced.addActionListener(e->{
			String str = JOptionPane.showInputDialog(ModifyDialog.this,"请输入：",getValue()+"");
			if(str==null||str.length()==0) {
				return;
			}
			try {
				int r = Integer.valueOf(str.trim());
				if(r<0) {
					throw new Exception();
				}
				setValue(r);
			}catch(Exception e1) {
				JOptionPane.showMessageDialog(ModifyDialog.this, "请输入正整数！","提示",JOptionPane.ERROR_MESSAGE);
			}
				
			ModifyDialog.this.setVisible(true);
		});
		getContentPane().add(advanced);
		
		JLabel nameL = new JLabel("\u59D3\u540D\uFF1A");
		nameL.setFont(MyFont.getFont(Font.PLAIN, 18));
		nameL.setForeground(MyFont.getColor());
		nameL.setBounds(15, 21, 57, 31);
		getContentPane().add(nameL);
		
		name = new JTextField(member.getName());
		name.setBounds(62, 27, 190, 21);
		name.setEditable(false);
		getContentPane().add(name);
		name.setColumns(10);
		
		JLabel idL = new JLabel("\u5B66\u53F7\uFF1A");
		idL.setFont(MyFont.getFont(Font.PLAIN, 18));
		idL.setForeground(MyFont.getColor());
		idL.setBounds(15, 62, 57, 31);
		getContentPane().add(idL);
		
		id = new JTextField(member.getId());
		id.setBounds(62, 68, 190, 21);
		id.setEditable(false);
		getContentPane().add(id);
		id.setColumns(10);
		
		JLabel workL = new JLabel("\u7279\u6B8A\u804C\u4F4D\uFF1A");
		workL.setFont(MyFont.getFont(Font.PLAIN, 18));
		workL.setForeground(MyFont.getColor());
		workL.setBounds(15, 103, 96, 31);
		getContentPane().add(workL);
		
		JScrollPane jsp = new JScrollPane();
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setSize(66, 40);
		jsp.setLocation(100, 105);
		
		work = new JList<>();
		ListModel<String> jListModel =  new DefaultComboBoxModel<>(new String[] { "管理员", "医生助理","无" });
		work.setModel(jListModel);
		int [] selected = new int[member.getTags().length];
		for(int k = 0; k < selected.length; k++) {
			switch(member.getTags()[k]) {
				case ADMIN:
					selected[k] = 0;
					break;
				case THURSDAY:
					selected[k] = 1;
					break;
				default:
					selected[k] = 2;
			}
		}
		work.setSelectedIndices(selected);
		jsp.setViewportView(work);
		getContentPane().add(jsp);
		
		JLabel spareL = new JLabel("\u7A7A\u95F2\u65F6\u6BB5\uFF1A");
		spareL.setFont(MyFont.getFont(Font.PLAIN, 18));
		spareL.setForeground(MyFont.getColor());
		spareL.setBounds(15, 144, 96, 29);
		getContentPane().add(spareL);
		
		this.addChooser(85, 142, jrb,member.getSpareTime());
	
		MainWindow.addDraggingFunction(this, point);
	}
	
	boolean getResult() {
		return flag;
	}
	
}
