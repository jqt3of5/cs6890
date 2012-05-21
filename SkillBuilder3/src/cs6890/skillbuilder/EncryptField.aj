package cs6890.skillbuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

import annotations.*;
import structures.*;

public aspect EncryptField extends BaseAspect{

	pointcut encrypt(Object obj) : set (@Encrypt * Person+.*) && args(obj);
	pointcut decrypt() : get (@Encrypt  * Person+.*);

	Hashtable<String, SealedObject> Person.encryptedObjects;
	
	private SecretKey key;
	private Cipher cipher;


	before() : execution(Person.new(..))
	{	
		((Person)thisJoinPoint.getThis()).encryptedObjects = new Hashtable<String, SealedObject>();
	}
	
	before() : main()
	{
	 
		filename = "key.des";
		
		ObjectInputStream inputStream;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(filename));
			key = (SecretKey) inputStream.readObject();
		
		} catch (FileNotFoundException e1) {
			
			try {		
				key = KeyGenerator.getInstance("DES").generateKey();	
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}
	
	
	after() returning : main()
	{
		ObjectOutputStream output;
		try {
			output = new ObjectOutputStream(new FileOutputStream(filename));
			output.writeObject(key);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	void around(Object obj) : encrypt(obj)// && setter()
	{
		String name = thisJoinPoint.toShortString();
		name = name.substring(4, name.length()-1);
		
		try {
			
			((Person)thisJoinPoint.getTarget()).encryptedObjects.put(name, new SealedObject((Serializable) obj, cipher));

			return;
		
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		proceed(obj);
 
	}
	
	Object around() : decrypt() //&& getter()
	{
		String name = thisJoinPoint.toShortString();
		name = name.substring(4, name.length()-1);
		
		SealedObject sealed = ((Person)thisJoinPoint.getTarget()).encryptedObjects.get(name);
		
		try {
			return sealed.getObject(key);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return proceed();
	}
}
