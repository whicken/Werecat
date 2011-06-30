package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexpMatchExpression extends BinaryExpression {
    public RegexpMatchExpression(Expression lhs, Expression rhs) {
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
	    if (m.find()) {
		return new Integer(m.start());
	    }
	    return null;
	}
	throw new RuntimeException("Unsupported types in =~");
    }
}
