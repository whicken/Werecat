package com.whicken.werecat.paw;

import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.RuleContext;
import java.lang.reflect.*;

/**
 * This class hides the RuleContext implementation behind a PawContext.
 * Similar to FieldExpression, just with one extra unwrapping
 */
class PawField extends Expression {
    Field field;
    PawField(Field field) {
	this.field = field;
    }
    public Object getValue(RuleContext context) {
	try {
	    Object obj = ((PawContext) context).object;
	    return field.get(obj);
	} catch (Throwable t) {
	    throw new RuntimeException(t.getMessage());
	}
    }
    public String toString() {
	return field.getName();
    }
}
