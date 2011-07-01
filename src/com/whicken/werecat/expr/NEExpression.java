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
	if (isNumber(l) || isNumber(r))
	    return asDouble(l) != asDouble(r) ?
		Boolean.TRUE : Boolean.FALSE;
	return asString(l).equals(asString(r)) ?
	    Boolean.FALSE : Boolean.TRUE;
    }
}
