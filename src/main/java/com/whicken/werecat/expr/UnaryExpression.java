package com.whicken.werecat.expr;

public abstract class UnaryExpression extends Expression {
    Expression expr;
    public UnaryExpression(Expression expr) {
	this.expr = expr;
    }
    public Expression getOperand() {
	return expr;
    }
}
