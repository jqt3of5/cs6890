//part 4, extending the design
package structures;

public class Diagnosis {
	
	public String m_condition;
	public  String m_diagnosis;
	public Physician m_attending;
	
	public Diagnosis(String condition, String diagnosis, Physician doctor)
	{
		m_condition = condition;
		m_diagnosis = diagnosis;
		m_attending = doctor;
	}
}
