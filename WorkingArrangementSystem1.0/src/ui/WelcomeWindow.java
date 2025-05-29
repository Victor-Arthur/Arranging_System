package ui;
import base.Member;
import process.Core;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JPanel;
import javax.swing.UIManager;
import java.awt.Toolkit;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JPasswordField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Set;

public final class WelcomeWindow{
	private static WelcomeWindow instance;
	private WelcomeWindow0 window;
	private boolean flag = true;
	private Member user;
	private WelcomeWindow(){
		window = new WelcomeWindow0();
	}
	public static WelcomeWindow getInstance() {
		if(instance == null) {
			instance = new WelcomeWindow();
			return instance;
		}else {
			return instance;
		}
	}
	public void terminate() {
		window.dispose();
	}
	public String getId() {
		EventQueue.invokeLater(
			()->{
				try {
					if(WelcomeWindow.this.flag) {
						WelcomeWindow.this.window.init();
						WelcomeWindow.this.window.setVisible(true);
						WelcomeWindow.this.flag = false;
					}
					WelcomeWindow.this.window.erase();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		);
		synchronized(window.getLock()) {
			try {
				window.getLock().wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return window.getId();
	}
	public void notExist() {
		JOptionPane.showMessageDialog(window, "用户不存在！","提示",JOptionPane.ERROR_MESSAGE);
	}
	public int confirmed() {
		int r1 = JOptionPane.showConfirmDialog(window, "是否以管理员模式进入系统？","提示",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
		if(r1 == JOptionPane.YES_OPTION) {
			return 1;
		}else if(r1 == JOptionPane.NO_OPTION){
			return 0;
		}else {
			return -2;
		}
	}
	public Member getUser() {
		return user;
	}
	/**
	 *@param id Id from front.
	 *@param core Core being used. 
	 *@return 1 <code>id</code> can be identified as an administrator.<br/>
	 *0 <code>id</code> can be identified as a user.<br/>
	 *-1 <code>id</code> doesn't exist.
	 *@author Victor
	*/
	public int checkId(String id,Core core) {
		Set<? extends Member> mset = core.getMembers();
		Iterator<? extends Member> i = mset.iterator();
		while(i.hasNext()) {
			Member m = i.next();
			if(id.equals(m.getId())) {
				user = m;
				return m.hasTag(Member.Tag.ADMIN)?1:0;
			}
		}
		return -1;
	}
}

class WelcomeWindow0 extends JDialog {

	/**
	 * 
	 */
	private final static int WIDTH = 400;
	private final static int HEIGHT = 300;
	
	private Coordination point = new Coordination();
	
	private static final long serialVersionUID = -8996996982790044706L;
	private JPanel panel;
	private JPasswordField passwordField;
	private String id;
	
	private final Object lock = new Object();
	static {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	WelcomeWindow0() {
		
	}
	
	final Object getLock() {
		return lock;
	}

	final String getId() {
		return id;
	}
	
	final void erase() {
		passwordField.setText("");
	}
	void init() {
		MainWindow.addDraggingFunction(this,this.point);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBackground(Color.WHITE);
		setResizable(false);
		setTitle("\u65B0\u5FC3\u6392\u73ED\u7CFB\u7EDF2.0");
		setIconImage(Toolkit.getDefaultToolkit().getImage(WelcomeWindow0.class.getResource("/resources/temp.png")));
		this.setUndecorated(true);
		//setBounds(100, 100, 450, 300);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		this.setSize(WIDTH, HEIGHT);
		this.setLocation((width - WIDTH) / 2, (height - HEIGHT) / 2);
		
		panel = new JPanel();
		panel.setLayout(null);
		
		JButton exit = new JButton("\u9000\u51FA\u7CFB\u7EDF");
		exit.addActionListener(
			e -> {
				WelcomeWindow0.this.id = "";
				synchronized(WelcomeWindow0.this.lock) {
					WelcomeWindow0.this.lock.notifyAll();
				}
			}
		);
		exit.setBounds(237, 199, 93, 23);
		panel.add(exit);
		
		JButton login = new JButton("\u8FDB\u5165\u7CFB\u7EDF");
		login.addActionListener(
			e -> {
				
				char[] c = WelcomeWindow0.this.passwordField.getPassword();
				if (c == null || c.length == 0) {
					JOptionPane.showMessageDialog(WelcomeWindow0.this,"请输入访问者学号！","提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				WelcomeWindow0.this.id = new String(WelcomeWindow0.this.passwordField.getPassword()).toUpperCase();
				synchronized (WelcomeWindow0.this.lock) {
					WelcomeWindow0.this.lock.notifyAll();
				}
				
			}
		);
		login.setBounds(72, 199, 93, 23);
		panel.add(login);
		
//		JLabel lblNewLabel = new JLabel("\u65B0\u5FC3\u52A9\u7406\u6392\u73ED\u7CFB\u7EDF");
//		lblNewLabel.setFont(new Font("方正颜真卿楷书 简繁", Font.PLAIN, 27));
//		lblNewLabel.setForeground(new Color(65, 83, 128));
//		lblNewLabel.setBounds(95, 48, 233, 54);
//		panel.add(lblNewLabel);
		
		passwordField = new JPasswordField();
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER) {
					login.doClick();
				}
			}
		});
		passwordField.setBounds(135, 123, 173, 21);
		panel.add(passwordField);
		
//		JLabel lblNewLabel_1 = new JLabel("\u5B66\u53F7");
//		lblNewLabel_1.setFont(new Font("方正颜真卿楷书 简繁", Font.PLAIN, 16));
//		lblNewLabel_1.setForeground(new Color(65, 83, 128));
//		lblNewLabel_1.setBounds(95, 120, 33, 32);
//		panel.add(lblNewLabel_1);
		
		panel.setOpaque(false);
	
		this.setContentPane(panel);
		
		JLabel lblNewLabel_2 = new JLabel("");
		ImageIcon image = new ImageIcon(WelcomeWindow0.class.getResource("/resources/bg13.png"));
		image.setImage(image.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH));
		lblNewLabel_2.setIcon(image);
		lblNewLabel_2.setBounds(0, 0, WIDTH, HEIGHT);
		panel.add(lblNewLabel_2);
	}
}
