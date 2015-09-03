package com.whicken.werecat.paw;

import com.whicken.werecat.RuleContext;

public class PawContext<T> implements RuleContext {
    T object;
    PawContext(T object) {
	this.object = object;
    }
    // Allows for more shenanigans by users
    public T getObject() {
	return object;
    }
}
