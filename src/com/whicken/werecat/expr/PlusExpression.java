package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class PlusExpression extends BinaryExpression {
    public PlusExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	return new Double(lhs.getDouble(context)+rhs.getDouble(context));
    }
}
