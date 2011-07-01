package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class LEExpression extends BinaryExpression {
    public LEExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	return lhs.getDouble(context) <= rhs.getDouble(context) ?
	    Boolean.TRUE : Boolean.FALSE;
    }
}
