package com.whicken.rule.expr;

import com.whicken.rule.RuleContext;

public abstract class BinaryExpression extends Expression {
    Expression lhs;
    Expression rhs;
    public BinaryExpression(Expression lhs, Expression rhs) {
	this.lhs = lhs;
	this.rhs = rhs;
    }
}
