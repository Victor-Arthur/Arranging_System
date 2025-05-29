package base;
import java.time.LocalDateTime;
import java.util.*;
public final class MemberSheet implements java.io.Serializable {

	private static final long serialVersionUID = -7795883608325853755L;
	private HashMap<Member,Integer> sheet;
	private String note = "";
	private LocalDateTime modifiedTime;
	private MemberSheet(HashMap<Member,Integer> sheet) {
		this.sheet = new HashMap<>();
		Iterator<Member> i = sheet.keySet().iterator();
		while(i.hasNext()) {
			Member m = i.next();
			this.sheet.put(m,sheet.get(m));
		}
		modifiedTime = LocalDateTime.now();
	}
	public static MemberSheet createMemberSheet(HashMap<Member,Integer> sheet) {
		return new MemberSheet(sheet);
	}
	public HashMap<Member, Integer> getSheet() {
		return new HashMap<>(sheet);
	}
	public LocalDateTime getModifiedTime() {
		return  modifiedTime;
	}
	public void setNote(String note) {
		this.note = note==null?"":note;
	}
	public String getNote() {
		return note;
	}
	
}
