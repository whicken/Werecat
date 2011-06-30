package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class OrExpression extends BinaryExpression {
    public OrExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	if (asBoolean(lhs.getValue(context)) ||
	    asBoolean(rhs.getValue(context)))
	    return Boolean.TRUE;
	else
	    return Boolean.FALSE;
    }
}
