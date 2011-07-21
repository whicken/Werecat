package com.whicken.werecat.expr;

import com.whicken.werecat.RuleContext;
import java.util.regex.Pattern;

public class RegexpConstant extends Expression {
    Pattern pattern;
    public RegexpConstant(String s) {
	pattern = Pattern.compile(s.substring(1, s.length()-1));
    }
    public Object getValue(RuleContext context) {
	return pattern;
    }
    public String toString() {
	return "/"+pattern+"/";
    }
}
