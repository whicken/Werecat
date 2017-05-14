package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import org.joda.money.Money;

public class SimpleConstant extends Expression {
    Object value;
    public SimpleConstant(Object o) {
	value = o;
    }
    public Object getValue(RuleContext context) {
	return value;
    }
    public String toString() {
	if (value == null)
	    return "null";
	if (value instanceof String)
	    return "\""+value.toString()+"\"";
	if (value instanceof Money)
	    return ((Money) value).getAmount().toString();
	return value.toString();
    }
    public Object getValue() {
	return value;
    }
    // Auto convert to long if the number is too big
    public static SimpleConstant parseInt(String s) {
	try {
	    return new SimpleConstant(new Integer(s));
	} catch (NumberFormatException e) {
	    return new SimpleConstant(new Long(s));
	}
    }
    public static SimpleConstant parseLong(String s) {
	return new SimpleConstant(new Long(s.substring(0, s.length()-1)));
    }
}
