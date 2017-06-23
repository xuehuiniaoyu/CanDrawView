package com.huan.icanvas.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Administrator on 2016/7/6.
 */
public class ErrorUtil {
    public static String e(String tag, Throwable e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String splitString = "========================= "+tag+" =========================\n";
            return splitString + sw.toString() + splitString;
        } catch (Throwable e1) {
            return "bad Exception";
        }
    }
}
