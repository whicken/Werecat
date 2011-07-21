package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class NegateExpression extends UnaryExpression {
    public NegateExpression(Expression expr) {
	super(expr);
    }
    public Object getValue(RuleContext context) {
	return new Double(-expr.getDouble(context));
    }
    public String toString() {
	return "-"+expr.toString();
    }
}
