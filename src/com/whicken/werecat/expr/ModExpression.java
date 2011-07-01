package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class ModExpression extends BinaryExpression {
    public ModExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	return new Double(((long) lhs.getDouble(context))%
			  ((long) rhs.getDouble(context)));
    }
}
