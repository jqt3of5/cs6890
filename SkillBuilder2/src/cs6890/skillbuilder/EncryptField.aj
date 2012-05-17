package cs6890.skillbuilder;

import annotations.*;
import structures.*;
import java.util.Hashtable;

import javax.crypto.SealedObject;

public aspect EncryptField {

	pointcut encrypt(Object obj) : set (@Encrypt * Person+.*) && args(obj);
	pointcut decrypt() : get (@Encrypt  * Person+.*);
	pointcut getter() : withincode(* Person+.get*(..));
	pointcut setter() : withincode(* Person+.set*(..));
	
	Hashtable<String, SealedObject> Person.encryptedObjects = new Hashtable<String, SealedObject>();
	
	private static String key = "abcdefg";
	
	void around(Object obj) : encrypt(obj)
	{
		String name = thisJoinPoint.toShortString();
		name = name.substring(4, name.length()-2);
		
		((Person)thisJoinPoint.getThis()).encryptedObjects.put(name, new SealedObject(obj, cipher));
		
		//proceed(obj);
	}
	
	Object around() : decrypt() && within(Person)
	{
		String name = thisJoinPoint.toShortString();
		name = name.substring(4, name.length()-2);
		
		SealedObject sealed = ((Person)thisJoinPoint.getThis()).encryptedObjects.get(name);
		return sealed.getObject(key);
		
	}
}
