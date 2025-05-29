package base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;
import java.util.*;

public class Member implements java.io.Serializable,Comparable<Member>{
	
	private static HashMap<String,Member> innerPool = new HashMap<>();
	
	public enum Tag {
		MALE, FEMALE, ADMIN("\u7ba1\u7406\u5458"), THURSDAY("\u533b\u751f\u52a9\u7406"),NONE;
		private String describe;
		private Tag() {
			this("");
		}
		private Tag(String s) {
			this.describe = s;
		}
		@Override
		public String toString() {
			return describe;
		}
	}

	private static final long serialVersionUID = -2599366396413650530L;
	private String name;
	private String id;
	private HashSet<Tag> tags;
	private int spareTime = 0;
	private LocalDateTime modifiedTime;
	private Member(String name, String id, int spare,Tag...tag) {
		this.name = name;
		this.id = id.trim().toUpperCase();
		this.spareTime = spare;
		this.tags = new HashSet<>();
		this.modifiedTime = LocalDateTime.now();
		for (Tag e : tag)
			tags.add(e);
		Member.innerPool.put(this.id, this);
	}

	public final static Member createMember(String name, String id, int spare, Tag...tag) {
		if (innerPool.containsKey(id)) {
			Member m = innerPool.get(id);
			m.setName(name);
			m.setSpareTime(spare);
			m.removeAllTags();
			m.addTags(tag);
			return m;
		}
		return new Member(name, id, spare,tag);
	}

	public final static Member createMember(String name, String id, int spare) {
		return createMember(name, id, spare, new Tag[]{});
	}

	public final static Member createMember(String name, String id) {
		return createMember(name, id, 0, new Tag[]{});
	}

	public final String getName() {
		return name;
	}
	
	private final void setName(String name) {
		this.name = name;
		this.modifiedTime = LocalDateTime.now();
	}

	public final String getId() {
		return id;
	}

	public final Tag[] getTags() {
		return (Tag[]) tags.toArray(new Tag[tags.size()]);
	}
	
	public final boolean hasTag(Tag...t) {
		if (t.length == 0)
			return false;
		boolean f = true;
		for (Tag e:t)
			f = tags.contains(e)&&f;
		return f;
	}
	
	public synchronized final void addTags(Tag...t) {
		for (Tag e:t)
			tags.add(e);
		this.modifiedTime = LocalDateTime.now();
	}
	
	public synchronized final boolean removeTags(Tag...t) {
		if (t.length == 0)
			return false;
		boolean f = true;
		for (Tag e:t)
			f = tags.remove(e)&&f;
		this.modifiedTime = LocalDateTime.now();
		return f;
	}
	
	public synchronized final void removeAllTags() {
		this.tags.clear();
		this.modifiedTime = LocalDateTime.now();
	}
	
	public final int getSpareTime() {
		return spareTime;
	}

	public final void setSpareTime(int spareTime) {
		this.spareTime = spareTime;
		this.modifiedTime = LocalDateTime.now();
	}

	public LocalDateTime getLastModifyTime() {
		return this.modifiedTime;
	}


	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Member)) {
			return false;
		}
		Member m = (Member) o;
		return this.getId().equals(m.getId());
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		Member.innerPool.put(this.getId(), this);
	}
	
	public String[] getDescription() {
		ArrayList<String> l = new ArrayList<>();
		for(Tag t:getTags()) {
			if(t.toString().equals(""))
				continue;
			l.add(t.toString());
		}
		return l.toArray(new String[l.size()]);
	}
	
	public final int getTimes() {
		int times = 0;
		int p = spareTime;
		while(p != 0) {
			times += p%2;
			p = p>>>1;
		}
		return times;
	}
//	public static void main(String [] args) {
//		Member m = Member.createMember("zhx", "U201910214", 3);
//		System.out.println(m.getSpareTime());
//		System.out.println(m.getName());
//		Member m1 = Member.createMember("zh", "U201910214");
//		System.out.println(m1.getSpareTime());
//		System.out.println(m1.getName());
//		System.out.println(m.getLastModifyTime());
//	}

	@Override
	public int compareTo(Member o) {
		// TODO Auto-generated method stub
		return this.getTimes()-o.getTimes();
	}
}
