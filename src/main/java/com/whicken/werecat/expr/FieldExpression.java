package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import com.whicken.werecat.WerecatException;
import java.lang.reflect.*;

/**
 * Call a simple field in the RuleContext class via reflection
 */
public class FieldExpression extends Expression {
    Field field;
    public FieldExpression(Field field) {
	this.field = field;
    }
    public Object getValue(RuleContext context) {
	try {
	    return field.get(context);
	} catch (Throwable t) {
	    throw new WerecatException("Error accessing field "+field, t);
	}
    }
    public String toString() {
	return field.getName();
    }
}
