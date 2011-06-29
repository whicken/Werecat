package com.whicken.werecat.paw;

import com.whicken.werecat.RuleContext;

class PawContext<T> extends RuleContext {
    T object;
    PawContext(T object) {
	this.object = object;
    }
}

