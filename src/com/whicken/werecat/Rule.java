package com.whicken.werecat;

import com.whicken.werecat.expr.Expression;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Rule {
    String tag;
    String description;
    Expression condition;
    Expression[] acceptList;
    Rule accept;
    Expression[] declineList;
    Rule decline;
    public Rule(String tag) {
	this.tag = tag;
    }
    public int getDepth() {
	int left = 0;
	if (accept != null)
	    left = accept.getDepth();
	int right = 0;
	if (decline != null)
	    right = decline.getDepth();
	return 1+Math.max(left, right);
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
		if (acceptList != null)
		    for (Expression e : acceptList)
			e.getValue(context);
		if (accept != null)
		    accept.evaluate(context);
	    } else {
		if (declineList != null)
		    for (Expression e : declineList)
			e.getValue(context);
		if (decline != null)
		    decline.evaluate(context);
	    }
	} catch (Throwable e) {
	    throw new RuntimeException(description+": "+e.getMessage(), e);
	}
    }
}
