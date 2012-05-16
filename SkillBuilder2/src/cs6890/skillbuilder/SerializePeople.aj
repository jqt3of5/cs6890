package cs6890.skillbuilder;

import java.util.ArrayList;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import structures.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public aspect SerializePeople {

	pointcut main() : execution(* Main.main(..));
	
	declare parents : Person implements Serializable;
	declare parents : Address implements Serializable;
	declare parents : Diagnosis implements Serializable;
	declare parents : Name implements Serializable;
	declare parents : PhoneNumber implements Serializable;
	
	
	private String encryptionKey = "key";
	private String filename = "peopleDump.dat";
	
	public void init()
	{
		
		Main.people = new ArrayList<Person>();
		Main.people.add(new Person());
		Main.people.add(new Person());
		Main.people.add(new Person());
		Main.people.add(new Person());
		
		Main.people.get(0).addAddress(new Address(Address.AddressType.HOME, "611 N. Palm st.", "", "La habra", "CA", "90631"));
		Main.people.get(0).addName(new Name(Name.NameType.ALIAS, "Sir", "John", "Quinton", "Todd", "I"));
		Main.people.get(0).addPhoneNumber(new PhoneNumber(PhoneNumber.PhoneType.CELL, "562", "697", "5733", "45"));
		
		Main.people.get(1).addAddress(new Address(Address.AddressType.POBOX, "611 N. Palm st.", "", "La habra", "CA", "90631"));
		Main.people.get(1).addName(new Name(Name.NameType.LEGAL, "Sir", "John", "Quinton", "Todd", "III"));
		Main.people.get(1).addPhoneNumber(new PhoneNumber(PhoneNumber.PhoneType.HOME, "562", "697", "5733", "45"));
		
		Main.people.get(2).addAddress(new Address(Address.AddressType.WORK, "611 N. Palm st.", "", "La habra", "CA", "90631"));
		Main.people.get(2).addName(new Name(Name.NameType.PEN, "Sir", "John", "Quinton", "Todd", "IIII"));
		Main.people.get(2).addPhoneNumber(new PhoneNumber(PhoneNumber.PhoneType.WORK, "562", "697", "5733", "45"));
				
		Main.people.get(3).addAddress(new Address(Address.AddressType.WORK, "611 N. Palm st.", "", "La habra", "CA", "90631"));
		Main.people.get(3).addName(new Name(Name.NameType.PEN, "Sir", "John", "Quinton", "Todd", "IIIIII"));
		Main.people.get(3).addPhoneNumber(new PhoneNumber(PhoneNumber.PhoneType.WORK, "562", "697", "5733", "45"));
		
	}
	
	before() : main()
	{
		
		ObjectInputStream inputStream = null;
        
        //Construct the LineNumberReader object
        try {
        	inputStream = new ObjectInputStream(new FileInputStream(filename));
			
        	Main.people = new ArrayList<Person>();
        	Person person;
        	while ((person = (Person) inputStream.readObject()) != null)
        	{
        		Main.people.add(person);
        	}
        } catch (EOFException ex) {
        	
        } catch (FileNotFoundException e) {
        	init();
        	e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	after() returning : main()
	{
		ObjectOutputStream outputStream = null;
          
        //Construct the LineNumberReader object
        try {
        	outputStream = new ObjectOutputStream(new FileOutputStream(filename));
			for (Person person : Main.people)
				outputStream.writeObject(person);
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
	}
}
