package cs6890.skillbuilder;

import java.util.ArrayList;

import structures.*;

public class Main {


	public static ArrayList<Person> people;
	public static void main(String[] args) {
				
		for (Person person : people)
		{
			System.out.println(person.getSortedNames().get(0).getFormattedName());
		}

	}

}
