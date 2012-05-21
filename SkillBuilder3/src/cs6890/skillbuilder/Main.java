package cs6890.skillbuilder;

import java.util.ArrayList;
import java.util.Scanner;

import structures.*;

public class Main {


	public static ArrayList<Person> people;
	

	static Person findPerson(Name name)
	{
		for (Person person : Main.people)
		{
			for (Name names : person.getSortedNames())
			{
				if (names.compare(name) == 0)
				{
					return person;
					
				}
			}
		}	
		return null;
	}
	
	public static void main(String[] args) {

		
		Scanner input = new Scanner(System.in);
		System.out.println("1. Add Person");
		System.out.println("2. Edit Person");
		System.out.println("3. Remove Person");
		
		int choice = 1;
		
		choice = input.nextInt();
		
		Name name;
		switch(choice)
		{
		case 1:
			Main.people.add(Person.newPersonUI());
			break;
			
		case 2:
			name = Name.newNameUI();
			
			Person person = findPerson(name);
			
			if (person != null)
				person.editPersonUI();		
			
			break;
			
		case 3:
			name = Name.newNameUI();
			Main.people.remove(findPerson(name));
					
			break;
		}
		
		for (Person person : people)
		{
			System.out.println(person.getSortedNames().get(0).getFormattedName());
			for (PhoneNumber number : person.getNumbers())
				System.out.println(number.getFormattedNumber());
		}
	}

}
