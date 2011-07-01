package com.whicken.werecat;

import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.expr.ArrayExpression;
import com.whicken.werecat.expr.DotExpression;
import com.whicken.werecat.expr.FieldExpression;
import com.whicken.werecat.expr.MethodExpression;
import java.lang.reflect.*;

/**
 * Helper class for instantiating a RuleEngine.  Subclass to provide
 * appropriate hooks for expression parser.
 */
public class RuleFactory {
    protected Class context;
    public RuleFactory(Class context) {
	this.context = context;
    }
    /**
     * Requires the method to be public
     */
    protected Method getMethod(String method) {
	try {
	    Method m = context.getDeclaredMethod(method, (Class[]) null);
	    if ((m.getModifiers() & Modifier.PUBLIC) != 0)
		return m;
	} catch (NoSuchMethodException e) {
	    // Not an error
	}
	return null;
    }
    /**
     * Override this is you want something different than reflection.
     */
    public Expression createExpression(String key) {
	try {
	    Field field = context.getDeclaredField(key);
	    if ((field.getModifiers() & Modifier.PUBLIC) != 0)
		return new FieldExpression(field);
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
	    return new MethodExpression(m);
	return null;
    }
    /**
     * Override this is you want something different than reflection.
     */
    public Expression createMethod(String method) {
	Method m = getMethod(method);
	if (m != null)
	    return new MethodExpression(m);
	return null;
    }
    public Expression createCompoundExpression(Expression lhs, Object rhs) {
	if (rhs instanceof String) {
	    // lhs.thing
	    return new DotExpression(lhs, (String) rhs);
	} else if (rhs instanceof Expression) {
	    // lhs[thing]
	    return new ArrayExpression(lhs, (Expression) rhs);
	}
	throw new RuntimeException("Unexpected case in createCompoundExpression");
    }
}
