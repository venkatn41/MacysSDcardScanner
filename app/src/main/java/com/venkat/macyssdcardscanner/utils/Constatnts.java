package com.venkat.macyssdcardscanner.utils;

/**
 * Created by venkat on 4/27/18.
 */

public class Constatnts {
    public interface ACTION {
        public static String STARTFOREGROUND_ACTION = "foregroundservice.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "foregroundservice.action.stopforeground";
    }

    public interface NOTIFICATION {
        public static int SERVICE_ID = 101;
    }

    public interface FRAGMENT {
        public static String EXTENSION_MAP = "extension_map";
        public static String SIZE_MAP = "size_map";
        public static String AVG_SIZE = "avg_size";
    }
}
