package com.whicken.werecat.parser;

import com.whicken.werecat.*;
import com.whicken.werecat.expr.*;
import junit.framework.*;
import org.json.*;

public class ExpressionParserTest extends TestCase {
    public static class AssortedContext implements RuleContext {
	public String bar = "BAR";
    };
    public static class JSONContext implements RuleContext {
	public JSONObject json;
    };
    public void testStringExpressions() throws Exception
    {
	RuleFactory factory = new RuleFactory(AssortedContext.class);
	AssortedContext context = new AssortedContext();

	Expression e = ExpressionParser.parse("\"foo\"+bar", factory);
	assertEquals("fooBAR", e.getValue(context));

	Expression e2 = ExpressionParser.parse("\"foo\\\"bar\\\"\"", factory);
	assertEquals("foo\"bar\"", e2.getValue(context));
    }
    // Test constants are stored properly
    public void testConstants() throws Exception
    {
	RuleFactory factory = new RuleFactory(Object.class);

	Expression e = ExpressionParser.parse("1", factory);
	assertTrue(e instanceof SimpleConstant);
	assertTrue(e.getValue(null) instanceof Integer);

	// If the number's too long, auto convert to Long
	e = ExpressionParser.parse("1491325994222", factory);
	assertTrue(e instanceof SimpleConstant);
	assertTrue(e.getValue(null) instanceof Long);

	e = ExpressionParser.parse("1L", factory);
	assertTrue(e instanceof SimpleConstant);
	assertTrue(e.getValue(null) instanceof Long);

	e = ExpressionParser.parse("\"test\"", factory);
	assertTrue(e instanceof SimpleConstant);
	assertTrue(e.getValue(null) instanceof String);
	assertEquals("test", e.getValue(null));
	assertEquals("\"test\"", e.toString());

	e = ExpressionParser.parse("0.23", factory);
	assertTrue(e instanceof SimpleConstant);
	assertTrue(e.getValue(null) instanceof Double);

	e = ExpressionParser.parse("0.23f", factory);
	assertTrue(e instanceof SimpleConstant);
	assertTrue(e.getValue(null) instanceof Float);

	e = ExpressionParser.parse("0.23d", factory);
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
	// Make sure the FULL expression was parsed...
	assertEquals("getClass().canonicalName =~ /ject/ and java.lang.Math.random() < 0.1", e.toString());
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

	/*
	e = ExpressionParser.parse("1/2", factory);
	assertTrue(e instanceof DivExpression);
	assertEquals(0.5, e.getValue(null));
	*/
    }
    // Test syntactic sugar on JSON
    public void testJSON() throws Exception
    {
	RuleFactory factory = new RuleFactory(JSONContext.class);
	JSONContext context = new JSONContext();
	context.json = new JSONObject("{ \"test\": 1 }");

	Expression e = ExpressionParser.parse("json[\"test\"] = 1", factory);
	assertEquals(Boolean.TRUE, e.getValue(context));

	e = ExpressionParser.parse("json[\"foo\"]", factory);
	assertNull(e.getValue(context));

	e = ExpressionParser.parse("json.test = 1", factory);
	assertEquals(Boolean.TRUE, e.getValue(context));
    }
    public void testConversions() throws Exception
    {
	RuleFactory factory = new RuleFactory(AssortedContext.class);
	AssortedContext context = new AssortedContext();

	// Here we are converting strings to numbers on add, if that
	// seems reasonable. In a pure Werecat environment this might
	// be too much. Expression.isNumber controls this choice.
	Expression e = ExpressionParser.parse("\"1.2\"+\"1.2\"", factory);
	assertEquals(2.4, e.getValue(context));
	// assertEquals("1.21.2", e.getValue(context));
    }
}
