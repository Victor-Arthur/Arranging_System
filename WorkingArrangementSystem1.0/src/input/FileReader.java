package input;
import java.io.*;
import java.util.*;
import base.Member;

public class FileReader implements process.Input{
	private File file;
	private FileInputStream fis;
	{
		file = new File(System.getProperty("user.dir")+"\\temp\\Data.vic");
	}
	@Override
	public Set<? extends Member> getMemberSet() {
		// TODO Auto-generated method stub
		try {
			fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Set<Member> r = (Set<Member>) ois.readObject();
			return r;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static Set<? extends Member> getFromFile(File f) throws FileNotFoundException{
		HashSet<Member> mset = new HashSet<>();
		FileInputStream fiss = new FileInputStream(f);
		Scanner sca = new Scanner(fiss,"UTF-8");
		while(sca.hasNextLine()) {
			String str = sca.nextLine();
			String [] stu = str.trim().split(",");
			Member mem = Member.createMember(stu[0] ,stu[1].toUpperCase(),Integer.valueOf(stu[2]));
			mset.add(mem);
		}
		sca.close();
		return mset;
	}
//	public static void main(String [] args) {
//		File f = new File(System.getProperty("user.dir")+"\\testData.csv");
//		HashSet<Member> mset = new HashSet<>();
//		try {
//			FileInputStream fiss = new FileInputStream(f);
//			Scanner sca = new Scanner(fiss,"UTF-8");
//			while(sca.hasNextLine()) {
//				String str = sca.nextLine();
//				String [] stu = str.trim().split(",");
//				Member mem = Member.createMember(stu[0] ,stu[1].toUpperCase(),Integer.valueOf(stu[2]));
//				if(mem.getId().equals("U201910214")) {
//					mem.addTags(Member.Tag.ADMIN);
//				}
//				System.out.println(mem.getTags().length);
//				mset.add(mem);
//			}
//			File af = new File(System.getProperty("user.dir")+"\\Data.vic");
//			FileOutputStream fos = new FileOutputStream(af);
//			ObjectOutputStream oos = new ObjectOutputStream(fos);
//			oos.writeObject(mset);
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
