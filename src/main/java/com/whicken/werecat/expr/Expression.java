package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import com.whicken.werecat.WerecatException;

public abstract class Expression {
    public abstract Object getValue(RuleContext context);
    public boolean getBoolean(RuleContext context) {
	return asBoolean(getValue(context));
    }
    public static boolean asBoolean(Object o) {
	if (o == null)
	    return false;
	if (o instanceof Boolean)
	    return ((Boolean) o).booleanValue();
	if (o instanceof Number)
	    return ((Number) o).doubleValue() != 0;
	if (o instanceof Character)
	    return ((Character) o).charValue() != 0;
	throw new WerecatException("Unexpected boolean conversion: "+
				   o.getClass());
    }
    public double getDouble(RuleContext context) {
	return asDouble(getValue(context));
    }
    public static double asDouble(Object o) {
	if (o instanceof Number)
	    return ((Number) o).doubleValue();
	if (o instanceof Boolean)
	    return ((Boolean) o).booleanValue() ? 1 : 0;
	if (o instanceof Character)
	    return ((Character) o).charValue();
	if (o != null)
	    return Double.parseDouble(o.toString());
	throw new WerecatException("Unexpected numeric conversion: "+
				   (o == null ? "null" : o.getClass()));
    }
    public String getString(RuleContext context) {
	return asString(getValue(context));
    }
    public static String asString(Object o) {
	if (o instanceof String)
	    return (String) o;
	else if (o == null)
	    return null;
	return o.toString();
    }
    static boolean isNumber(Object o) {
	return o instanceof Number ||
	    o instanceof Character ||
	    o instanceof Boolean;
    }
    static boolean isStringNumber(String s) {
	if (s == null || s.length() == 0)
	    return false;
	boolean dot = false;
	for (int i = 0; i < s.length(); ++i) {
	    if (s.charAt(i) == '.') {
		if (dot)
		    return false;
		dot = true;
	    } else if (s.charAt(i) == '-') {
		if (i > 0)
		    return false;
	    } else if (Character.isDigit(s.charAt(i))) {
	    } else {
		return false;
	    }
	}
	return true;
    }
}
