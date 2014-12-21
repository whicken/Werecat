package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class NotExpression extends UnaryExpression {
    public NotExpression(Expression expr) {
	super(expr);
    }
    public Object getValue(RuleContext context) {
	return asBoolean(expr.getValue(context)) ? Boolean.FALSE : Boolean.TRUE;
    }
    public String toString() {
	// TODO: Improve parentheses logic
	if (expr instanceof BinaryExpression)
	    return "!("+expr.toString()+")";
        else
	    return "!"+expr.toString();
    }
}
