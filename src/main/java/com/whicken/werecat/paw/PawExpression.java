package com.whicken.werecat.paw;

import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.expr.ExpressionVisitor;

public class PawExpression<T> {
    Expression expr;
    public PawExpression(Expression expr) {
	this.expr = expr;
    }
    public Object getValue(T object) {
	PawContext context = new PawContext<T>(object);
	return expr.getValue(context);
    }
    public boolean getBoolean(T object) {
	PawContext context = new PawContext<T>(object);
	return expr.getBoolean(context);
    }
    public String toString() {
	return expr.toString();
    }
    public Expression getExpr() {
	return expr;
    }
    public void accept(ExpressionVisitor v) {
	expr.accept(v);
    }
}
