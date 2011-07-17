package com.whicken.werecat.paw;

import com.whicken.werecat.RuleContext;

class PawContext<T> implements RuleContext {
    T object;
    PawContext(T object) {
	this.object = object;
    }
}

