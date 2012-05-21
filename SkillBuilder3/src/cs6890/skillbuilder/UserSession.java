package cs6890.skillbuilder;

import java.util.ArrayList;

public class UserSession {

	private static UserSession session = null;

	private ArrayList<String> propertyPermissions;
	
	
	private UserSession()
	{
		propertyPermissions = new ArrayList<String>();
	}

	void addPermission(String property)
	{
		propertyPermissions.add(property);
	}
	void removePermission(String property)
	{
		propertyPermissions.remove(property);
	}
	boolean canAccess(String property)
	{
		return propertyPermissions.contains(property);	
	}
	public static UserSession getSession()
	{
		if (session == null)
		{
			session = new UserSession();
		}
		return session;
	}
	
}
