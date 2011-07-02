package com.whicken.werecat.parser;

import com.whicken.werecat.*;
import com.whicken.werecat.expr.*;
import junit.framework.*;

public class ExpressionParserTest extends TestCase {
    public void testParser()
	throws Exception
    {
	RuleFactory factory = new RuleFactory(Object.class);

	// Test constants are stored properly
	Expression e = ExpressionParser.parse("1", factory);
	assertTrue(e instanceof SimpleConstant);
	assertTrue(e.getValue(null) instanceof Integer);

	e = ExpressionParser.parse("\"test\"", factory);
	assertTrue(e instanceof SimpleConstant);
	assertTrue(e.getValue(null) instanceof String);
	assertEquals("test", e.getValue(null));
    }
}
