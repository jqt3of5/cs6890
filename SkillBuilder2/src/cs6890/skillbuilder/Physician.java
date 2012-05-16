package cs6890.skillbuilder;

import java.util.ArrayList;

public class Physician extends Person {


	private String m_specialization;
	private ArrayList<Patient> m_patients;
	
	public Physician()
	{
		m_patients = new ArrayList<Patient>();
	}
	public Physician(Person person)
	{
		super(person);
		m_patients = new ArrayList<Patient>();
	}
	public void addPatient(Patient patient)
	{
		m_patients.add(patient);
	}
	public void removePatient(Patient patient)
	{
		m_patients.remove(patient);
	}
	public String getSpecialization() {
		return m_specialization;
	}
	public void setSpecialization(String specialization) {
		this.m_specialization = specialization;
	}
	
	//@Override
	//public float match(Person person);

}
