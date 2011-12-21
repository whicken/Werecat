package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class PlusExpression extends BinaryExpression {
    public PlusExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	Object l = lhs.getValue(context);
	Object r = rhs.getValue(context);
	if (isNumber(l) && isNumber(r))
	    return new Double(asDouble(l)+asDouble(r));
	return asString(l)+asString(r);
    }
    public String toString() {
	StringBuffer b = new StringBuffer();
	b.append(lhs.toString());
	b.append(" + ");
	b.append(rhs.toString());
	return b.toString();
    }
}
