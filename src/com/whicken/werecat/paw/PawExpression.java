package com.whicken.werecat.paw;

import com.whicken.werecat.expr.Expression;

public class PawExpression<T> {
    Expression expr;
    public PawExpression(Expression expr) {
	this.expr = expr;
    }
    public Object getValue(T object) {
	PawContext context = new PawContext<T>(object);
	return expr.getValue(context);
    }
}
