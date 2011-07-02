package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class MultExpression extends BinaryExpression {
    public MultExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	return new Double(lhs.getDouble(context)*rhs.getDouble(context));
    }
    public String toString() {
	StringBuffer b = new StringBuffer();
	b.append(lhs.toString());
	b.append(" * ");
	b.append(rhs.toString());
	return b.toString();
    }
}
