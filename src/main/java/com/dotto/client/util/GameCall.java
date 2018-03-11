package com.dotto.client.util;

/**
 * Performs a specific dynamic game call.
 * 
 * @author lite20 (Ephraim Bilson)
 */
@FunctionalInterface
public interface GameCall {
    public void call(double delta);
}
