package com.whicken.werecat;

public class RuleAction extends Action {
    Rule rule;
    RuleAction(Rule rule) {
	this.rule = rule;
    }
    public void evaluate(RuleContext context) {
	rule.evaluate(context);
    }
    public String toString() {
	return rule.tag;
    }
}
