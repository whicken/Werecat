package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import com.whicken.werecat.WerecatException;
import java.lang.reflect.*;
import java.util.List;

/**
 * Call a simple method in the RuleContext class via reflection
 */
public class MethodExpression extends Expression {
    protected Method method;
    protected Expression[] args;
    public MethodExpression(Method method, List<Expression> args) {
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
	try {
	    Object[] a;
	    if (args == null) {
		a = null;
	    } else {
		a = new Object[args.length];
		for (int i = 0; i < args.length; ++i)
		    a[i] = args[i].getValue(context);
	    }
	    return method.invoke(context, a);
	} catch (Throwable t) {
	    throw new WerecatException("Error invoking "+method, t);
	}
    }
    public String toString() {
	StringBuffer b = new StringBuffer(method.getName());
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
    public String getMethod() {
	return method.getName();
    }
    public Expression[] getArguments() {
	return args;
    }
}
