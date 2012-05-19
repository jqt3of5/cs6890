package cs6890.skillbuilder;

import java.util.ArrayList;

import structures.*;

public class Main {


	public static ArrayList<Person> people;
	public static void main(String[] args) {
			
		for (Person person : people)
		{
			System.out.println(person.tempStr1);
			System.out.println(person.tempStr2);
			System.out.println(person.tempStr3);
			
			System.out.println(person.getSortedNames().get(0).getFormattedName());
			System.out.println(person.getNumbers().get(0).getFormattedNumber());
			
			person.tempStr1 = "";
			person.tempStr2 = "";
			person.tempStr3 = "";
			
			System.out.println(person.tempStr1);
			System.out.println(person.tempStr2);
			System.out.println(person.tempStr3);
			System.out.println("\n");
		}

	}

}
