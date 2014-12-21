package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import com.whicken.werecat.RuleFactory;
import com.whicken.werecat.WerecatException;
import java.lang.reflect.*;
import java.util.List;

/**
 * This may not be particularly fast, since we do a full reflection
 * lookup each time getValue is invoked.  It's also one of the ugliest
 * classes in the codebase.
 */
public class DotMethodExpression extends UnaryExpression {
    String method;
    List<Expression> args;
    public DotMethodExpression(Expression expr, String method, List<Expression> args) {
	super(expr);
	this.method = method;
	this.args = args;
    }
    public Object getValue(RuleContext context) {
	Class c;
	Object l;
	if (expr instanceof ClassReference) {
	    c = ((ClassReference) expr).c;
	    l = null;
	} else {
	    l = expr.getValue(context);
	    if (l == null)
		throw new WerecatException("Dereferencing null");
	    c = l.getClass();
	}
	try {
	    Method m = RuleFactory.getMethod(c, method, args);
	    if (m != null) {
		Object[] a;
		if (args == null) {
		    a = null;
		} else {
		    a = new Object[args.size()];
		    for (int i = 0; i < args.size(); ++i)
			a[i] = args.get(i).getValue(context);
		}
		return m.invoke(l, a);
	    }
	    throw new WerecatException("Cannot resolve method: "+method);
	} catch (Throwable e) {
	    throw new WerecatException("Error invoking "+method, e);
	}
    }
    public String toString() {
	StringBuffer b = new StringBuffer();
	b.append(expr.toString());
	b.append(".");
	b.append(method);
	b.append("(");
	if (args != null) {
	    for (int i = 0; i < args.size(); ++i) {
		if (i > 0) b.append(", ");
		b.append(args.get(i).toString());
	    }
	}
	b.append(")");
	return b.toString();
    }
    public String getMethod() {
	return method;
    }
    public List<Expression> getArguments() {
	return args;
    }
}
