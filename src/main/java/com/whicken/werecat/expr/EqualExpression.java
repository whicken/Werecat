package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class EqualExpression extends BinaryExpression {
    public EqualExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	Object l = lhs.getValue(context);
	Object r = rhs.getValue(context);
	if (l == null) {
	    if (r == null)
		return Boolean.TRUE;
	    return Boolean.FALSE;
	} else if (r == null) {
	    return Boolean.FALSE;
	}
	if (isNumber(l)) {
	    if (isNumber(r))
		return asDouble(l) == asDouble(r) ? Boolean.TRUE : Boolean.FALSE;
	    String rstr = asString(r);
	    if (isStringNumber(rstr)) {
		double d = Double.parseDouble(rstr);
		return asDouble(l) == d ? Boolean.TRUE : Boolean.FALSE;
	    }
	    return Boolean.FALSE;
	} else if (isNumber(r)) {
	    String lstr = asString(l);
	    if (isStringNumber(lstr)) {
		double d = Double.parseDouble(lstr);
		return d == asDouble(r) ? Boolean.TRUE : Boolean.FALSE;
	    }
	    return Boolean.FALSE;
	}
	return asString(l).equals(asString(r)) ? Boolean.TRUE : Boolean.FALSE;
    }
    public String toString() {
	StringBuffer b = new StringBuffer();
	b.append(lhs.toString());
	b.append(" = ");
	b.append(rhs.toString());
	return b.toString();
    }
}
