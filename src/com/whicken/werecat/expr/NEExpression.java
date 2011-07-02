package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class NEExpression extends BinaryExpression {
    public NEExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	Object l = lhs.getValue(context);
	Object r = rhs.getValue(context);
	if (l == null) {
	    if (r == null)
		return Boolean.FALSE;
	    return Boolean.TRUE;
	} else if (r == null) {
	    return Boolean.TRUE;
	}
	if (isNumber(l)) {
	    if (isNumber(r))
		return asDouble(l) != asDouble(r) ? Boolean.TRUE : Boolean.FALSE;
	    String rstr = asString(r);
	    if (isStringNumber(rstr)) {
		double d = Double.parseDouble(rstr);
		return asDouble(l) != d ? Boolean.TRUE : Boolean.FALSE;
	    }
	    return Boolean.TRUE;
	} else if (isNumber(r)) {
	    String lstr = asString(l);
	    if (isStringNumber(lstr)) {
		double d = Double.parseDouble(lstr);
		return d != asDouble(r) ? Boolean.TRUE : Boolean.FALSE;
	    }
	    return Boolean.TRUE;
	}
	return asString(l).equals(asString(r)) ?
	    Boolean.FALSE : Boolean.TRUE;
    }
}
