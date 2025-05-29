package output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;

public class Log {
	private static Log instance;
	private File file;
	private PrintStream logOut;
	
	private Log() throws IOException {
		file = new File(System.getProperty("user.dir")+"\\temp\\log.log");
		boolean flag = !(file.exists()&&file.canWrite());
		if(flag) {
			System.out.println("new");
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file, true);
		logOut = new PrintStream(fos);
		if(file.length()!=0) {
			logOut.println("=============================");
			logOut.println();
		}
		logOut.println(LocalDateTime.now());
	}
	public static Log getInstance() throws IOException {
		if(instance == null) {
			instance = new Log();
		}
		return instance;
	}

	public PrintStream getOut() {
		return logOut;
	}
}
