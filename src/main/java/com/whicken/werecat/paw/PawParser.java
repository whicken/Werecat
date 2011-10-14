package com.whicken.werecat.paw;

import com.whicken.werecat.WerecatException;
import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.parser.ExpressionParser;
import org.apache.log4j.Logger;

/**
 * The template just enforces that our PawExpression must be passed the
 * right type of object to the getValue method.
 */
public class PawParser<T> {
    private static final Logger log = Logger.getLogger(PawParser.class);

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
	    log.error(t);
	    throw new WerecatException("Unable to parse: "+expr, t);
	}
	return null;
    }
}
