package base;

public abstract class Pattern {
	public enum Priority{
		LOW,MEDIUM,HIGH;
	}
	public abstract Priority tagsPattern(Member m, int time);
}
