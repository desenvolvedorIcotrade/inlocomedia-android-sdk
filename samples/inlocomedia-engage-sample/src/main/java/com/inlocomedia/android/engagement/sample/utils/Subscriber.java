/*
 * Observer.java
 * Copyright (c) 2017 InLocoMedia.
 * All rights reserved.
 */

package com.inlocomedia.android.engagement.sample.utils;

public interface Subscriber<ObservedType> {
    void update(Publisher<ObservedType> object, ObservedType data);
}
