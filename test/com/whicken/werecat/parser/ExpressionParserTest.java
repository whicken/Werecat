package com.whicken.werecat.parser;

import com.whicken.werecat.*;
import com.whicken.werecat.expr.*;
import com.whicken.werecat.paw.*;
import java.util.*;
import junit.framework.*;

public class ExpressionParserTest extends TestCase {
    public static class Sample {
	public String str;
	Sample(String str) {
	    this.str = str;
	}
    };
    public void testParser()
	throws Exception
    {
	List<Sample> samples = new ArrayList<Sample>();
	samples.add(new Sample("a"));
	samples.add(new Sample("1"));
	RuleFactory factory = new RuleFactory(Object.class);

	Expression e = ExpressionParser.parse("1", factory);
	assertTrue(e instanceof SimpleConstant);
	assertTrue(e.getValue(null) instanceof Integer);

	e = ExpressionParser.parse("\"test\"", factory);
	assertTrue(e instanceof SimpleConstant);
	assertTrue(e.getValue(null) instanceof String);
	assertEquals("test", e.getValue(null));

	PawParser<Sample> parser = new PawParser(Sample.class);
	PawExpression<Sample> expr = parser.parse("str=1");
	assertEquals(Boolean.FALSE, expr.getValue(samples.get(0)));
	assertEquals(Boolean.TRUE, expr.getValue(samples.get(1)));
    }
}
