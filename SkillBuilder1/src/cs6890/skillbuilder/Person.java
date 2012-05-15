package cs6890.skillbuilder;

import java.util.ArrayList;

public class Person {
	
	ArrayList<Name> m_names;
	ArrayList<PhoneNumber> m_numbers;
	ArrayList<Address> m_addresses;
	public Person()
	{
		m_names = new ArrayList<Name>();
		m_numbers = new ArrayList<PhoneNumber>();
		m_addresses = new ArrayList<Address>();
	}
	public Person(Person person) 
	{
		m_names = person.m_names;
		m_numbers = person.m_numbers;
		m_addresses = person.m_addresses;
	}
	//extension
	//========================
	public ArrayList<Name> getSortedNames()
	{
		ArrayList<Name> sorted = new ArrayList<Name>();
		boolean inserted = false;
		
		for (Name name : m_names)
		{
			inserted = false;
			for (int i = 0; i < sorted.size(); ++i)
			{
				if (sorted.get(i).compare(name) > 0)
				{
					sorted.add(i, name);
					inserted = true;
					break;
				}
			}
			if (inserted == false)
				sorted.add(name);
		}
		return sorted;
	}
	//========================
	public void addName(Name name)
	{
		m_names.add(name);
	}
	public void removeName(Name name)
	{
		m_names.remove(name);
	}
	public void addPhoneNumber(PhoneNumber number)
	{
		m_numbers.add(number);
	}
	public void removePhoneNumber(PhoneNumber number)
	{
		m_numbers.remove(number);
	}
	public void addAddress(Address addr)
	{
		m_addresses.add(addr);
	}
	public void removeAddress(Address addr)
	{
		m_addresses.remove(addr);
	}	
	//public abstract float match(Person person);
	
}
