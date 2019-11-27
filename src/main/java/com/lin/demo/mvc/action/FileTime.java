package com.lin.demo.mvc.action;

import java.util.Date;

public class FileTime {

    /**
     * Unix 时间 1970-01-01 00:00:00 与 Win32 FileTime 时间 1601-01-01 00:00:00
     * 毫秒数差
     */
    public final static long UNIX_FILETIME_MILLISECOND_DIFF = 11644473600000L;

    /**
     * Win32 FileTime 采用 100ns 为单位的，定义 100 ns 与 1 ms 的倍率
     */
    public final static int MILLISECOND_100NANOSECOND_MULTIPLE = 10000;

    /**
     * FileTime 的低 32 位数
     */
    private final long low;

    /**
     * FileTime 的高 32 位数
     */
    private final long high;

    public FileTime(long high, long low) {
        this.high = high;
        this.low = low;
    }

    /**
     * 获得 FileTime 以 64 位数字表示的数据
     * @return FileTime 的数据值
     */
    public long getFileTime() {
        return ((high << 32) & 0xffffffff00000000L) | (low & 0xffffffffL);
    }

    public long getLow() {
        return low;
    }

    public long getHigh() {
        return high;
    }

    /**
     * 将 Java 中的 Date 类型转为 Win32 的 FileTime 结构
     * @param date
     * @return
     */
    public static FileTime date2FileTime(Date date) {
        long time = (UNIX_FILETIME_MILLISECOND_DIFF + date.getTime()) *
                MILLISECOND_100NANOSECOND_MULTIPLE;
        long high = (time >> 32) & 0xffffffffL;
        long low = time & 0xffffffffL;
        FileTime fileTime = new FileTime( high, low );
        return fileTime;
    }

    public Date toDate() {
        return new Date(getFileTime() / MILLISECOND_100NANOSECOND_MULTIPLE -
                UNIX_FILETIME_MILLISECOND_DIFF);
    }

    @Override
    public String toString() {
        return "high: " + high + ", low: " + low;
    }
}