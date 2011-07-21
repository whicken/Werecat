package com.whicken.werecat;

import com.whicken.werecat.expr.Expression;
import com.whicken.werecat.expr.SimpleConstant;
import com.whicken.werecat.expr.BlindField;
import com.whicken.werecat.expr.BlindMethod;
import java.lang.reflect.*;
import java.util.List;

/**
 * RuleFactory with NO concrete implementation, useful for editing outside
 * an actual context, but you'll get no validation.
 */
public class BlindFactory extends RuleFactory {
    public BlindFactory() {
	super(Object.class);
    }
    protected Method getMethod(String method, List<Expression> args) {
	throw new RuntimeException("BlindFactory does not support getMethod");
    }
    public Expression createField(String key) {
	return new BlindField(key);
    }
    public Expression createMethod(String method, List<Expression> args) {
	return new BlindMethod(method, args);
    }
}
