package base;
import java.time.LocalDateTime;
import java.util.ArrayList;
public class Schedule implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5631896376780052150L;
	private LocalDateTime modifiedTime;
	private int [][] workingSchedule= {{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}};
	{
		this.modifiedTime = LocalDateTime.now();
	}
	
	private Schedule() {}
	
	public static Schedule getScheduleByArray(int[][] source) {
		Schedule s = new Schedule();
		s.setWorkingSchedule(source);
		return s;
	}
	
	void setWorkingSchedule(int[][] source) {
		this.modifiedTime = LocalDateTime.now();
		if (source == null)
			return;
		for (int i = 0; i < ((source.length <= this.workingSchedule.length) ? source.length
				: this.workingSchedule.length); i++)
			for (int j = 0; j < ((source[i].length <= this.workingSchedule[i].length) ? source[i].length
					: this.workingSchedule[i].length); j++)
				this.workingSchedule[i][j] = source[i][j];
	}
	public int getByDate(int date) {
		if (date>=0&&date<=20) {
			int y = date%3;
			int x = date/3;
			return this.workingSchedule[y][x];
		}
		return -1;
	}
	
	public boolean setByDate(int date, int times) {
		if (date >= 0 && date <= 20) {
			if (times >= 0) {
				int y = date % 3;
				int x = date / 3;
				this.workingSchedule[y][x] = times>=0?times:0;
				this.modifiedTime = LocalDateTime.now();
				return true;
			}

		}
		return false;
	}

	public boolean setByDate(int day, int time, int times) {
		if (day >= 0 && day <= 6) {
			if (time >= 0 && time <= 2) {
				if (times >= 0) {
					this.workingSchedule[time][day] = times>=0?times:0;
					this.modifiedTime = LocalDateTime.now();
					return true;
				}
			}

		}
		return false;
	}
	
	public int[][] getSchedule(){
		return this.workingSchedule.clone();
	}
	
	public Schedule getCopy() {
		Schedule r = new Schedule();
		r.setWorkingSchedule(this.getSchedule());
		return r;
	}
	public LocalDateTime getModifiedTime() {
		return modifiedTime;
	}
	public final Integer[] getOrder(Integer...init) {
		ArrayList<Integer> temp = new ArrayList<>();
		for(int c:init) {
			if(c<0||c>20) {
				continue;
			}
			if(temp.contains(c)) {
				continue;
			}
			temp.add(c);
		}
		int isize = temp.size();
		for (int i = 0; i < 21; i++) {
			int times = this.getByDate(i);
			if(temp.size()==isize) {
				temp.add(i);
				continue;
			}
			if(temp.contains(i)) {
				continue;
			}
			
			
			for(int j = isize; j < temp.size();j++) {
				if(this.getByDate(temp.get(j))>times) {
					temp.add(j, i);
					break;
				}else if(j == temp.size()-1) {
					temp.add(i);
					break;
				}
				
			}
		}
		return temp.toArray(new Integer[temp.size()]);
	}
//	public static void main(String [] args) {
//		Schedule s = Schedule.getScheduleByArray(new int[][]{{3,1,1,1,1,1,1},
//															 {3,1,1,7,1,1,6},
//															 {4,1,1,1,3,1,6}});
//		Integer i[] = s.getOrder(10);
//		for(int in:i) {
//			System.out.println(in);
//		}
//		System.out.println(i.length);
//	}
}
