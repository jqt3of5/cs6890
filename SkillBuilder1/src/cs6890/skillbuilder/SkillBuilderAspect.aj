package cs6890.skillbuilder;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Timer;

public aspect SkillBuilderAspect {
	private int indents = 0;
	private long start = 0;
	private long end = 0;
	private PrintWriter fout = null;
	pointcut trace() : call (* Person.*(..)) || call (* Patient.*(..)) || call (* EmergencyContact.*(..));
	pointcut getset() : call (* Person.get*(..) ) || call (* Person.set*(..)) 
					 || call (* Patient.get*(..) ) || call (* Patient.set*(..))
					 || call (* EmergencyContact.get*(..) ) || call (* EmergencyContact.set*(..)) ;
	
	
	before() : execution(* *.main(..))
	{
		try {
				fout = new PrintWriter("dump.dat");
			} catch (FileNotFoundException e) {
				System.out.println("blah, couldn't open file");
			}
		
	}
	before() : (trace() && cflow(trace()))
	{
		
		
		indents += 1;
		for (int i = 1; i < indents; ++i)
			fout.print("\t");
		fout.println(thisJoinPoint);
		
	}
	
	before() : trace() && !getset()
	{
		start = System.nanoTime();
	}
	after() returning : execution (* *.main(..))
	{
		fout.close();
	}
	
	
	after()  returning : (trace() && !getset())
	{
		end = System.nanoTime();
		System.out.println(thisJoinPoint.toShortString() + " timing results:" + (end - start));
	}
	after() returning : trace()
	{
		indents -= 1;
	}
	
}
