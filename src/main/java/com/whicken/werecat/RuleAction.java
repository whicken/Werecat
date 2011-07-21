package com.whicken.werecat;

class RuleAction extends Action {
    Rule rule;
    RuleAction(Rule rule) {
	this.rule = rule;
    }
    void evaluate(RuleContext context) {
	rule.evaluate(context);
    }
    public String toString() {
	return rule.tag;
    }
}
