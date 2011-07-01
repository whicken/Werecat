package com.whicken.werecat.paw;

import com.whicken.werecat.RuleFactory;
import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.expr.MethodExpression;
import java.lang.reflect.*;

/**
 * This class hides the RuleContext implementation.
 */
class PawFactory extends RuleFactory {
    PawFactory(Class context) {
	super(context);
    }
    public Expression createExpression(String key) {
	try {
	    Field field = context.getDeclaredField(key);
	    if ((field.getModifiers() & Modifier.PUBLIC) != 0)
		return new PawField(field);
	} catch (NoSuchFieldException e) {
	}

	String method;
	if (Character.isLowerCase(key.charAt(0))) {
	    char c = Character.toUpperCase(key.charAt(0));
	    method = "get" + c + key.substring(1);
	} else {
	    method = "get"+key;
	}
	Method m = getMethod(method);
	if (m != null)
	    return new PawMethod(m);
	return null;
    }
    public Expression createMethod(String method) {
	Method m = getMethod(method);
	if (m != null)
	    return new PawMethod(m);
	return null;
    }
}
