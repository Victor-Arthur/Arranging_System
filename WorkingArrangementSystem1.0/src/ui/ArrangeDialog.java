package ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.format.DateTimeFormatter;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JSpinner;

import base.Schedule;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SpinnerNumberModel;

public class ArrangeDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1961191357460985616L;
	private static Schedule schedule 
							= Schedule.getScheduleByArray(new int[][] {{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}});
	/**
	 * Launch the application.
	 */
	private final static int WIDTH = 472;
	private final static int HEIGHT = 354;
	private Coordination point = new Coordination();
	private JSpinner spinners[] = new JSpinner[21];
	private static File file;
	
	static {
		try {
			file = new File(System.getProperty("user.dir")+"\\temp\\arrange.vic");
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			schedule = (Schedule)ois.readObject();
			ois.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ArrangeDialog dialog = new ArrangeDialog(null);
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
	public ArrangeDialog(JFrame frame) {
		super(frame,true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		this.setSize(WIDTH, HEIGHT);
		this.setLocation((width - WIDTH) / 2, (height - HEIGHT) / 2);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/temp.png")));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setUndecorated(true);
		MainWindow.addDraggingFunction(this, point);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JButton exit = new JButton("\u5173\u95ED");
		exit.addActionListener(e->{
			ArrangeDialog.this.dispose();
		});
		exit.setBounds(371, 305, 66, 23);
		panel.add(exit);
		
		synchronized (schedule) {
			

			for (int i = 0; i < 21; i++) {
				spinners[i] = new JSpinner();
				spinners[i].setModel(new SpinnerNumberModel(0, 0, null, 1));
				spinners[i].setValue(Integer.valueOf(schedule.getByDate(i)));
				spinners[i].setBounds(75 + (i / 3) * 50 + 3 * (i / 3) / 4 + 8 * (i / 3) / 4, 135 + (i % 3) * 50, 36, 22);
				panel.add(spinners[i]);
			}
		}
		
		JButton confirm = new JButton("\u786E\u8BA4");
		confirm.setBounds(295, 305, 66, 23);
		confirm.addActionListener(e->{
			int[][] r = new int[][] {{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}};
			for(int i = 0; i < 21; i++) {
				r[i%3][i/3] = (int)spinners[i].getValue(); 
			}
			schedule = Schedule.getScheduleByArray(r);
			try {
				FileOutputStream fos = new FileOutputStream(file,false);
				ObjectOutputStream oos =new ObjectOutputStream(fos);
				oos.writeObject(schedule);
				oos.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ArrangeDialog.this.dispose();
		});
		panel.add(confirm);
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("制定时间："+"yyyy年MM月dd日 HH:mm:ss");
		JLabel label = new JLabel(dtf.format(schedule.getModifiedTime()));
		label.setFont(MyFont.getFont(Font.PLAIN, 14));
		label.setForeground(MyFont.getColor());
		label.setBounds(32, 309, 253, 15);
		panel.add(label);
		
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(ArrangeDialog.class.getResource("/resources/arrangementbg.png")));
		lblNewLabel.setBounds(0, 0, 472, 354);
		panel.add(lblNewLabel);
		
	}
	static void arrangeSchedule(JFrame frame) {

		EventQueue.invokeLater(() -> {
			try {
				ArrangeDialog dialog = new ArrangeDialog(frame);
				dialog.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
	}
	static Schedule getSchedule() {
		return schedule;
	}
}
