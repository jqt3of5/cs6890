package structures;

public class Name {
	
	public enum NameType {ALIAS, LEGAL, PEN, OTHER}
	
	private NameType m_nameType;
	private String m_salutation;
	private String m_firstName;
	private String m_middleName;
	private String m_lastName;
	private String m_suffix;
	
	public Name(){}
	public Name(NameType type, String salutation, String first, String middle, String last, String suffix)
	{
		m_nameType = type;
		m_salutation = salutation;
		m_firstName = first;
		m_middleName = middle;
		m_lastName = last;
		m_suffix = suffix;
	}
	public String getSortName()
	{
		return  m_lastName + ", " + m_firstName + " " + m_middleName;
	}
	public String getFormattedName()
	{
		return m_salutation + " " + m_firstName + " " + m_middleName + " " + m_lastName + " " + m_suffix;
	}	
	public int compare(Name name)
	{
		return getSortName().compareTo(name.getSortName());
	}
	
	@Override public boolean equals(Object number)
	{
		return compare((Name) number) == 0;
	}
	//public float match(Name name);
	
}
