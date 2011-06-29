package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import java.lang.reflect.*;

/**
 * Call a simple method in the RuleContext class via reflection
 */
public class MethodExpression extends Expression {
    Method method;
    public MethodExpression(Method method) {
	this.method = method;
    }
    public Object getValue(RuleContext context) {
	try {
	    return method.invoke(context, (Object[])null);
	} catch (Throwable t) {
	    throw new RuntimeException(t.getMessage());
	}
    }
}
