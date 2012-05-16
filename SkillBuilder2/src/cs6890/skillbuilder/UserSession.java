package cs6890.skillbuilder;

public class UserSession {

	private static UserSession session = null;
	private int sessionId;
	private String userName;
	public enum Permissions{READ, WRITE};
	
	private UserSession()
	{
		sessionId = -1;
		userName = "";
	}
	
	public UserSession getSession()
	{
		if (session == null)
		{
			session = new UserSession();

		}
		
		return session;
	}
	
	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
