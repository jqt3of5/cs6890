package cs6890.skillbuilder;

import structures.*;
import java.util.Scanner;

import com.sun.corba.se.spi.orbutil.fsm.Input;


public aspect PersonUI {

	private static Scanner input = new Scanner(System.in);
	
	static Name Name.newNameUI()
	{
		String salutation = "", first = "", middle= "", last= "", suffix = "";
		
		//Scanner input = new Scanner(System.in);
		
		System.out.print("First Name: ");
		first = input.nextLine();
		System.out.print("Middle Name: ");
		middle = input.nextLine();
		System.out.print("Last Name: ");
		last = input.nextLine();
		
		System.out.print("Would you like to add more information?");
		
		if (input.nextLine().charAt(0) == 'y'){
			System.out.println("Suffix : ");
			suffix = input.nextLine();
			System.out.println("Salutation : ");
			salutation = input.nextLine();
		}

		return new Name(Name.NameType.LEGAL, salutation, first, middle, last, suffix);
	}
	
	void Person.editPersonUI()
	{
		//Scanner input = new Scanner(System.in);
		System.out.println("What would you like to edit?");
		System.out.println("1. Names");
		System.out.println("2. Phone numbers");
		System.out.println("3. Addresses");
		
		char choice;
		switch(Integer.parseInt(input.nextLine()))
		{
		case 1:
			System.out.println("Would you like to (a)dd or (r)emove a name?");
			choice = input.nextLine().charAt(0);
			
			System.out.println("Please enter the name:");
			Name name = Name.newNameUI();
			switch (choice)
			{
			case 'a':
				this.addName(name);
				break;
			case 'r':
				this.removeName(name);				
				break;
			}
			break;
			
		case 2:
			System.out.println("Would you like to (a)dd or (r)emove a phone number?");
			choice = input.nextLine().charAt(0);
			
			System.out.println("Please enter the number:");
			PhoneNumber number = PhoneNumber.newNumberUI();
			System.out.println(number.getFormattedNumber());
			switch (choice)
			{
			case 'a':
				this.addPhoneNumber(number);
				break;
			case 'r':
				this.removePhoneNumber(number);				
				break;
			}
			break;

		case 3:
			System.out.println("Would you like to (a)dd or (r)emove an address?");
			choice = input.nextLine().charAt(0);
			
			System.out.println("Please enter the address:");
			Address address = Address.newAddressUI();
			switch (choice)
			{
			case 'a':
				this.addAddress(address);
				break;
			case 'r':
				this.removeAddress(address);				
				break;
			}
			
			break;
		}
		
	}
	
	static Person Person.newPersonUI()
	{
		Person newPerson = new Person();
		//Scanner input = new Scanner(System.in);
		char choice = 'n';
		
		System.out.println("Would you like to add a name?");
		choice = input.nextLine().charAt(0);
		
		while (choice == 'y'){
		newPerson.addName(Name.newNameUI());
		
		System.out.println("Would you like to add a name?");
		choice = input.nextLine().charAt(0);
		}
		
		System.out.println("Would you like to add a phone number?");
		choice = input.nextLine().charAt(0);
		
		while (choice == 'y'){
		newPerson.addPhoneNumber(PhoneNumber.newNumberUI());
		
		System.out.println("Would you like to add a phone number?");
		choice = input.nextLine().charAt(0);
		}
		
		System.out.println("Would you like to add an address");
		choice = input.nextLine().charAt(0);
		
		while (choice == 'y'){
		newPerson.addAddress(Address.newAddressUI());
		
		System.out.println("Would you like to add an address?");
		choice = input.nextLine().charAt(0);
		}
		
		return newPerson;
	}
	
	static PhoneNumber PhoneNumber.newNumberUI()
	{
		String areaCode, exchange, detailNumber, extension;
		//Scanner input = new Scanner(System.in);
		
		System.out.print("Area Code: ");
		areaCode = input.nextLine();
		
		System.out.print("Exchange: ");
		exchange = input.nextLine();
		
		System.out.print("Detail: ");
		detailNumber= input.nextLine();
		
		System.out.print("Extension: ");
		extension = input.nextLine();
		
		return new PhoneNumber(PhoneNumber.PhoneType.HOME, areaCode, exchange, detailNumber, extension);
	}

	static Address Address.newAddressUI()
	{
		//Scanner input = new Scanner(System.in);
		String line1, line2, city, state, postalCode;
		
		System.out.print("First Line: ");
		line1 = input.nextLine();
		
		System.out.print("Second Line: ");
		line2 = input.nextLine();
		
		System.out.print("City: ");
		city = input.nextLine();
		
		System.out.print("State: ");
		state = input.nextLine();
		
		System.out.print("Postal Code: ");
		postalCode = input.nextLine();
		
		return new Address(Address.AddressType.HOME, line1, line2, city, state, postalCode);
	}
}
