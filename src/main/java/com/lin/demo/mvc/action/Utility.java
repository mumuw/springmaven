package com.lin.demo.mvc.action;

import java.util.Date;

public class Utility {
    /**
     * Unix 时间 1970-01-01 00:00:00 与 Win32 FileTime 时间 1601-01-01 00:00:00
     * 毫秒数差
     */
    public final static long UNIX_FILETIME_MILLISECOND_DIFF = 11644473600000L;

    /**
     * Win32 FileTime 采用 100ns 为单位的，定义 100 ns 与 1 ms 的倍率
     */
    public final static int MILLISECOND_100NANOSECOND_MULTIPLE = 10000;

}
