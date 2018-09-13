package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import com.whicken.werecat.WerecatException;
import java.util.Date;
import net.balusc.util.DateUtil;
import org.joda.money.Money;

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
	if (o instanceof String)
	    return !((String) o).isEmpty();
	throw new WerecatException("Unexpected boolean conversion: "+
				   o.getClass());
    }
    public static Date asDate(Object o) {
	if (o instanceof Date)
	    return (Date) o;
	if (o instanceof String) {
	    String str = (String) o;
	    try {
		return DateUtil.parse(str);
	    } catch (Throwable t) {
		throw new WerecatException("Unexpected date conversion: "+str);
	    }
	}
	// Use "one year minimum to prevent accidental conversions. It's a
	// bit of a hack for sure.
	if (o instanceof Number) {
	    long ts = ((Number) o).longValue();
	    if (ts > 31536000000L)
		return new Date(ts);
	}
	throw new WerecatException("Unexpected date conversion: "+
				   o.getClass());
    }
    public double getDouble(RuleContext context) {
	return asDouble(getValue(context));
    }
    // Boolean's no longer promote to double
    public static double asDouble(Object o) {
	if (o instanceof Number)
	    return ((Number) o).doubleValue();
	if (o instanceof Character)
	    return ((Character) o).charValue();
	if (o instanceof Money)
	    return ((Money) o).getAmount().doubleValue();
	if (o instanceof Date)
	    return ((Date) o).getTime();
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
	if (o instanceof Money)
	    return ((Money) o).getAmount().toString();
	if (o == null)
	    return null;
	return o.toString();
    }
    public static boolean isNumber(Object o) {
	if (o instanceof Number || o instanceof Money)
	    return true;
	if (o instanceof String && isStringNumber((String) o))
	    return true;
	return false;
    }
    public static boolean isStringNumber(String s) {
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
    public void accept(ExpressionVisitor v) {
	v.visit(this);
    }
}
