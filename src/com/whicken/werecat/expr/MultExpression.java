package com.whicken.rule.expr;

import com.whicken.rule.RuleContext;

public class MultExpression extends BinaryExpression {
    public MultExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	return new Double(lhs.getDouble(context)*rhs.getDouble(context));
    }
}
