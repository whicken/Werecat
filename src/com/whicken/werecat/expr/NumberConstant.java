package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class NumberConstant extends Expression {
    Double value;
    public NumberConstant(double v) {
	value = new Double(v);
    }
    public Object getValue(RuleContext context) {
	return value;
    }
}
