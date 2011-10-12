package com.whicken.werecat;

import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.parser.ExpressionParser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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
    static Action[] getList(List<Action> actions) {
	if (actions.size() == 0)
	    return null;
	Action[] list = new Action[actions.size()];
	for (int i = 0; i < list.length; ++i)
	    list[i] = actions.get(i);
	return list;
    }
    public boolean load(File file, RuleFactory factory)
	throws IOException, JSONException, WerecatException
    {
	FileReader reader = null;
	try {
	    reader = new FileReader(file);
	    return load(reader, factory);
	} finally {
	    if (reader != null)
		reader.close();
	}
    }
    public boolean load(Reader reader, RuleFactory factory)
	throws IOException, JSONException, WerecatException
    {
	JSONTokener token = new JSONTokener(reader);
	JSONObject obj = new JSONObject(token);
	return load(obj, factory);
    }
	
    /**
     * Use this initialization method if your Werecat rules are embedded 
     * in another JSON file.
     */
    public boolean load(JSONObject obj, RuleFactory factory)
	throws JSONException, WerecatException
    {
	String version = obj.getString("version");
	JSONObject rulesObject = obj.getJSONObject("rules");
	
	// Make the basic tree first
	for (String tag : JSONObject.getNames(rulesObject)) {
	    if (rules.get(tag) != null)
		throw new WerecatException("Duplicate tag: "+tag);
	    rules.put(tag, new Rule(tag));
	}
	
	if (obj.has("top")) {
	    String top = obj.getString("top");
	    this.top = rules.get(top);
	    if (this.top == null) {
		throw new WerecatException("Invalid top: "+top);
	    }
	}
	
	if (obj.has("imports")) {
	    JSONArray imports = obj.getJSONArray("imports");
	    for (int i = 0; i < imports.length(); ++i) {
		String s = imports.getString(i);
		factory.addImport(s);
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
		throw new WerecatException("Cannot parse condition for "+tag+": "+str, t);
	    }
	    if (rule.condition == null) {
		throw new WerecatException("Invalid condition for "+tag+": "+str);
	    }
	    if (o.has("description"))
		rule.description = o.getString("description");
	    
	    if (o.has("accept")) {
		String accept = o.getString("accept");
		String[] actiontext = accept.split(";");
		List<Action> actions = new ArrayList<Action>();
		for (String s : actiontext) {
		    if (s.indexOf("(") > 0) {
			try {
			    Expression e = ExpressionParser.parse(s, factory);
			    if (e == null)
				throw new WerecatException("Invalid method: "+s);
			    actions.add(new ExpressionAction(e));
			} catch (Throwable t) {
			    throw new WerecatException("Invalid method: "+s, t);
			}
		    } else {
			Rule r = rules.get(s);
			if (r == null)
			    throw new WerecatException("Invalid rule: "+s);
			actions.add(new RuleAction(r));
		    }
		}
		rule.accept = getList(actions);
	    }
	    if (o.has("decline")) {
		String decline = o.getString("decline");
		String[] actiontext = decline.split(";");
		List<Action> actions = new ArrayList<Action>();
		for (String s : actiontext) {
		    if (s.indexOf("(") > 0) {
			try {
			    Expression e = ExpressionParser.parse(s, factory);
			    if (e == null)
				throw new WerecatException("Invalid method: "+s);
			    actions.add(new ExpressionAction(e));
			} catch (Throwable t) {
			    throw new WerecatException("Invalid method: "+s, t);
			}
		    } else {
			Rule r  = rules.get(s);
			if (r == null)
			    throw new WerecatException("Invalid rule: "+s);
			actions.add(new RuleAction(r));
		    }
		}
		rule.decline = getList(actions);
	    }
	}
	return true;
    }
    public void evaluate(RuleContext context)
	throws WerecatException
    {
	if (top == null)
	    throw new WerecatException("Rule has no explicit top");
	top.evaluate(context);
    }
    public Rule getRule(String tag) {
	return rules.get(tag);
    }
}
