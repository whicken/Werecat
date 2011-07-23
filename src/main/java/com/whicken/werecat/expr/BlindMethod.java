package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import com.whicken.werecat.WerecatException;
import java.util.List;

public class BlindMethod extends Expression {
    protected String method;
    protected Expression[] args;
    public BlindMethod(String method, List<Expression> args) {
	this.method = method;
	if (args == null || args.size() == 0) {
	    this.args = null;
	} else {
	    this.args = new Expression[args.size()];
	    for (int i = 0; i < args.size(); ++i)
		this.args[i] = args.get(i);
	}
    }
    public Object getValue(RuleContext context) {
	throw new WerecatException("BlindMethod cannot be evaluated.");
    }
    public String toString() {
	StringBuffer b = new StringBuffer(method);
	b.append("(");
	if (args != null) {
	    for (int i = 0; i < args.length; ++i) {
		if (i > 0) b.append(", ");
		b.append(args[i].toString());
	    }
	}
	b.append(")");
	return b.toString();
    }
}
