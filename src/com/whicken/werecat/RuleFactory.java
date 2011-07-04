package com.whicken.werecat;

import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.expr.ArrayExpression;
import com.whicken.werecat.expr.DotExpression;
import com.whicken.werecat.expr.DotMethodExpression;
import com.whicken.werecat.expr.FieldExpression;
import com.whicken.werecat.expr.MethodExpression;
import com.whicken.werecat.expr.ClassReference;
import com.whicken.werecat.expr.IdentifierCall;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Helper class for instantiating a RuleEngine.  Subclass to provide
 * appropriate hooks for expression parser.
 */
public class RuleFactory {
    protected Class context;
    protected Map<String, Class> importedClasses;
    protected List<String> importedPackages;
    public RuleFactory(Class context) {
	this.context = context;
	importedClasses = new TreeMap<String, Class>();
	importedPackages = new ArrayList<String>();
    }
    public void addImport(String path) {
	if (path.endsWith(".*")) {
	    importedPackages.add(path.substring(0, path.length()-1));
	} else {
	    try {
		Class c = Class.forName(path);
		importedClasses.put(c.getSimpleName(), c);
	    } catch (Throwable e) {
		// NoClassDefFoundError
		// ClassNotFoundException
		throw new RuntimeException("Cannot import class "+path,
					   e);
	    }
	}
    }
    protected Class getClass(String name) {
	try {
	    Package p = context.getPackage();
	    Class c = context.forName(p.getName()+"."+name);
	    return c;
	} catch (Throwable e) {
	    // NoClassDefFoundError
	    // ClassNotFoundException
	}
	Class c = importedClasses.get(name);
	if (c != null)
	    return c;
	for (String p : importedPackages) {
	    try {
		return context.forName(p+name);
	    } catch (Throwable e) {
		// NoClassDefFoundError
		// ClassNotFoundException
	    }
	}
	return null;
    }
    /**
     * Requires the method to be public
     */
    public static Method getMethod(Class context,
				   String method, List<Expression> args) {
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
    protected Method getMethod(String method, List<Expression> args) {
	return getMethod(context, method, args);
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
	} else if (rhs instanceof IdentifierCall) {
	    // TODO: If lhs instanceof ClassReference, resolve all issues
	    // at parse time instead of evaluate time
	    IdentifierCall ic = (IdentifierCall) rhs;
	    return new DotMethodExpression(lhs, ic.name, ic.args);
	} else if (rhs instanceof Expression) {
	    // lhs[thing]
	    return new ArrayExpression(lhs, (Expression) rhs);
	}
	throw new RuntimeException("Unexpected case in createCompoundExpression");
    }
}
