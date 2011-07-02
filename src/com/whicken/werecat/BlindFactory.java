package com.whicken.werecat;

import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.expr.SimpleConstant;
import java.lang.reflect.*;
import java.util.List;

/**
 * RuleFactory with NO concrete implementation, useful for editing outside
 * an actual context, but you'll get no validation.
 */
public class BlindFactory extends RuleFactory {
    public BlindFactory() {
	super(Object.class);
    }
    protected Method getMethod(String method, List<Expression> args) {
	try {
	    return context.getDeclaredMethod("toString", (Class[]) null);
	} catch (NoSuchMethodException e) {
	}
	return null;
    }
    public Expression createField(String key) {
	return new SimpleConstant(null);
    }
    public Expression createMethod(String method, List<Expression> args) {
	return new SimpleConstant(null);
    }
    public Expression createCompoundExpression(Expression lhs, Object rhs) {
	return new SimpleConstant(null);
    }
}
