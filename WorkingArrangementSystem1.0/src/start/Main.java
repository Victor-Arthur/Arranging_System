package start;
import ui.MainWindow;
import ui.WelcomeWindow;

import process.Core;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.Vector;

import base.MemberSheet;
import input.FileReader;
import input.HistoryReader;
public class Main {
	static {try {
		System.setErr(output.Log.getInstance().getOut());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vector<MemberSheet> v = HistoryReader.getInstance().getHistoryList();
		Core core = v.size()==0?new Core(new FileReader()):new Core(new FileReader(),v.get(v.size()-1));

		int r = startWelcome(core);
		if(r == -1)
			System.exit(0);
		
		EventQueue.invokeLater(
			()->{
				try {
					WelcomeWindow.getInstance().terminate();
					MainWindow frame = new MainWindow(core, WelcomeWindow.getInstance().getUser(),r == 1);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		);
	}
	/**
	 *@param core Core being used. 
	 *@return 1 Administrator mode.<br/>
	 *0 User mode.<br/>
	 *-1 Program should be terminated.
	 *@author Victor
	*/
	public static int startWelcome(Core core) {
		while (true) {
			String str = WelcomeWindow.getInstance().getId();
			if (str.length()==0) {
				return -1;
			}
			int r = WelcomeWindow.getInstance().checkId(str, core);
			if (r == -1) {
				WelcomeWindow.getInstance().notExist();
				continue;
			}else if(r == 1){
				int r1 = WelcomeWindow.getInstance().confirmed();
				if(r1 == 1) {
					return 1;
				}else if(r1 == 0){
					return 0;
				}else {
					continue;
				}
			}else {
				return 0;
			}
		}
	}
}
