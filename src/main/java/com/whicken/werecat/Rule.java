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
    public String toString() {
        return tag+": "+description;
    }
    public String getExpressionText() {
	return condition.toString();
    }
    public void evaluate(RuleContext context) {
	evaluate(this, context);
    }
    // Remove tail end recursion to minimize impact on stack (also keeps
    // exceptions from getting nested like crazy)
    public static void evaluate(Rule rule, RuleContext context) {
	try {
	    while (true) {
		Object o = rule.condition.getValue(context);
		// For debugging: System.out.println(rule.description+": "+o);
		Action[] actions;
		if (Expression.asBoolean(o))
		    actions = rule.accept;
		else
		    actions = rule.decline;
		if (actions == null)
		    break;

		for (int i = 0; i < actions.length-1; ++i)
		    actions[i].evaluate(context);
		
		Action action = actions[actions.length-1];
		if (!(action instanceof RuleAction)) {
		    action.evaluate(context);
		    break;
		}

		rule = ((RuleAction) action).rule;
	    }
	} catch (WerecatException e) {
	    e.setRule(rule);
	    throw e;
	} catch (Throwable e) {
	    throw new WerecatException(e, rule);
	}
    }
}
