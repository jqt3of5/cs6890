package cs6890.skillbuilder;

import java.util.ArrayList;

public class Main {


	public static void main(String[] args) {
		
		Person personA = new Person();
		Person personB = new Person();
		Person personC = new Person();
		
		ArrayList<Patient> patients = new ArrayList<Patient>();
		patients.add(new Patient(personA));
		patients.add(new Patient(personB));
		patients.add(new Patient(personC));
		
		EmergencyContact contact = new EmergencyContact(personB, "father");
		Physician doctor = new Physician(personC);
		
		personA.addAddress(new Address(Address.AddressType.HOME, "611 N. Palm st.", "", "La habra", "CA", "90631"));
		personA.addName(new Name(Name.NameType.ALIAS, "Sir", "John", "Quinton", "Todd", "II"));
		personA.addName(new Name(Name.NameType.LEGAL, "Dr.", "Lisa", "Beth", "Pinch", ""));
		personA.addName(new Name(Name.NameType.ALIAS, "Sir", "Mark", "Henry", "Struve", ""));
		personA.addName(new Name(Name.NameType.ALIAS, "", "Lisa", "Park", "Pinch", ""));
		personA.addName(new Name(Name.NameType.ALIAS, "", "Matt", "Henry", "Struve", ""));
		personA.addPhoneNumber(new PhoneNumber(PhoneNumber.PhoneType.CELL, "562", "697", "5733", "45"));
		
		personB.addAddress(new Address(Address.AddressType.POBOX, "611 N. Palm st.", "", "La habra", "CA", "90631"));
		personB.addName(new Name(Name.NameType.LEGAL, "Sir", "John", "Quinton", "Todd", "II"));
		personB.addPhoneNumber(new PhoneNumber(PhoneNumber.PhoneType.HOME, "562", "697", "5733", "45"));
		
		personC.addAddress(new Address(Address.AddressType.WORK, "611 N. Palm st.", "", "La habra", "CA", "90631"));
		personC.addName(new Name(Name.NameType.PEN, "Sir", "John", "Quinton", "Todd", "II"));
		personC.addPhoneNumber(new PhoneNumber(PhoneNumber.PhoneType.WORK, "562", "697", "5733", "45"));
		
		doctor.addPatient(patients.get(0));
		patients.get(0).addPhysician(doctor);
		patients.get(0).addEmergencyContact(personB, "Mother");
		patients.get(0).addEmergencyContact(contact, "Father");
		
		patients.get(0).removePhysician(doctor);
		doctor.removePatient(patients.get(0));
		
		//these need some help I believe..
		//patient.removeEmergencyContact(personB);
		//patient.removeEmergencyContact(contact);
				
				
		//extension..
		patients.get(0).addDiagnosis(new Diagnosis("Heart problems", "Cheerios", doctor));
		patients.get(1).addDiagnosis(new Diagnosis("Women", "Confidence", doctor));
		patients.get(2).addDiagnosis(new Diagnosis("Women", "Cheerios", doctor));
		patients.get(2).addDiagnosis(new Diagnosis("Boring", "Placebo", doctor));
		
		String condition = "Women";
		for (Patient patient : patients)
		{
			for (Diagnosis diag : patient.getDiagnosis())
			{
				if (diag.m_condition == condition)
				{
	//				System.out.println("Condition: " + condition + " Diagnosis: " + diag.m_diagnosis );
				}
			}
		}
		//============
		
		ArrayList<Name> sorted = personA.getSortedNames();
		
		for (Name name : sorted)
		{
		//	System.out.println(name.getFormattedName());
		}
		

	}

}
