package aspects;

import model.EuclideDoc;

public privileged aspect FireEventAspect {

	pointcut fireEventAdd(EuclideDoc doc) : execution(* EuclideDoc.add*(..)) && !execution(* EuclideDoc.addDocumentListener(..)) && this(doc);
	pointcut fireEventRemove(EuclideDoc doc) : execution(* EuclideDoc.remove*(..)) && this(doc);
	
	after(EuclideDoc doc) : fireEventRemove(doc)
	{
		doc.fireEvent(new EuclideDoc.DocumentModifiedEvent(doc));
		System.out.println("We just fired an event on remove");
	}
	
	
	after(EuclideDoc doc) : fireEventAdd(doc)
	{
		doc.fireEvent(new EuclideDoc.DocumentModifiedEvent(doc));
		System.out.println("We just fired an event on add");
	}
	
}
