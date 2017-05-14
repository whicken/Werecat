package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import org.joda.money.Money;

public class MinusExpression extends BinaryExpression {
    public MinusExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	Object l = lhs.getValue(context);
	Object r = rhs.getValue(context);
	if (l instanceof Money) {
	    Money lm = (Money) l;
	    if (r instanceof Money) {
		return lm.minus((Money) r);
	    } else {
		return lm.minus(asDouble(r));
	    }
	} else if (r instanceof Money) {
	    Money rm = (Money) r;
	    Money lm = rm.withAmount(asDouble(l));
	    return lm.minus(rm);
	}
	return new Double(asDouble(l)-asDouble(r));
    }
    public String toString() {
	StringBuffer b = new StringBuffer();
	b.append(lhs.toString());
	b.append(" - ");
	b.append(rhs.toString());
	return b.toString();
    }
}
