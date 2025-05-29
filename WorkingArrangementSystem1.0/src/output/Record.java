package output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import base.Member;

public class Record {
	private Record() {}
	public static void saveMembers(HashSet<Member> members) throws IOException {
		File af = new File(System.getProperty("user.dir")+"\\temp\\Data.vic");
		FileOutputStream fos = new FileOutputStream(af);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(members);
		oos.close();
	}
}
