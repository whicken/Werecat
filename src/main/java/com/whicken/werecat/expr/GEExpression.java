package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import java.util.Date;

public class GEExpression extends BinaryExpression {
    public GEExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	Object l = lhs.getValue(context);
	Object r = rhs.getValue(context);
	// This is mostly useful if strings are involved
	if (l instanceof Date || r instanceof Date) {
	    Date ld = asDate(l);
	    Date rd = asDate(r);
	    return ld.getTime() >= rd.getTime() ?
		Boolean.TRUE : Boolean.FALSE;
	}
	// FR: If either side is not a number, do string comparison
	return asDouble(l) >= asDouble(r) ? Boolean.TRUE : Boolean.FALSE;
    }
    public String toString() {
	StringBuffer b = new StringBuffer();
	b.append(lhs.toString());
	b.append(" >= ");
	b.append(rhs.toString());
	return b.toString();
    }
}
