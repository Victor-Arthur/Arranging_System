package process;
import java.util.*;

import base.Member;
public interface Input {
	public Set<? extends base.Member> getMemberSet();
}

class MemberPool{
	private Input source;
	private Set<? extends base.Member> pool;
	private MemberPool(Input source) {
		this.source = source;
	}
	static MemberPool getPool(Input source) {
		MemberPool p = new MemberPool(source);
		p.flush();
		return p;
	}
	void flush() {
		pool = source.getMemberSet();
	}
	base.Member getByName(String name) {
		Iterator<? extends base.Member> i = pool.iterator();
		while(i.hasNext()) {
			base.Member m = i.next();
			if(m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}
	base.Member getById(String id) {
		Iterator<? extends base.Member> i = pool.iterator();
		while(i.hasNext()) {
			base.Member m = i.next();
			if(m.getId().equals(id.trim().toUpperCase())) {
				return m;
			}
		}
		return null;
	}
	base.Member[] getByTags(Member.Tag...tags){
		ArrayList<Member> a = new ArrayList<>();
		Iterator<? extends base.Member> i = pool.iterator();
		while(i.hasNext()) {
			base.Member m = i.next();
			if(m.hasTag(tags)) {
				a.add(m);
			}
		}
		return (Member[]) a.toArray(new Member[a.size()]);
	}
	base.Member[] getBySpareTime(int spare){
		ArrayList<Member> a = new ArrayList<>();
		Iterator<? extends base.Member> i = pool.iterator();
		while(i.hasNext()) {
			base.Member m = i.next();
			if((m.getSpareTime()&spare)>0) {
				a.add(m);
			}
		}
		return (Member[]) a.toArray(new Member[a.size()]);
	}
	boolean contains(Member m) {
		return pool.contains(m);
	}
	Set<? extends Member> getAll(){
		return this.pool;
	}
	
}