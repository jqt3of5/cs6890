package cs6890.skillbuilder;

public class EmergencyContact extends Person {

	private String m_relationship;

	public EmergencyContact() {}
	public EmergencyContact(Person person, String relation)
	{
		super(person);
		setRelationship(relation);
	}
	//@Override
	//public float match(Person person);
	public String getRelationship() {
		return m_relationship;
	}
	public void setRelationship(String relationship) {
		this.m_relationship = relationship;
	}
}