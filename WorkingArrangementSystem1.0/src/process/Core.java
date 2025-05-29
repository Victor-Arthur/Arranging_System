package process;
import java.util.*;
import base.*;
import static base.Pattern.Priority;
public class Core {
	
	public enum Status{
		NORMAL,REDUNDANCY,CLASH,REPETITION,OCCUPIED
	}
	private MemberPool pool;
	private HashMap<Member,Integer> position;
	
	{
		position = new HashMap<>();
	}
	
	public Core() {}
	public Core(Input input) {
		link(input);
	}
	public Core(Input input, MemberSheet init) {
		link(input);
		this.position = init.getSheet();
	}
	public void link(Input input) {
		this.pool = MemberPool.getPool(input);
		this.position = new HashMap<>();
	}
	private void check() {
		if (pool == null)
			throw new NoPoolException("No available member pool detected.");
		//System.out.println(pool.getAll().size());
	}
	
	public synchronized final Status update(Member m, int loc, boolean forced) {
		check();
		if (loc < 0) {
			if((position.get(m)&(-loc)) == 0) {
				position.remove(m);
			}else{
				position.replace(m, position.get(m)&(~(-loc)));
			}
			return Status.NORMAL;
		}
		
		if (loc == 0) {
			position.remove(m);
			return Status.NORMAL;
		}
		
		if ((loc&m.getSpareTime()) <= 0) {
			if (!forced) {
				return Status.CLASH;
			}
		}
		
		if (position.containsKey(m)) {
			if ((loc&position.get(m)) > 0) {
				return Status.OCCUPIED;
			}
			if (!forced) {
				return Status.REPETITION;
			}
		}
		if (position.containsKey(m)) {
			position.replace(m, position.get(m)|loc);
		}else {
			position.put(m, loc);
		}
		return Status.NORMAL;
	}
	
	public Set<? extends Member> getMembers(){
		check();
		return Collections.unmodifiableSet(pool.getAll());
	}
	
	public synchronized final MemberSheet generateSheet(){
		check();
		return MemberSheet.createMemberSheet(position);
	}
	
	
	public final void autoArrange(Schedule schedule,Pattern pattern) {
		// TODO
		AutoArrange auto = new AutoArrange(pool,schedule,pattern,position);
		HashMap<? extends Member,Integer> r = auto.generate();
		HashMap<Member,Integer> re = new HashMap<>();
		Iterator<? extends Member> i = r.keySet().iterator();
		while(i.hasNext()) {
			Member m = i.next();
			re.put(m,r.get(m));
		}
		this.position = re;
	}
	public final void autoArrange(Schedule schedule) {
		autoArrange(schedule,null);
	}
	
	public static int getTimes(int position) {
		int times = 0;
		int p = position;
		while(p != 0) {
			times += p%2;
			p = p>>>1;
		}
		return times;
	}
	
	public Set<? extends Member> getByTime(int time){
		HashSet<Member> r = new HashSet<>();
		Iterator<Member> i = position.keySet().iterator();
		while(i.hasNext()) {
			Member m = i.next();
			if((position.get(m)&time) > 0)
				r.add(m);
		}
		return r;
	}
//	public static void main(String [] args) {
//		Schedule s = Schedule.getScheduleByArray(new int[][] {{0,0,0,0,0,0,0},
//															  {0,0,0,3,0,0,0},
//															  {2,2,2,2,2,2,2}}) ;
//
//		FileReader fr = new FileReader();
//		Core core = new Core(fr);
//		core.autoArrange(s);
//		MemberSheet ms = core.generateSheet();
//		HashMap<Member,Integer> result = ms.getSheet();
//		Iterator<Member> i = result.keySet().iterator();
//		while(i.hasNext()) {
//			Member m = i.next();
//			System.out.println(m.getName()+"\t"+m.getId()+"\t"+Integer.toBinaryString(result.get(m)));
//		}
//		System.out.println(result.size());
//		
//		
//	}
}

class AutoArrange{
	private MemberPool pool;
	private Schedule schedule;
	private Pattern rule;
	private HashMap<? extends Member,Integer> origin; 
	AutoArrange(MemberPool pool,Schedule schedule){
		this(pool, schedule, null, null);
	}
	AutoArrange(MemberPool pool,Schedule schedule,HashMap<? extends Member,Integer> origin){
		this(pool, schedule, null, origin);
	}
	AutoArrange(MemberPool pool,Schedule schedule,Pattern rule){
		this(pool, schedule, rule, null);
	}
	AutoArrange(MemberPool pool,Schedule schedule,Pattern rule, HashMap<? extends Member,Integer> origin){
		this.pool = pool;
		this.schedule = schedule;
		this.rule = rule;
		this.origin = origin;
	}
	HashMap<? extends Member,Integer> generate(){
		HashMap<Member,Integer> r = new HashMap<>();
		if (rule == null) {
			generate0(r,pool,schedule);
//			if(origin == null || origin.isEmpty()) {
//				HashMap<Member,Integer> r = new HashMap<>();
//				generate0(r,pool,schedule);
//				return r;
//			} else {
//				//TODO
//				HashMap<Member,Integer> r = new HashMap<>();
//				Iterator<? extends Member> i = origin.keySet().iterator();
//				while(i.hasNext()) {
//					Member m = i.next();
//					if(Core.getTimes(origin.get(m)&m.getSpareTime()) == 0) {
//						continue;
//					}
//					r.put(m, origin.get(m)&m.getSpareTime());
//				}
//				Schedule s = schedule.getCopy();
//				for(int j = 0; j < 21; j++) {
//					int p = 1 << j;
//					Iterator<? extends Member> it = r.keySet().iterator();
//					while(it.hasNext()) {
//						Member m = it.next();
//						if((m.getSpareTime()&p)==0)
//							continue;
//						if(s.getByDate(j)>0) {
//							s.setByDate(j, s.getByDate(j)-1);
//						} else {
//							int p1 = r.get(m)&(~p);
//							if(p1==0) {
//								r.remove(m);
//							}else {
//								r.replace(m, p1);
//							}
//						}
//					}
//				}
//				generate0(r,pool,s);
//			}
		}else {
			generate0(r,pool,schedule,rule);
		}
		return r;
	}
	private synchronized void generate0(HashMap<Member, Integer> input, MemberPool pool, Schedule schedule) {
		for (int i = 0; i < 21; i++) {
			Member[] m0 = pool.getBySpareTime(1 << i);
			for (int j = 0; j < schedule.getByDate(i); j++) {
				for (Member m : m0) {
					if (input.containsKey(m)) {
						continue;
					}else {
						input.put(m, 1 << i);
						break;
					}
				}
			}
		}
	}
	private synchronized void generate0(HashMap<Member, Integer> input, MemberPool pool, Schedule schedule, Pattern pattern) {
		Integer[] o = schedule.getOrder(10);
		for (int i : o) {
			Member[] m0 = pool.getBySpareTime(1 << i);
			ArrayList<ArrayList<Member>> list = new ArrayList<>();
			for(int a = 0 ; a < Priority.values().length; a++) {
				list.add(new ArrayList<Member>());
			}
			for(Member m : m0) {
				Priority p = pattern.tagsPattern(m, 1<<i);
					for(Priority pr : Priority.values()) {
						if(p == pr) {
							list.get(pr.ordinal()).add(m);
						}
				}
			}
			ArrayList<Member> temp = new ArrayList<>();
			for(int k = list.size()-1; k>=0;k--) {
				ArrayList<Member> temp0 = list.get(k);
				Collections.sort(temp0);
				temp.addAll(temp0);
			}

			for (int j = 0; j < schedule.getByDate(i); j++) {
				for (Member m : temp) {
					if (input.containsKey(m)) {
						continue;
					}else {
						input.put(m, 1 << i);
						break;
					}
				}
			}
		}
	}
}


class NoPoolException extends RuntimeException{
	private static final long serialVersionUID = 7137361877891322320L;
	public NoPoolException(String message) {
		super(message);
	}
}
class WrongLocationException extends RuntimeException{
	private static final long serialVersionUID = 5956943873653387179L;
	public WrongLocationException(String message) {
		super(message);
	}
}