package cs6890.skillbuilder;

import annotations.*;
import structures.*;

public aspect EncryptField {

	pointcut encrypt() : set (@Encrypt * Person+.*);
	pointcut decrypt() : get (@Encrypt  * Person+.*);
	
	Object around() :encrypt()
	{
		return proceed();
	}
	
	Object around() : decrypt()
	{
		return proceed();
	}
}
