package com.whicken.werecat.expr;

import junit.framework.*;

public class ExpressionTest extends TestCase {
    public void testIsStringNumber() {
	assertFalse(Expression.isStringNumber(null));
	assertFalse(Expression.isStringNumber(""));
	assertTrue(Expression.isStringNumber("1"));
	assertTrue(Expression.isStringNumber(".1"));
	assertTrue(Expression.isStringNumber("1."));
	assertFalse(Expression.isStringNumber(".1."));
	assertFalse(Expression.isStringNumber("A"));
    }
}
