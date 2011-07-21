package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class NotExpression extends UnaryExpression {
    public NotExpression(Expression expr) {
	super(expr);
    }
    public Object getValue(RuleContext context) {
	return asBoolean(expr) ? Boolean.FALSE : Boolean.TRUE;
    }
    public String toString() {
	return "!"+expr.toString();
    }
}
