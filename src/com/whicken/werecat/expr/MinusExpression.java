package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class MinusExpression extends BinaryExpression {
    public MinusExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	return new Double(lhs.getDouble(context)-rhs.getDouble(context));
    }
}
