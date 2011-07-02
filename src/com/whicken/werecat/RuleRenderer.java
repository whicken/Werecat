package com.whicken.werecat;

import com.whicken.werecat.expr.Expression;
import java.io.File;
import java.io.IOException;

public class RuleRenderer {
    StringBuffer b = new StringBuffer();
    public RuleRenderer() {
    }
    public void render(RuleEngine engine) {
	for (Rule rule : engine.rules.values())
	    render(rule);
    }
    public void render(Rule rule) {
	b.append("<p><b><a name=\""+rule.tag+"\">"+rule.tag+"</a></b></p>\n");
        b.append("<p><i>"+rule.description+"</i></p>\n");
        b.append("<p>Condition: "+rule.condition+"</p>\n");

	if (rule.accept != null || rule.acceptList != null) {
	    b.append("<p>Accept: ");
	    boolean first = true;
	    if (rule.acceptList != null) {
		for (Expression e : rule.acceptList) {
		    if (first) first = false;
		    else b.append(", ");
		    b.append(e.toString());
		}
	    }
	    if (rule.accept != null) {
		if (first) first = false;
		else b.append(", ");
		b.append("<a href=\"#"+rule.accept.tag+"\">"+rule.accept.tag+"</a>");
	    }
	    b.append("</p>\n");
	}
	if (rule.decline != null || rule.declineList != null) {
	    b.append("<p>Decline: ");
	    boolean first = true;
	    if (rule.declineList != null) {
		for (Expression e : rule.declineList) {
		    if (first) first = false;
		    else b.append(", ");
		    b.append(e.toString());
		}
	    }
	    if (rule.decline != null) {
		if (first) first = false;
		else b.append(", ");
		b.append("<a href=\"#"+rule.decline.tag+"\">"+rule.decline.tag+"</a>");
	    }
	    b.append("</p>\n");
	}
    }
    public String toString() {
	return b.toString();
    }
    public static void main(String[] args) {
	RuleFactory factory = new BlindFactory();
	for (String arg : args) {
	    try {
		File file = new File(arg);
		RuleEngine engine = new RuleEngine();
		engine.load(file, factory);
		RuleRenderer r = new RuleRenderer();
		r.render(engine);
		System.out.println(r.toString());
	    } catch (IOException e) {
		System.out.println("Error for "+arg);
	    }
	}
    }
}
