package cs6890.skillbuilder;

public class Address {

	public enum AddressType{HOME, WORK, POBOX}
	
	private AddressType m_addressType;
	private String m_streetLine1;
	private String m_streetLine2;
	private String m_city;
	private String m_state;
	private String m_postalCode;
	
	public Address(){};
	public Address(AddressType type, String line1, String line2, String city, String state, String postalCode)
	{
		m_addressType = type;
		m_streetLine1 = line1;
		m_streetLine2 = line2;
		m_city = city;
		m_state = state;
		m_postalCode = postalCode;
	}
	public String getFormattedAddress()
	{
		return m_streetLine1 + " " + m_streetLine2 + " " + m_city + ", " + m_state + " " + m_postalCode; 
	}
	public int compare(Address addr)
	{
		return getFormattedAddress().compareTo(addr.getFormattedAddress());
	}
	//public float match(Address addr);
}
