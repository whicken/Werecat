package com.whicken.werecat;

import com.whicken.werecat.expr.Expression;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;

/**
 * This class has a log4j logger attached.
 */
public class Rule {
    private static final Logger log = Logger.getLogger(Rule.class);

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
	evaluate(this, context);
    }
    // Remove tail end recursion to minimize impact on stack (also keeps
    // exceptions from getting nested like crazy)
    public static void evaluate(Rule rule, RuleContext context) {
	if (log.isTraceEnabled()) {
	    evaluateWithTrace(rule, context);
	    return;
	}
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
	    log.error(e);
	    throw e;
	} catch (Throwable e) {
	    log.error(e);
	    throw new WerecatException(e, rule);
	}
    }
    /**
     * Same as regular evaluate, except it does verbose logging. Separated
     * to minimized performance penalty when no logging is enabled.
     */
    private static void evaluateWithTrace(Rule rule, RuleContext context)
    {
	while (true) {
	    Object o = rule.condition.getValue(context);
	    Action[] actions;
	    if (Expression.asBoolean(o)) {
		log.trace(rule.description+": "+o+" -> accept");
		actions = rule.accept;
	    } else {
		log.trace(rule.description+": "+o+" -> decline");
		actions = rule.decline;
	    }
	    if (actions == null)
		break;
	    
	    for (int i = 0; i < actions.length-1; ++i) {
		log.trace("  Executing "+actions[i]);
		actions[i].evaluate(context);
	    }
	    
	    Action action = actions[actions.length-1];
	    log.trace("  Executing "+action);
	    if (!(action instanceof RuleAction)) {
		action.evaluate(context);
		break;
	    }
	    
	    rule = ((RuleAction) action).rule;
	}
    }
}
