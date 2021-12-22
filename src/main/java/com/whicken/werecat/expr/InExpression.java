package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import com.whicken.werecat.WerecatException;
import java.util.List;

public class InExpression extends Expression {
    Expression lhs;
    Expression[] rhs;
    public InExpression(Expression lhs, List<Expression> rhs) {
        this.lhs = lhs;
	if (rhs == null || rhs.size() == 0) {
	    throw new WerecatException("In requires at least one option");
	} else {
	    this.rhs = new Expression[rhs.size()];
	    for (int i = 0; i < rhs.size(); ++i)
		this.rhs[i] = rhs.get(i);
	}
    }
    public Expression getLHS() {
	return lhs;
    }
    public Expression[] getOptions() {
	return rhs;
    }
    public Object getValue(RuleContext context) {
	Object l = lhs.getValue(context);
	if (l == null)
	    return Boolean.FALSE;
	for (int i = 0; i < rhs.length; ++i) {
	    Object r = rhs[i].getValue(context);
	    if (r != null) {
	        if (isNumber(l)) {
	            if (isNumber(r)) {
			if (asDouble(l) == asDouble(r))
			    return Boolean.TRUE;
		    } else {
	                String rstr = asString(r);
	                if (isStringNumber(rstr)) {
		            double d = Double.parseDouble(rstr);
		            if (asDouble(l) == d)
			        return Boolean.TRUE;
	                }
		    }
	        } else if (isNumber(r)) {
	            String lstr = asString(l);
	            if (isStringNumber(lstr)) {
		        double d = Double.parseDouble(lstr);
		        if (d == asDouble(r))
			    return Boolean.TRUE;
	            }
	        } else {
	            // TODO: Dates
		    if (asString(l).equals(asString(r)))
		        return Boolean.TRUE;
		}
	    }
	}
	return Boolean.FALSE;
    }
    public String toString() {
	StringBuffer b = new StringBuffer();
	b.append(lhs.toString());
	b.append(" in ");
	b.append("(");
	for (int i = 0; i < rhs.length; ++i) {
	    if (i > 0) b.append(", ");
	    b.append(rhs[i].toString());
	}
	b.append(")");
	return b.toString();
    }
    public void accept(ExpressionVisitor v) {
	super.accept(v);
	lhs.accept(v);
	for (int i = 0; i < rhs.length; ++i) {
	    rhs[i].accept(v);
	}
    }
}
