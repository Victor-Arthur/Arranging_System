package output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import base.MemberSheet;

public class HistoryWritter {
	private static HistoryWritter instance;
	private File file;
	private HistoryWritter(){
		file = new File(System.getProperty("user.dir")+"\\temp\\history.vic");
		
	}
	
	public static HistoryWritter getInstance() {
		return instance==null?new HistoryWritter():instance;
	}
	public synchronized boolean writeHistoryList(Vector<MemberSheet> list){
		try {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
