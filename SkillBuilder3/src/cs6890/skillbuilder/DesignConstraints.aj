package cs6890.skillbuilder;

import structures.*;
import annotations.*;

public aspect DesignConstraints {

	declare error : call (* UserSession.addPermission(..)) && !within(AccessRights) : "Only the aspect is allowed to add permissions";
	declare warning : call (* UserSession.removePermission(..)) && !within(AccessRights) : "User Permissions should be handled in the aspect only";
	
	declare error : (get(@Encrypt * Person+.*) || set (@Encrypt * Person+.*)) && within(EncryptField) : "This may cause infinite recursion!";
	
}
