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
	if (l instanceof String || r instanceof String) {
	    return new Boolean(!asString(l).equals(asString(r)));
	}
	return new Boolean(asDouble(l) != asDouble(r));
    }
}
