package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import com.whicken.werecat.WerecatException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexpNoMatchExpression extends BinaryExpression {
    public RegexpNoMatchExpression(Expression lhs, Expression rhs) {
	super(lhs, rhs);
    }
    public Object getValue(RuleContext context) {
	Object l = lhs.getValue(context);
	Object r = rhs.getValue(context);
	if (l == null) {
	    if (r == null)
		return Boolean.TRUE;
	    return Boolean.FALSE;
	} else if (r == null) {
	    return Boolean.FALSE;
	}
	if (l instanceof String) {
	    Pattern pattern;
	    if (r instanceof Pattern) {
		pattern = (Pattern) r;
	    } else {
		pattern = Pattern.compile(asString(r));
	    }
	    Matcher m = pattern.matcher(asString(l));
	    return m.find() ? Boolean.FALSE : Boolean.TRUE;
	}
	throw new WerecatException("Unsupported types in !~: "+l+" !~ "+r);
    }
    public String toString() {
	StringBuffer b = new StringBuffer();
	b.append(lhs.toString());
	b.append(" !~ ");
	b.append(rhs.toString());
	return b.toString();
    }
}
