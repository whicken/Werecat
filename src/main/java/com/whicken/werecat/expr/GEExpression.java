package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class GEExpression extends BinaryExpression {
    public GEExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	return lhs.getDouble(context) >= rhs.getDouble(context) ?
	    Boolean.TRUE : Boolean.FALSE;
    }
    public String toString() {
	StringBuffer b = new StringBuffer();
	b.append(lhs.toString());
	b.append(" >= ");
	b.append(rhs.toString());
	return b.toString();
    }
}
