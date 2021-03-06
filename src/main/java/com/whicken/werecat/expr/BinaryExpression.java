package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public abstract class BinaryExpression extends Expression {
    Expression lhs;
    Expression rhs;
    public BinaryExpression(Expression lhs, Expression rhs) {
	this.lhs = lhs;
	this.rhs = rhs;
    }
    public Expression getLHS() {
	return lhs;
    }
    public Expression getRHS() {
	return rhs;
    }
    public void accept(ExpressionVisitor v) {
	super.accept(v);
	lhs.accept(v);
	rhs.accept(v);
    }
}
