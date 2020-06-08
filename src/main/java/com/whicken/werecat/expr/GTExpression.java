package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import com.whicken.werecat.WerecatException;
import java.util.Date;

public class GTExpression extends BinaryExpression {
    public GTExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	Object l = lhs.getValue(context);
	Object r = rhs.getValue(context);
	// This is mostly useful if strings are involved
	if (l instanceof Date || r instanceof Date) {
	    Date ld = asDate(l);
	    Date rd = asDate(r);
	    return ld.getTime() > rd.getTime() ?
		Boolean.TRUE : Boolean.FALSE;
	}
	if (isNumber(l) && isNumber(r)) {
	    return asDouble(l) > asDouble(r) ? Boolean.TRUE : Boolean.FALSE;
	}
	if (isStringComparable(l) && isStringComparable(r)) {
	    return asString(l).compareTo(asString(r)) > 0 ? Boolean.TRUE : Boolean.FALSE;
	}
	throw new WerecatException("Unsupported types in >: "+l+" > "+r);
    }
    public String toString() {
	StringBuffer b = new StringBuffer();
	b.append(lhs.toString());
	b.append(" > ");
	b.append(rhs.toString());
	return b.toString();
    }
}
