package com.whicken.werecat;

import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.expr.MethodExpression;
import java.lang.reflect.*;

/**
 * Helper class for instantiating a RuleEngine.  Subclass to provide
 * appropriate hooks for expression parser.
 */
public class RuleFactory {
    Class context;
    public RuleFactory(Class context) {
	this.context = context;
    }
    protected Method getMethod(String method) {
	try {
	    // TODO: Require the method to be public
	    return context.getDeclaredMethod(method, (Class[]) null);
	} catch (NoSuchMethodException e) {
	    // Not an error
	}
	return null;
    }
    /**
     * Override this is you want something different than reflection.
     */
    public Expression createExpression(String key) {
	// First, use reflection to see if the context has an accessor
	String method;
	if (Character.isLowerCase(key.charAt(0))) {
	    char c = Character.toUpperCase(key.charAt(0));
	    method = "get" + c + key.substring(1);
	} else {
	    method = "get"+key;
	}
	Method m = getMethod(method);
	if (m != null)
	    return new MethodExpression(m);
	return null;
    }
    /**
     * Override this is you want something different than reflection.
     */
    public Expression createMethod(String method) {
	Method m = getMethod(method);
	if (m != null)
	    return new MethodExpression(m);
	return null;
    }
    public Expression createSubExpression(String namespace, String key) {
	// TODO: Fancier reflection?
	return null;
    }
}
