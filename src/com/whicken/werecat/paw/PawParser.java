package com.whicken.werecat.paw;

import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.parser.ExpressionParser;

/**
 * The template just enforces that our PawExpression must be passed the
 * right type of object to the getValue method.
 */
public class PawParser<T> {
    PawFactory factory;
    public PawParser(Class c) {
	factory = new PawFactory(c);
    }
    public PawExpression<T> parse(String expr) {
	try {
	    Expression e = ExpressionParser.parse(expr, factory);
	    if (e != null)
		return new PawExpression(e);
	} catch (Throwable t) {
	    // TODO: Propagate this error cleanly
	}
	return null;
    }
}
