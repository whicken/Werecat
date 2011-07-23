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

	if (rule.accept != null) {
	    b.append("<p>Accept: ");
	    boolean first = true;
	    if (rule.accept != null) {
		for (Action a : rule.accept) {
		    if (first) first = false;
		    else b.append("; ");
		    if (a instanceof RuleAction) {
			b.append("<a href=\"#"+a.toString()+"\">"+a.toString()+"</a>");
		    } else {
			b.append(a.toString());
		    }
		}
	    }
	    b.append("</p>\n");
	}
	if (rule.decline != null) {
	    b.append("<p>Decline: ");
	    boolean first = true;
	    if (rule.decline != null) {
		for (Action a : rule.decline) {
		    if (first) first = false;
		    else b.append("; ");
		    if (a instanceof RuleAction)
			b.append("<a href=\"#"+a.toString()+"\">"+a.toString()+"</a>");
		    else
			b.append(a.toString());
		}
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
	    } catch (Throwable e) {
		System.out.println("Error for "+arg);
	    }
	}
    }
}
