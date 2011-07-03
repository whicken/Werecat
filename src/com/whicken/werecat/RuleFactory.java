package com.whicken.werecat;

import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.expr.ArrayExpression;
import com.whicken.werecat.expr.DotExpression;
import com.whicken.werecat.expr.FieldExpression;
import com.whicken.werecat.expr.MethodExpression;
import com.whicken.werecat.expr.ClassReference;
import java.lang.reflect.*;
import java.util.List;

/**
 * Helper class for instantiating a RuleEngine.  Subclass to provide
 * appropriate hooks for expression parser.
 */
public class RuleFactory {
    protected Class context;
    public RuleFactory(Class context) {
	this.context = context;
    }
    // FR: Support "import" type syntax to reference static methods, etc
    protected Class getClass(String name) {
	try {
	    Package p = context.getPackage();
	    Class c = context.forName(p.getName()+"."+name);
	    return c;
	} catch (Throwable e) {
	    // NoClassDefFoundError
	    // ClassNotFoundException
	}
	return null;
    }
    /**
     * Requires the method to be public
     */
    protected Method getMethod(String method, List<Expression> args) {
	try {
	    if (args == null) {
		Method m = context.getDeclaredMethod(method, (Class[]) null);
		if ((m.getModifiers() & Modifier.PUBLIC) != 0)
		    return m;
	    } else {
		// More work here, since we don't have types
		Method[] list = context.getDeclaredMethods();
		for (Method m : list) {
		    if ((m.getModifiers() & Modifier.PUBLIC) == 0)
			continue;
		    if (!m.getName().equals(method))
			continue;
		    Class[] p = m.getParameterTypes();
		    if (p.length == args.size())
			return m;
		}
	    }
	} catch (NoSuchMethodException e) {
	    // Not an error
	}
	return null;
    }
    /**
     * Override this is you want something different than reflection.
     */
    public Expression createField(String key) {
	try {
	    Field field = context.getDeclaredField(key);
	    if ((field.getModifiers() & Modifier.PUBLIC) != 0)
		return new FieldExpression(field);
	} catch (NoSuchFieldException e) {
	}

	String method;
	if (Character.isLowerCase(key.charAt(0))) {
	    char c = Character.toUpperCase(key.charAt(0));
	    method = "get" + c + key.substring(1);
	} else {
	    method = "get"+key;
	}
	Method m = getMethod(method, null);
	if (m != null)
	    return new MethodExpression(m, null);

	Class c = getClass(key);
	if (c != null)
	    return new ClassReference(c);

	return null;
    }
    /**
     * Override this is you want something different than reflection.
     */
    public Expression createMethod(String method, List<Expression> args) {
	Method m = getMethod(method, args);
	if (m != null)
	    return new MethodExpression(m, args);
	return null;
    }
    public Expression createCompoundExpression(Expression lhs, Object rhs) {
	if (rhs instanceof String) {
	    // TODO: If lhs instanceof ClassReference, resolve all issues
	    // at parse time instead of evaluate time
	    // lhs.thing
	    return new DotExpression(lhs, (String) rhs);
	} else if (rhs instanceof Expression) {
	    // lhs[thing]
	    return new ArrayExpression(lhs, (Expression) rhs);
	}
	throw new RuntimeException("Unexpected case in createCompoundExpression");
    }
}
