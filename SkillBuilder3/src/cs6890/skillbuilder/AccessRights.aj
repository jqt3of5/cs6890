package cs6890.skillbuilder;
import structures.Person;
import annotations.Protected;

public aspect AccessRights extends BaseAspect{

	pointcut writepoint(Object obj) : set(@Protected * Person+.*) && args(obj); 
	pointcut readpoint() : get(@Protected  * Person+.*); 
	
	before() : main()
	{
		UserSession.getSession().addPermission("get(Person.m_numbers)");
		UserSession.getSession().addPermission("set(Person.m_numbers)");
	}
	
	void around(Object obj) : writepoint(obj)
	{
		if(UserSession.getSession().canAccess(thisJoinPoint.toShortString()))
		{
			proceed(obj);
			return;
		}
		throw new Error("Attempted to violate: " + thisJoinPoint.toShortString());
	}
	
	Object around() : readpoint()
	{
		if(UserSession.getSession().canAccess(thisJoinPoint.toShortString()))
		{
			return proceed();
		}
		throw new Error("Attempted to violate: " + thisJoinPoint.toShortString());
		
	}
}
