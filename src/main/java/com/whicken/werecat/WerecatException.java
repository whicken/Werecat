package com.whicken.werecat;

/**
 * Subclass RuntimeException, so we don't need to declare it, but still give us
 * an easy way to catch internal issues so we can handle them differently.
 *
 * If exception was thrown while evaluating a rule, the rule field will be set
 * with the offending rule.
 */
public class WerecatException extends RuntimeException {
    Rule rule;
    public WerecatException(String s) {
	super(s);
    }
    public WerecatException(String s, Throwable t) {
	super(s, t);
    }
    public WerecatException(Throwable t) {
	super(t);
    }
    public WerecatException(Throwable t, Rule rule) {
	super(t);
	this.rule = rule;
    }
    public void setRule(Rule rule) {
	this.rule = rule;
    }
    public Rule getRule() {
	return rule;
    }
}
