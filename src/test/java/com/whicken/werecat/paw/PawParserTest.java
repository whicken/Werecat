package com.whicken.werecat.paw;

import com.whicken.werecat.*;
import com.whicken.werecat.expr.*;
import java.util.*;
import junit.framework.*;

public class PawParserTest extends TestCase {
    public static class Sample {
	public Day day = Day.Wednesday;
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

	try {
	    expr = parser.parse("foo and");
	    assertFalse(true);
	} catch (WerecatException e) {
	    // This is the expected path
	}
	expr = parser.parse("foo()");
	assertNotNull(expr);
	assertTrue(expr.expr instanceof PawMethod);
	expr = parser.parse("timesTwo(2)");
	assertNotNull(expr);
	assertTrue(expr.expr instanceof PawMethod);
	assertEquals(4.0, Expression.asDouble(expr.getValue(samples.get(0))));

	// Test negative integer comparisons
	Sample minus1 = new Sample("-1");
	expr = parser.parse("str=-1");
	assertEquals(Boolean.TRUE, expr.getValue(minus1));

	// Test case insentivity
	parser.parse("str=1 and str=1");
	parser.parse("str=1 AND str=1");
    }
    public void testClassReference()
	throws Exception
    {
	PawParser<Sample> parser = new PawParser(Sample.class);
	PawExpression<Sample> expr;

	expr = parser.parse("PawClass.hello");
	assertEquals("Hello", expr.getValue(null));

	expr = parser.parse("PawClass.isOdd(1)");
	assertEquals(Boolean.TRUE, expr.getValue(null));
	
	expr = parser.parse("PawClass.isOdd(2)");
	assertEquals(Boolean.FALSE, expr.getValue(null));
    }
    public void testEnums()
	throws Exception
    {
	PawParser<Sample> parser = new PawParser(Sample.class);
	PawExpression<Sample> expr;
	Sample sample = new Sample("a");

	expr = parser.parse("day");
	assertEquals(Day.Wednesday, expr.getValue(sample));

	expr = parser.parse("day = Day.Monday");
	assertEquals(Boolean.FALSE, expr.getValue(sample));

	expr = parser.parse("day = Day.Wednesday");
	assertEquals(Boolean.TRUE, expr.getValue(sample));

	expr = parser.parse("str =~ /a/");
	assertEquals(0, expr.getValue(sample));
    }
}
