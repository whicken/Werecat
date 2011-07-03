package com.whicken.werecat.paw;

import com.whicken.werecat.*;
import com.whicken.werecat.expr.*;
import java.util.*;
import junit.framework.*;

public class PawParserTest extends TestCase {
    public static class Sample {
	public String str;
	Sample(String str) {
	    this.str = str;
	}
	public void foo() {
	}
	public int timesTwo(int a) {
	    return a * 2;
	}
    };
    public void testParser()
	throws Exception
    {
	List<Sample> samples = new ArrayList<Sample>();
	samples.add(new Sample("a"));
	samples.add(new Sample("1"));

	PawParser<Sample> parser = new PawParser(Sample.class);
	PawExpression<Sample> expr;

	// Test string/integer comparisons
	expr = parser.parse("str=1");
	assertEquals(Boolean.FALSE, expr.getValue(samples.get(0)));
	assertEquals(Boolean.TRUE, expr.getValue(samples.get(1)));

	// Test different kinds of methods
	expr = parser.parse("str");
	assertNotNull(expr);
	assertTrue(expr.expr instanceof PawField);
	expr = parser.parse("foo");
	assertNull(expr);
	expr = parser.parse("foo()");
	assertNotNull(expr);
	assertTrue(expr.expr instanceof PawMethod);
	expr = parser.parse("timesTwo(2)");
	assertNotNull(expr);
	assertTrue(expr.expr instanceof PawMethod);
	assertEquals(4.0, Expression.asDouble(expr.getValue(samples.get(0))));
    }
}