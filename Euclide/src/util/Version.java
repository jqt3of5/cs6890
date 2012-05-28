package util;

public class Version {
	int major;
	int minor;
	int build;
	String desc = "";

	public Version() {
		this(0, 0, 0);
	}
	
	public Version(int major, int minor, int build) {
		this.major = major;
		this.minor = minor;
		this.build = build;
	}
	
	public Version(int major, int minor, int build, String desc) {
		this.major = major;
		this.minor = minor;
		this.build = build;
		this.desc = desc;
	}
	
	public String toString() {
		if (this.desc.isEmpty())
			return String.format("%d.%d.%d", major, minor, build);
		else
			return String.format("%d.%d.%d-%s", major, minor, build, desc);
	}
}
