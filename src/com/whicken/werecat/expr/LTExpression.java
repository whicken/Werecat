package com.whicken.rule.expr;

import com.whicken.rule.RuleContext;

public class LTExpression extends BinaryExpression {
    public LTExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	return new Boolean(lhs.getDouble(context) < rhs.getDouble(context));
    }
}
