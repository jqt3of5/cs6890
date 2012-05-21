package cs6890.skillbuilder;

public class PhoneNumber {

	public enum PhoneType{CELL, HOME, WORK}
	
	private PhoneType m_phoneType;
	private String m_areaCode;
	private String m_exchange;
	private String m_detailNumber;
	private String m_extension;
	
	public PhoneNumber(){}
	public PhoneNumber(PhoneType type, String areaCode, String exchange, String detail, String extension)
	{
		m_phoneType = type;
		m_areaCode = areaCode;
		m_exchange = exchange;
		m_detailNumber = detail;
		m_extension = extension;
	}
 	public String getFormattedNumber()
	{
		return "1-" + m_areaCode + "-" + m_exchange + "-" + m_detailNumber + "+" + m_extension;
	}
	public int compare(PhoneNumber number)
	{
		return getFormattedNumber().compareTo(number.getFormattedNumber());
	}
	//public float match(PhoneNumber number);
	
}
