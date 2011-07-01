package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class LTExpression extends BinaryExpression {
    public LTExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	return lhs.getDouble(context) < rhs.getDouble(context) ?
	    Boolean.TRUE : Boolean.FALSE;
    }
}
