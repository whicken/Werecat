package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class StringConstant extends Expression {
    String str;
    public StringConstant(String s) {
	str = s;
    }
    public Object getValue(RuleContext context) {
	return str;
    }
}
