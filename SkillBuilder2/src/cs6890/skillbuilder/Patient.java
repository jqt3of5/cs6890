package cs6890.skillbuilder;

import java.util.ArrayList;

public class Patient extends Person {

	public class Date{
		int day;
		int month;
		int year;
	}
	private int m_id;
	private String m_gender;
	private Date m_birthday;
	private ArrayList<Person> m_emergencyContacts;
	private ArrayList<Person> m_physicians;
	//extension
	private ArrayList<Diagnosis> m_diagnosis;
	
	public Patient(Person person)
	{
		super(person);
		m_emergencyContacts = new ArrayList<Person>();
		m_physicians = new ArrayList<Person>();
		//extension
		m_diagnosis = new ArrayList<Diagnosis>();
	}
	public Patient()
	{
		m_emergencyContacts = new ArrayList<Person>();
		m_physicians = new ArrayList<Person>();
		//extension
		m_diagnosis = new ArrayList<Diagnosis>();
	}
	public void addPhysician(Physician physician)
	{
		m_physicians.add(physician);
	}
	public void removePhysician(Physician physician)
	{
		m_physicians.remove(physician);
	}
	public void addEmergencyContact(Person person, String relationship)
	{
		m_emergencyContacts.add(new EmergencyContact(person, relationship));
	}
	public void removeEmergencyContact(Person person)
	{
		m_emergencyContacts.remove(person);
	}
	public int getId() {
		return m_id;
	}
	public void setId(int id) {
		this.m_id = id;
	}
	public String getGender() {
		return m_gender;
	}
	public void setGender(String gender) {
		this.m_gender = gender;
	}
	public Date getBbirthday() {
		return m_birthday;
	}
	public void setBirthday(Date birthday) {
		this.m_birthday = birthday;
	}

	//extension
	public void addDiagnosis(Diagnosis diag)
	{
		m_diagnosis.add(diag);
	}
	public void removeDiagnosis(Diagnosis diag)
	{
		m_diagnosis.remove(diag);
	}
	public ArrayList<Diagnosis> getDiagnosis() {
		return m_diagnosis;
	}
	public void setDiagnosis(ArrayList<Diagnosis> diagnosis) {
		this.m_diagnosis = diagnosis;
	}
	

	//@Override
	//public float match(Person person);
	
}
