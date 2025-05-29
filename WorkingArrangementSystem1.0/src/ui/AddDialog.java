package ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Set;

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

class AddDialog extends SpareDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1501543138602534238L;
	private static final int WIDTH=300;
	private static final int HEIGHT=280;
	private Coordination point = new Coordination();
	private JTextField name;
	private JTextField id;
	private JList<String> work;
	private Member member;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			AddDialog dialog = new AddDialog(null,null);
//			
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public AddDialog(JDialog dialog, Set<Member> members) {
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
			AddDialog.this.dispose();
		});
		this.getContentPane().add(exit);
		
		JButton add = new JButton("\u589E\u52A0");
		add.setBounds(40, 245, 57, 23);
		add.addActionListener(e->{
			String n = name.getText().trim();
			String i = id.getText().toUpperCase().trim();
			Member mem;
			if(n.length() == 0 && i.length() == 0) {
				JOptionPane.showMessageDialog(AddDialog.this, "请输入姓名与学号！","提示",JOptionPane.WARNING_MESSAGE);
				return;
			}else if(n.length() == 0) {
				JOptionPane.showMessageDialog(AddDialog.this, "请输入姓名！","提示",JOptionPane.WARNING_MESSAGE);
				return;
			}else if(i.length() == 0) {
				JOptionPane.showMessageDialog(AddDialog.this, "请输入学号！","提示",JOptionPane.WARNING_MESSAGE);
				return;
			}else;
			int [] t = work.getSelectedIndices();
			if(t.length == 0) {
				 mem = Member.createMember(n, i, getValue());
				
			} else {
				Member.Tag[] ts = new Member.Tag[t.length];
				for (int j = 0; j < ts.length; j++) {
					switch (t[j]) {
					case 0:
						ts[j] = Member.Tag.ADMIN;
						break;
					case 1:
						ts[j] = Member.Tag.THURSDAY;
						break;
					}
				}
				mem = Member.createMember(n, i, getValue(), ts);
			}
			if (members!=null&&members.contains(mem)) {
				JOptionPane.showMessageDialog(AddDialog.this, "人员已存在！","提示",JOptionPane.INFORMATION_MESSAGE);
				AddDialog.this.dispose();
				return;
			}
			
			JOptionPane.showMessageDialog(AddDialog.this, "新增成功！","提示",JOptionPane.INFORMATION_MESSAGE);
			this.member = mem;
			AddDialog.this.dispose();
			//System.out.println(mem.getName()+mem.getId()+mem.getSpareTime()+mem.getTags()[0]);
		});
		getContentPane().add(add);
		
		JButton advanced = new JButton("\u9ad8\u7ea7");
		advanced.setBounds(115, 245, 57, 23);
		advanced.addActionListener(e->{
			String str = JOptionPane.showInputDialog(AddDialog.this,"请输入：",name.getText().trim()+((name.getText().trim().length()==0)?"NoName,":",")+id.getText().toUpperCase().trim()+((id.getText().trim().length()==0)?"NoId,":",")+getValue());
			if(str==null||str.length()==0)
				return;
			String[] s = str.trim().split(",");
			name.setText(s[0]);
			if(s.length>=2)
				id.setText(s[1].equalsIgnoreCase("NoId")?"":s[1].toUpperCase());
			if(s.length>=3)
				setValue(Integer.valueOf(s[2]));
			else
				setValue(0);
			AddDialog.this.setVisible(true);
		});
		getContentPane().add(advanced);
		
		JLabel nameL = new JLabel("\u59D3\u540D\uFF1A");
		nameL.setFont(MyFont.getFont(Font.PLAIN, 18));
		nameL.setForeground(MyFont.getColor());
		nameL.setBounds(15, 21, 57, 31);
		getContentPane().add(nameL);
		
		name = new JTextField();
		
		name.setBounds(62, 27, 190, 21);
		getContentPane().add(name);
		name.setColumns(10);
		
		JLabel idL = new JLabel("\u5B66\u53F7\uFF1A");
		idL.setFont(MyFont.getFont(Font.PLAIN, 18));
		idL.setForeground(MyFont.getColor());
		idL.setBounds(15, 62, 57, 31);
		getContentPane().add(idL);
		
		id = new JTextField();
		id.setBounds(62, 68, 190, 21);
		getContentPane().add(id);
		id.setColumns(10);
		
		JLabel workL = new JLabel("\u7279\u6B8A\u804C\u4F4D\uFF1A");
		workL.setFont(MyFont.getFont(Font.PLAIN, 18));
		workL.setForeground(MyFont.getColor());
		workL.setBounds(15, 103, 96, 31);
		getContentPane().add(workL);
		
		JScrollPane jsp = new JScrollPane();
		jsp.setSize(66, 36);
		jsp.setLocation(100, 105);
		
		work = new JList<>();
		ListModel<String> jListModel =  new DefaultComboBoxModel<>(new String[] { "管理员", "医生助理" });
		work.setModel(jListModel);
		jsp.setViewportView(work);
		getContentPane().add(jsp);
		
		JLabel spareL = new JLabel("\u7A7A\u95F2\u65F6\u6BB5\uFF1A");
		spareL.setFont(MyFont.getFont(Font.PLAIN, 18));
		spareL.setForeground(MyFont.getColor());
		spareL.setBounds(15, 144, 96, 29);
		getContentPane().add(spareL);
		
		this.addChooser(85, 142, jrb);
	
		MainWindow.addDraggingFunction(this, point);
	}
	Member getMember() {
		
			
		return member;

	}
	
}
