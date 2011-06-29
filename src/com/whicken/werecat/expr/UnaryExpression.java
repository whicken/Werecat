package com.whicken.rule.expr;

public abstract class UnaryExpression extends Expression {
    Expression expr;
    public UnaryExpression(Expression expr) {
	this.expr = expr;
    }
}
