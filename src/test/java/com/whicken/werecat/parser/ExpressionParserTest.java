package com.whicken.werecat.parser;

import com.whicken.werecat.*;
import com.whicken.werecat.expr.*;
import junit.framework.*;
import org.json.*;

public class ExpressionParserTest extends TestCase {
    public static class JSONContext implements RuleContext {
	public JSONObject json;
    };
    // Test constants are stored properly
    public void testConstants() throws Exception
    {
	RuleFactory factory = new RuleFactory(Object.class);

	Expression e = ExpressionParser.parse("1", factory);
	assertTrue(e instanceof SimpleConstant);
	assertTrue(e.getValue(null) instanceof Integer);

	e = ExpressionParser.parse("\"test\"", factory);
	assertTrue(e instanceof SimpleConstant);
	assertTrue(e.getValue(null) instanceof String);
	assertEquals("test", e.getValue(null));
	assertEquals("\"test\"", e.toString());

	e = ExpressionParser.parse("0.23", factory);
	assertTrue(e instanceof SimpleConstant);
	assertTrue(e.getValue(null) instanceof Double);

	e = ExpressionParser.parse("null", factory);
	assertTrue(e instanceof SimpleConstant);
	assertNull(e.getValue(null));
	assertEquals("null", e.toString());
    }

    public void testImport() throws Exception
    {
	RuleFactory factory = new RuleFactory(Object.class);
	factory.addImport(Math.class);
	Expression e = ExpressionParser.parse("class.canonicalName =~ /ject/ and Math.random() < 0.1", factory);
	assertNotNull(e);
    }

    // These are kind of mindless, but prevent silly screw-ups in the future
    public void testOperators() throws Exception
    {
	RuleFactory factory = new RuleFactory(Object.class);

	Expression e = ExpressionParser.parse("1 > 0", factory);
	assertTrue(e instanceof GTExpression);
	assertEquals(Boolean.TRUE, e.getValue(null));
	assertEquals("1 > 0", e.toString());
	e = ExpressionParser.parse("0 > 1", factory);
	assertEquals(Boolean.FALSE, e.getValue(null));

	e = ExpressionParser.parse("1 >= 1", factory);
	assertTrue(e instanceof GEExpression);
	assertEquals(Boolean.TRUE, e.getValue(null));
	assertEquals("1 >= 1", e.toString());
	e = ExpressionParser.parse("0 >= 1", factory);
	assertEquals(Boolean.FALSE, e.getValue(null));

	e = ExpressionParser.parse("0 < 1", factory);
	assertTrue(e instanceof LTExpression);
	assertEquals(Boolean.TRUE, e.getValue(null));
	assertEquals("0 < 1", e.toString());
	e = ExpressionParser.parse("0 < 0", factory);
	assertEquals(Boolean.FALSE, e.getValue(null));

	e = ExpressionParser.parse("0 <= 0", factory);
	assertTrue(e instanceof LEExpression);
	assertEquals(Boolean.TRUE, e.getValue(null));
	assertEquals("0 <= 0", e.toString());
	e = ExpressionParser.parse("1 <= 0", factory);
	assertEquals(Boolean.FALSE, e.getValue(null));
    }
    // Test syntactic sugar on JSON
    public void testJSON() throws Exception
    {
	RuleFactory factory = new RuleFactory(JSONContext.class);
	Expression e = ExpressionParser.parse("json[\"test\"] = 1", factory);
	JSONContext context = new JSONContext();
	context.json = new JSONObject("{ \"test\": 1 }");
	assertEquals(Boolean.TRUE, e.getValue(context));
	e = ExpressionParser.parse("json[\"foo\"]", factory);
	assertNull(e.getValue(context));
    }
}
