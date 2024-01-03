package com.ll.medium.standard.util;

import org.apache.catalina.util.URLEncoder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class Ut {
    public static class exception {
        public static String toString(Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();

            StringBuilder details = new StringBuilder();

            details.append("Exception Message: ").append(ex.getMessage()).append("\n");

            Throwable cause = ex.getCause();
            if (cause != null) {
                details.append("Cause by: ").append(cause.toString()).append("\n");
            }

            details.append("Stack Trace:\n").append(stackTrace);

            return details.toString();
        }
    }

    public static class url {
        public static String encode(String url) {
            return new URLEncoder().encode(url, StandardCharsets.UTF_8);
        }
    }
}
