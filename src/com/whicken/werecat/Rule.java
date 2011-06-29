package com.whicken.rule;

import com.whicken.rule.expr.Expression;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Rule {
    String tag;
    String description;
    Expression condition;
    Method acceptMethod;
    Rule accept;
    Method declineMethod;
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
	    System.out.println(description+": "+o);
	    if (Expression.asBoolean(o)) {
		if (acceptMethod != null)
		    acceptMethod.invoke(context, (Object[]) null);
		if (accept != null)
		    accept.evaluate(context);
	    } else {
		if (declineMethod != null)
		    declineMethod.invoke(context, (Object[]) null);
		if (decline != null)
		    decline.evaluate(context);
	    }
	} catch (IllegalAccessException e) {
	    throw new RuntimeException(e.getMessage());
	} catch (IllegalArgumentException e) {
	    throw new RuntimeException(e.getMessage());
	} catch (InvocationTargetException e) {
	    throw new RuntimeException(e.getMessage());
	}
    }
}
