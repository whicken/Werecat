package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import java.lang.reflect.*;

/**
 * This may not be particularly fast, since we do a full reflection
 * lookup each time getValue is invoked.
 */
public class DotExpression extends UnaryExpression {
    String field;
    public DotExpression(Expression expr, String field) {
	super(expr);
	this.field = field;
    }
    public Object getValue(RuleContext context) {
	Object l = expr.getValue(context);
	if (l == null)
	    throw new RuntimeException("Dereferencing null");
	Class c = l.getClass();
	try {
	    Field f = c.getDeclaredField(field);
	    if ((f.getModifiers() & Modifier.PUBLIC) != 0)
		return f.get(l);
	} catch (NoSuchFieldException e) {
	} catch (IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
	String method;
	if (Character.isLowerCase(field.charAt(0))) {
	    char u = Character.toUpperCase(field.charAt(0));
	    method = "get" + u + field.substring(1);
	} else {
	    method = "get"+field;
	}
	try {
	    Method m = c.getDeclaredMethod(method, (Class[]) null);
	    if ((m.getModifiers() & Modifier.PUBLIC) != 0)
		return m.invoke(l, (Object[]) null);
	} catch (NoSuchMethodException e) {
	} catch (IllegalAccessException e) {
	    throw new RuntimeException(e);
	} catch (InvocationTargetException e) {
	    throw new RuntimeException(e);
	}
	throw new RuntimeException("Cannot resolve "+field);
    }
}
