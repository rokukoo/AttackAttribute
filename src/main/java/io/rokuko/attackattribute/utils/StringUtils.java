package io.rokuko.attackattribute.utils;

public class StringUtils {

    public static String clearColor(String s) {
        return s.replaceAll("§[\\da-z]", "");
    }

    public static Double extractDouble(String s){
        return Double.valueOf(clearColor(s).replaceAll("[^\\d.]", ""));
    }

    public static String formatDouble(Double d){
        return String.format("%.2f", d);
    }

}
