package aspects;


public aspect RepaintAspect {

	pointcut repaintJoinPoint() : within(gui.*);
	
	
}
