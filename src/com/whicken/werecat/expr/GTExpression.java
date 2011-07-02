package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class GTExpression extends BinaryExpression {
    public GTExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	// FR: If either side is not a number, do string comparison
	return lhs.getDouble(context) > rhs.getDouble(context) ?
	    Boolean.TRUE : Boolean.FALSE;
    }
    public String toString() {
	StringBuffer b = new StringBuffer();
	b.append(lhs.toString());
	b.append(" > ");
	b.append(rhs.toString());
	return b.toString();
    }
}
