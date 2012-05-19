package structures;

import java.util.ArrayList;

import annotations.Encrypt;
import annotations.Protected;


public class Person {
	
	ArrayList<Name> m_names;
	@Protected ArrayList<PhoneNumber> m_numbers;
	ArrayList<Address> m_addresses;
	public @Encrypt String tempStr1;
	public @Encrypt String tempStr2;
	public @Encrypt String tempStr3;
	
	public Person()
	{
		tempStr1 = new String("");
		tempStr2 = new String("");
		tempStr3 = new String("");
		
		m_names = new ArrayList<Name>();
		m_numbers = new ArrayList<PhoneNumber>();
		m_addresses = new ArrayList<Address>();
	}
	public Person(Person person) 
	{
		tempStr1 = new String("");
		tempStr2 = new String("");
		tempStr3 = new String("");
		
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
	
	public ArrayList<PhoneNumber> getNumbers() {
		return m_numbers;
	}
	public void setNumbers(ArrayList<PhoneNumber> m_numbers) {
		this.m_numbers = m_numbers;
	}
	//public abstract float match(Person person);
	
}
