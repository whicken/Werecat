package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import com.whicken.werecat.WerecatException;
import java.lang.reflect.*;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

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
	Class c;
	Object l;
	if (expr instanceof ClassReference) {
	    c = ((ClassReference) expr).c;
	    l = null;
	} else {
	    l = expr.getValue(context);
	    if (l == null)
		throw new WerecatException("Dereferencing null");
	    c = l.getClass();
	}
	try {
	    Field f = c.getDeclaredField(field);
	    if ((f.getModifiers() & Modifier.PUBLIC) != 0)
		return f.get(l);
	} catch (NoSuchFieldException e) {
	} catch (IllegalAccessException e) {
	    throw new WerecatException("Error accessing "+field+" on "+c, e);
	}

	// First look for plain accessor (scala style)
	try {
	    Method m = c.getDeclaredMethod(field, (Class[]) null);
	    if ((m.getModifiers() & Modifier.PUBLIC) != 0)
		return m.invoke(l, (Object[]) null);
	} catch (NoSuchMethodException e) {
	} catch (IllegalAccessException e) {
	    throw new WerecatException("Error accessing "+field+" on "+c, e);
	} catch (InvocationTargetException e) {
	    throw new WerecatException("Error invoking "+field+" on "+c, e);
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
	    throw new WerecatException("Error accessing "+field+" on "+c, e);
	} catch (InvocationTargetException e) {
	    throw new WerecatException("Error invoking "+method+" on "+c, e);
	}

	// Some syntactic sugar for maps and map-like objects
	if (l instanceof Map) {
	    // Note: This requires the key to be of the right type
	    return ((Map) l).get(field);
	} else if (l instanceof Set) {
	    // Note: This requires the key to be of the right type
	    return ((Set) l).contains(field) ? Boolean.TRUE : Boolean.FALSE;
	} else if (l instanceof JSONObject) {
	    try {
		JSONObject obj = (JSONObject) l;
		if (obj.has(field))
		    return obj.get(field);
		return null;
	    } catch (JSONException e) {
		// Note: This might be worthy of a log4j warning
	    }
	}

	throw new WerecatException("Cannot resolve "+field+" on "+c);
    }
    public String toString() {
	StringBuffer b = new StringBuffer();
	b.append(expr.toString());
	b.append(".");
	b.append(field);
	return b.toString();
    }
}
