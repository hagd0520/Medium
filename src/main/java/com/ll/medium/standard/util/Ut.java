package com.ll.medium.standard.util;

import org.apache.catalina.util.URLEncoder;

import java.nio.charset.StandardCharsets;

public class Ut {
    public static class url {
        public static String encode(String url) {
            return new URLEncoder().encode(url, StandardCharsets.UTF_8);
        }
    }
}
