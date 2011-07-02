package com.whicken.werecat;

import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.parser.ExpressionParser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.json.*;

public class RuleEngine {
    Map<String, Rule> rules;
    Rule top;
    public RuleEngine() {
	rules = new TreeMap<String, Rule>();
    }
    static Expression[] getList(List<Expression> calls) {
	if (calls.size() == 0)
	    return null;
	Expression[] list = new Expression[calls.size()];
	for (int i = 0; i < list.length; ++i)
	    list[i] = calls.get(i);
	return list;
    }
    public boolean load(File file, RuleFactory factory) throws IOException {
	try {
	    JSONTokener token = new JSONTokener(new FileReader(file));
	    JSONObject obj = new JSONObject(token);

	    String version = obj.getString("version");
	    JSONObject rulesObject = obj.getJSONObject("rules");

	    // Make the basic tree first
	    for (String tag : JSONObject.getNames(rulesObject)) {
		if (rules.get(tag) != null)
		    throw new IOException("Duplicate tag: "+tag);
		rules.put(tag, new Rule(tag));
	    }

	    if (obj.has("top")) {
		String top = obj.getString("top");
		this.top = rules.get(top);
		if (this.top == null) {
		    throw new IOException("Invalid top: "+top);
		}
	    }

	    // Next, instantiate the details
	    for (String tag : JSONObject.getNames(rulesObject)) {
		Rule rule = rules.get(tag);
		JSONObject o = rulesObject.getJSONObject(tag);
		String str = o.getString("condition");
		try {
		    rule.condition = ExpressionParser.parse(str, factory);
		} catch (Throwable t) {
		    throw new IOException("Cannot parse condition for "+tag+": "+str);
		}
		if (rule.condition == null) {
		    throw new IOException("Invalid condition for "+tag+": "+str);
		}
		if (o.has("description"))
		    rule.description = o.getString("description");

		if (o.has("accept")) {
		    String accept = o.getString("accept");
		    String[] actions = accept.split(",");
		    List<Expression> calls = new ArrayList<Expression>();
		    for (String s : actions) {
			if (s.indexOf("(") > 0) {
			    try {
				Expression e = ExpressionParser.parse(s, factory);
				if (e == null)
				    throw new IOException("Invalid method: "+s);
				calls.add(e);
			    } catch (Throwable t) {
				throw new IOException("Invalid method: "+s);
			    }
			} else {
			    rule.accept = rules.get(s);
			    if (rule.accept == null)
				throw new IOException("Invalid rule: "+s);
			}
		    }
		    rule.acceptList = getList(calls);
		}
		if (o.has("decline")) {
		    String decline = o.getString("decline");
		    String[] actions = decline.split(",");
		    List<Expression> calls = new ArrayList<Expression>();
		    for (String s : actions) {
			if (s.indexOf("(") > 0) {
			    try {
				Expression e = ExpressionParser.parse(s, factory);
				if (e == null)
				    throw new IOException("Invalid method: "+s);
				calls.add(e);
			    } catch (Throwable t) {
				throw new IOException("Invalid method: "+s);
			    }
			} else {
			    rule.decline = rules.get(s);
			    if (rule.decline == null)
				throw new IOException("Invalid rule: "+s);
			}
		    }
		    rule.declineList = getList(calls);
		}
	    }
	} catch (JSONException e) {
	    e.printStackTrace();
	    throw new IOException(e);
	}
	return true;
    }
    public void evaluate(RuleContext context) {
	if (top == null)
	    throw new RuntimeException("Rule has not explicit top");
	top.evaluate(context);
    }
    public Rule getRule(String tag) {
	return rules.get(tag);
    }
}
