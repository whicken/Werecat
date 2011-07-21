package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;

public class BlindField extends Expression {
    String field;
    public BlindField(String field) {
	this.field = field;
    }
    public Object getValue(RuleContext context) {
	throw new RuntimeException("BlindField cannot be evaluated.");
    }
    public String toString() {
	return field;
    }
}
