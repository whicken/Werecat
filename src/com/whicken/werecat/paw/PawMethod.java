package com.whicken.werecat.paw;

import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.RuleContext;
import java.lang.reflect.*;

/**
 * This class hides the RuleContext implementation behind a PawContext.
 * Similar to MethodExpression, just with one extra unwrapping
 */
class PawMethod extends Expression {
    Method method;
    PawMethod(Method method) {
	this.method = method;
    }
    public Object getValue(RuleContext context) {
	try {
	    Object obj = ((PawContext) context).object;
	    return method.invoke(obj, (Object[])null);
	} catch (Throwable t) {
	    throw new RuntimeException(t.getMessage());
	}
    }
}
