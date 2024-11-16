package com.ganmashop.utils;

import java.util.UUID;

/**
 * @author Desmond
 * @created 06/02/2024 - 8:09 PM
 */
public class GenUUID {

    private GenUUID() {}

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
