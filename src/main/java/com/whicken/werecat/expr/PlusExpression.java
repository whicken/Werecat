package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import org.joda.money.Money;

public class PlusExpression extends BinaryExpression {
    public PlusExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	Object l = lhs.getValue(context);
	Object r = rhs.getValue(context);
	if (l instanceof Money) {
	    Money lm = (Money) l;
	    if (r instanceof Money) {
		return lm.plus((Money) r);
	    } else {
		return lm.plus(asDouble(r));
	    }
	} else if (r instanceof Money) {
	    Money rm = (Money) r;
	    return rm.plus(asDouble(l));
	}
	if (isNumber(l) && isNumber(r))
	    return new Double(asDouble(l)+asDouble(r));
	return asString(l)+asString(r);
    }
    public String toString() {
	StringBuffer b = new StringBuffer();
	b.append(lhs.toString());
	b.append(" + ");
	b.append(rhs.toString());
	return b.toString();
    }
}
