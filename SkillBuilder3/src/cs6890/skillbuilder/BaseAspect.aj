package cs6890.skillbuilder;

public abstract aspect BaseAspect {

	pointcut main() : execution(* Main.main(..));
	
	protected String filename; //= "peopleDump.dat";

}
