package input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;

import base.MemberSheet;

public class HistoryReader {
	private static HistoryReader instance;
	private File file;
	private HistoryReader(){
		file = new File(System.getProperty("user.dir")+"\\temp\\history.vic");
	}
	public static HistoryReader getInstance() {
		return instance==null?new HistoryReader():instance;
	}
	public synchronized Vector<MemberSheet> getHistoryList(){
		if((!file.exists())||(file.length()==0))
			return new Vector<>();
		Vector<MemberSheet> m = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			m = (Vector<MemberSheet>) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return m;
	}
}
