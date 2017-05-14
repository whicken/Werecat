package com.whicken.werecat.expr;

import junit.framework.*;
import org.joda.money.Money;

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

    public void testMoney() {
        Money money = Money.parse("USD .50");
        SimpleConstant fifty = new SimpleConstant(money);
	assertEquals("0.50", fifty.toString());
	assertEquals(.5, fifty.getDouble(null));
    }
}
