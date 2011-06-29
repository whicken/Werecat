package com.whicken.rule.expr;

import com.whicken.rule.RuleContext;

public class StringConstant extends Expression {
    String str;
    public StringConstant(String s) {
	str = s;
    }
    public Object getValue(RuleContext context) {
	return str;
    }
}
