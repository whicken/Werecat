package com.whicken.werecat.paw;

import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.expr.MethodExpression;
import com.whicken.werecat.RuleContext;
import java.lang.reflect.*;
import java.util.List;

/**
 * This class hides the RuleContext implementation behind a PawContext.
 * Similar to MethodExpression, just with one extra unwrapping
 */
class PawMethod extends MethodExpression {
    PawMethod(Method method, List<Expression> args) {
	super(method, args);
    }
    public Object getValue(RuleContext context) {
	Object obj = ((PawContext) context).object;
	try {
	    Object[] a;
	    if (args == null) {
		a = null;
	    } else {
		a = new Object[args.length];
		for (int i = 0; i < args.length; ++i)
		    a[i] = args[i].getValue(context);
	    }
	    return method.invoke(obj, a);
	} catch (Throwable t) {
	    throw new RuntimeException(t.getMessage());
	}

    }
}
