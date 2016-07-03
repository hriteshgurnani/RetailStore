package com.tw.retailstore.utils;

/**
 * Preconditions Utility.
 * Created by hritesh on 29/06/16.
 */

public class Preconditions {

    public static void checkNotNull(Object object, String errorMessage) {
        if (object == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
