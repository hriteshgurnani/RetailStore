package com.tw.retailstore.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Basic Currency operations to Add/Subtract money and display Price in currency format.
 * Created by hritesh on 29/06/16.
 */

public class CurrencyUtils {
    public static String formatAsCurrency(double doubleValue) {
        boolean isWholeNumber = (doubleValue == Math.round(doubleValue));
        String pattern = isWholeNumber ? "₹ #,##,###.##" : "₹ #,##,###.00";
        DecimalFormat format = new DecimalFormat(pattern);
        return format.format(doubleValue);
    }

    public static String formatAsNumber(double doubleValue) {
        boolean isWholeNumber = (doubleValue == Math.round(doubleValue));
        String pattern = isWholeNumber ? "#,##,###.##" : "#,##,###.00";
        DecimalFormat format = new DecimalFormat(pattern);
        return format.format(doubleValue);
    }

    public static double buildPrice(double price, int quantity) {
        BigDecimal totalPrice = new BigDecimal(price).multiply(new BigDecimal(quantity));
        return roundToTwo(totalPrice).doubleValue();
    }

    private static BigDecimal roundToTwo(BigDecimal price) {
        return price.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal add(BigDecimal a, double b) {
        BigDecimal bigDecimal = a.add(new BigDecimal(b));
        return roundToTwo(bigDecimal);
    }

    public static BigDecimal subtract(BigDecimal a, double b) {
        BigDecimal bigDecimal = a.subtract(new BigDecimal(b));
        return roundToTwo(bigDecimal);
    }
}
