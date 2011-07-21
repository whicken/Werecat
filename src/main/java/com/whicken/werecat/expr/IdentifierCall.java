package com.whicken.werecat.expr;

import java.util.List;

/**
 * This is a helper class during parsing, nothing more.
 */
public class IdentifierCall {
    public String name;
    public List<Expression> args;
    public IdentifierCall(String name, List<Expression> args) {
	this.name = name;
	this.args = args;
    }
}
