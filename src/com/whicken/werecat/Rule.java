package com.whicken.werecat;

import com.whicken.werecat.expr.Expression;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Rule {
    String tag;
    String description;
    Expression condition;
    Action[] accept;
    Action[] decline;
    public Rule(String tag) {
	this.tag = tag;
    }
    public String getDescription() {
	return description;
    }
    public String getTag() {
	return tag;
    }
    public String getExpressionText() {
	return condition.toString();
    }
    public void evaluate(RuleContext context) {
	try {
	    Object o = condition.getValue(context);
	    // For debugging: System.out.println(description+": "+o);
	    if (Expression.asBoolean(o)) {
		if (accept != null)
		    for (Action a : accept)
			a.evaluate(context);
	    } else {
		if (decline != null)
		    for (Action a : decline)
			a.evaluate(context);
	    }
	} catch (Throwable e) {
	    throw new RuntimeException(description+": "+e.getMessage(), e);
	}
    }
}
