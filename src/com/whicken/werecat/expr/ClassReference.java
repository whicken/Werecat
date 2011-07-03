package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import java.lang.reflect.*;

/**
 * This class is only used to store class references before resolving
 * DotExpressions (i.e. accessing fields or methods for a class directly)
 */
public class ClassReference extends Expression {
    Class c;
    public ClassReference(Class c) {
	this.c = c;
    }
    public Object getValue(RuleContext context) {
	throw new RuntimeException("Bare class name cannnot be referenced");
    }
    public String toString() {
	return c.getName();
    }
}
