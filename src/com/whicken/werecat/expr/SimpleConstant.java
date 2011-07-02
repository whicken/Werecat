package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

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
	return value.toString();
    }
}
