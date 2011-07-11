package com.whicken.werecat;

import com.whicken.werecat.expr.Expression;

public class ExpressionAction extends Action {
    Expression expr;
    ExpressionAction(Expression expr) {
	this.expr = expr;
    }
    public void evaluate(RuleContext context) {
	expr.getValue(context);
    }
    public String toString() {
	return expr.toString();
    }
}
