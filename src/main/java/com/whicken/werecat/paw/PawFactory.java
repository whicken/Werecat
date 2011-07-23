package com.whicken.werecat.paw;

import com.whicken.werecat.RuleFactory;
import com.whicken.werecat.WerecatException;
import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.expr.MethodExpression;
import com.whicken.werecat.expr.ClassReference;
import java.lang.reflect.*;
import java.util.List;

/**
 * This class hides the RuleContext implementation.
 */
class PawFactory extends RuleFactory {
    PawFactory(Class context) {
	super(context);
    }
    public Expression createField(String key) {
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
	Method m = getMethod(method, null);
	if (m != null)
	    return new PawMethod(m, null);

	Class c = getClass(key);
	if (c != null)
	    return new ClassReference(c);

	throw new WerecatException("Cannot resolve field: "+key);
    }
    public Expression createMethod(String method, List<Expression> args) {
	Method m = getMethod(method, args);
	if (m != null)
	    return new PawMethod(m, args);

	throw new WerecatException("Cannot resolve method: "+method);
    }
}
