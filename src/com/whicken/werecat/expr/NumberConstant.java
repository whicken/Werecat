package com.whicken.rule.expr;

import com.whicken.rule.RuleContext;

public class NumberConstant extends Expression {
    Double value;
    public NumberConstant(double v) {
	value = new Double(v);
    }
    public Object getValue(RuleContext context) {
	return value;
    }
}
