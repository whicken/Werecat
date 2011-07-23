package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import com.whicken.werecat.WerecatException;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArrayExpression extends BinaryExpression {
    public ArrayExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	Object l = lhs.getValue(context);
	// TODO: regular arrays (l.getClass().isArray())
	if (l instanceof List) {
	    int index = (int) rhs.asDouble(context);
	    return ((List) l).get(index);
	} else if (l instanceof Map) {
	    // Note: This requires the key to be of the right type
	    Object index = rhs.getValue(context);
	    return ((Map) l).get(index);
	} else if (l instanceof Set) {
	    // Note: This requires the key to be of the right type
	    Object index = rhs.getValue(context);
	    return ((Set) l).contains(index) ? Boolean.TRUE : Boolean.FALSE;
	}
	throw new WerecatException("Unable to index: "+
				   (l == null ? "null" : l.getClass()));
    }
    public String toString() {
	StringBuffer b = new StringBuffer();
	b.append(lhs.toString());
	b.append("[");
	b.append(rhs.toString());
	b.append("]");
	return b.toString();
    }
}
