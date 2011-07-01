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
	if (l instanceof String || r instanceof String) {
	    return asString(l).equals(asString(r)) ?
		Boolean.TRUE : Boolean.FALSE;
	}
	return asDouble(l) == asDouble(r) ?
	    Boolean.TRUE : Boolean.FALSE;
    }
}
