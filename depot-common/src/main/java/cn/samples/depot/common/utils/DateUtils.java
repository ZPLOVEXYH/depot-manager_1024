package cn.samples.depot.common.utils;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Description: 日期工具类
 *
 * @className: DateUtils
 * @Author: zhangpeng
 * @Date 2019/7/16 14:43
 * @Version 1.0
 **/
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    static public final String DATE_FORMAT_DAY = "yyyy-MM-dd";
    static public final String DATE_FORMAT_MONTH = "yyyy-MM";
    static public final String DATE_FORMAT_DAY_2 = "yyyy/MM/dd";
    static public final String TIME_FORMAT_SEC = "HH:mm:ss";
    static public final String DATE_FORMAT_SEC = "yyyy-MM-dd HH:mm:ss";
    static public final String DATE_FORMAT_MSEC = "yyyy-MM-dd HH:mm:ss.SSS";
    static public final String DATE_FORMAT_MSEC_T = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    static public final String DATE_FORMAT_MSEC_T_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    static public final String DATE_FORMAT_DAY_SIMPLE = "y/M/d";

    /**
     * 匹配yyyy-MM-dd
     */
    private static final String DATE_REG = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
    /**
     * 匹配yyyy/MM/dd
     */
    private static final String DATE_REG_2 = "^[1-9]\\d{3}/(0[1-9]|1[0-2])/(0[1-9]|[1-2][0-9]|3[0-1])$";
    /**
     * 匹配y/M/d
     */
    private static final String DATE_REG_SIMPLE_2 = "^[1-9]\\d{3}/([1-9]|1[0-2])/([1-9]|[1-2][0-9]|3[0-1])$";
    /**
     * 匹配HH:mm:ss
     */
    private static final String TIME_SEC_REG = "^(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
    /**
     * 匹配yyyy-MM-dd HH:mm:ss
     */
    private static final String DATE_TIME_REG = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s" +
            "(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
    /**
     * 匹配yyyy-MM-dd HH:mm:ss.SSS
     */
    private static final String DATE_TIME_MSEC_REG = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s" +
            "(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d\\.\\d{3}$";
    /**
     * 匹配yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    private static final String DATE_TIME_MSEC_T_REG = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T" +
            "(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d\\.\\d{3}$";
    /**
     * 匹配yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     */
    private static final String DATE_TIME_MSEC_T_Z_REG = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T" +
            "(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d\\.\\d{3}Z$";

    public static final long SECOND_PER_MINUTE = 60;
    public static final long SECOND_PER_HOUR = 60 * SECOND_PER_MINUTE;
    public static final long SECOND_PER_DAY = 24 * SECOND_PER_HOUR;
    public static final long MILLIS_PER_DAY = SECOND_PER_DAY * 1000;

    public static final FastDateFormat DATE_FORMAT_LONG = FastDateFormat.getInstance("yyyyMMdd");
    public static final FastDateFormat DATE_FORMAT_SHORT = FastDateFormat.getInstance("yyMMdd");
    public static final FastDateFormat DATE_FORMAT_LONG_DASH = FastDateFormat.getInstance("yyyy-MM-dd");
    public static final FastDateFormat TIMESTAMP_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("Asia/Shanghai"));
    // 14位精确到秒的日期格式
    private static String DATE_14 = "yyyyMMddHHmmss";
    public static SimpleDateFormat DATE_FORMAT_14 = new SimpleDateFormat(DATE_14);
    // 19位精确到秒的日期格式2，如:2015-01-01 00:00:00
    public static String DATE_19 = "yyyy-MM-dd HH:mm:ss";
    public static SimpleDateFormat DATE_FORMAT_19 = new SimpleDateFormat(DATE_19);

    public static long parseTimestamp(String timestamp) {
        try {
            return TIMESTAMP_FORMAT.parse(timestamp).getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatTimestamp(long millis) {
        return TIMESTAMP_FORMAT.format(millis);
    }

    public static long parseDateLong(String dateFormatLong) {
        try {
            return DATE_FORMAT_LONG.parse(dateFormatLong).getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Calendar currentCalendar() {
        return Calendar.getInstance();
    }

    public static int getCurrentYear() {
        return toCalendar(new Date()).get(Calendar.YEAR);
    }

    public static long[] getDays(long centerDay, int dayOffset) {
        long[] days = new long[dayOffset * 2 + 1];
        for (int i = -dayOffset; i <= dayOffset; i++) {
            days[i + dayOffset] = centerDay + i * MILLIS_PER_DAY;
        }
        return days;
    }

    public static long tomorrow() {
        return today() + MILLIS_PER_DAY;
    }

    public static long today() {
        return truncate(new Date(), Calendar.DATE).getTime();
    }

    public static long todayOfLastMonth() {
        Calendar calendar = currentCalendar();
        calendar.add(Calendar.MONTH, -1);
        return truncate(calendar.getTime(), Calendar.DATE).getTime();
    }

    public static int todayOfMonth() {
        return currentCalendar().get(Calendar.DAY_OF_MONTH);
    }

    public static long yesterday() {
        return today() - MILLIS_PER_DAY;
    }

    public static long truncate(long time, int field) {
        return truncate(new Date(time), field).getTime();
    }

    public static long now() {
        return System.currentTimeMillis();
    }

    public static Calendar getCalendar(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.setTime(new Date(date));
        return calendar;
    }

    public static long getDate(long date) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    public static String getYearMonth(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_MONTH);
        return sdf.format(date);
    }

    public static long getTime(long date) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.YEAR, 0);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获得指定日期所在当月第一天
     */
    public static long getFirstDateOfMonth(long date) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获得指定日期所在当月最后一天
     */
    public static long getLastDateOfMonth(long date) {
        Calendar calendar = getCalendar(date);

        //将小时至0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        calendar.set(Calendar.MINUTE, 0);
        //将秒至0
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

//        calendar.set(Calendar.DATE, 0);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
        return calendar.getTime().getTime();

//        Calendar calendar = getCalendar(date);
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//        calendar.set(Calendar.HOUR_OF_DAY, 23);
//        calendar.set(Calendar.MINUTE, 59);
//        calendar.set(Calendar.SECOND, 59);
//        calendar.set(Calendar.MILLISECOND, 0);
//        calendar.setTimeZone(TimeZone.getTimeZone("Europe/London"));
//        return calendar.getTime().getTime();
    }

    public static String getCurrentDate8() {
        return DATE_FORMAT_LONG.format(new Date());
    }

    public static String format14to19(String dateString) {
        return formatDate(DATE_FORMAT_14, DATE_FORMAT_19, dateString);
    }

    public static String formatDate(SimpleDateFormat fromFormatter, SimpleDateFormat toFormatter, String dateString) {
        if (null == fromFormatter || null == toFormatter || null == dateString) {
            return "";
        }
        Date date;
        try {
            date = fromFormatter.parse(dateString);
            return toFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String longToString(long currentTime) {
        try {
            return longToString(currentTime, DATE_FORMAT_SEC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate2(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }


    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate2(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }


    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }


    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

//    public static void main(String[] args) throws ParseException {
////        System.out.println("formatTimestamp:" + formatTimestamp(1535731200000L));
////
////        System.out.println("now(:" + now());
//
//        Long startTime = DateUtils.getStartLongTime();
////            // （时间待定）数据扫描结束时间
//        Long endTime = DateUtils.getEndLongTime();
//
//        System.out.println(startTime);
//        System.out.println(endTime);
//
//        System.out.println("test===" + Long.parseLong(DateUtils.getFullTimeStamp()));
//
//        //1533553500000
//        //1533524700000
////        System.out.println("parseTimestamp:"+parseTimestamp("2018-08-06 11:05:00"));
////        System.out.println("parseTimestamp:"+formatTimestamp(parseTimestamp("2018-08-06 11:05:00")));
////        System.out.println("parseTimestamp:"+parseTimestamp(formatTimestamp(1528214034000L)));
//
////        System.out.println("parseTimestamp:" + getLastDateOfMonth(1535731200000L));
//////        System.out.println("parseTimestamp:"+formatTimestamp(getLastDateOfMonth(1535731200000L)));
//////
////        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//////        long time = 1528185234000L;
////        long timeStart = sdf.parse("2018-08-06 11:05:00").getTime();
////        System.out.println(timeStart);
////        Date date = new Date(1533744000000L);
////        System.out.println(sdf.format(date));
////        System.out.println(parseDateLong("1526628693000"));
////        System.out.println(sdf.format(new Date(time)));
////        System.out.println(sdf.format(new Date(parseDateLong("1526628693000"))));
////
////        System.out.println(parseMonthLong("1526628693000"));
////        System.out.println(sdf.format(new Date(1526628693000L)));
////        System.out.println(sdf.format(new Date(parseMonthLong("1526628693000"))));
//
////        System.out.println(getFirstDateOfMonth(time));
////        System.out.println(sdf.format(new Date(getFirstDateOfMonth(time))));
////        System.out.println(getLastDateOfMonth(time));
////        System.out.println(sdf.format(new Date(getLastDateOfMonth(time))));
////
////        System.out.println(now());
//
////        String date = "2018-09";
////        SimpleDateFormat format2db = new SimpleDateFormat("yyyy-MM-dd");
////        Date date2db = format2db.parse(date);
////        System.out.println(date2db);
//
////        Calendar ca = Calendar.getInstance();
////ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
////String last = sdf.format(ca.getTime());
////System.out.println("===============last:"+last);
//
//    }

    /**
     * 得到完整的时间戳，格式:yyyy-MM-dd HH:mm:ss(年月日时分秒毫秒)
     *
     * @return 完整的时间戳
     */
    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 得到完整的时间戳，格式:yyyyMMddHHmmssSSS(年月日时分秒毫秒)
     *
     * @return 完整的时间戳
     */
    public static String getFullTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(getMonthStartTime(-5));
//        System.out.println(getYearMonth(1567496996921L));
////        System.out.println(Long.parseLong(getFullTimeStamp()));
//        System.out.println(longToString(1567496996921L, "yyyy-MM-dd hh:mm:ss"));
    }

    @Deprecated
    static
    public Date str2Date(String strDate) throws RuntimeException,
            ParseException {

        strDate = strDate.trim();
        SimpleDateFormat sdf = null;
        if (RegularUtils.isMatched(strDate, DATE_REG)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_DAY);
        }
        if (RegularUtils.isMatched(strDate, DATE_REG_2)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_DAY_2);
        }
        if (RegularUtils.isMatched(strDate, DATE_REG_SIMPLE_2)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_DAY_SIMPLE);
        }
        if (RegularUtils.isMatched(strDate, TIME_SEC_REG)) {
            sdf = new SimpleDateFormat(TIME_FORMAT_SEC);
        }
        if (RegularUtils.isMatched(strDate, DATE_TIME_REG)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_SEC);
        }
        if (RegularUtils.isMatched(strDate, DATE_TIME_MSEC_REG)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_MSEC);
        }
        if (RegularUtils.isMatched(strDate, DATE_TIME_MSEC_T_REG)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_MSEC_T);
        }
        if (RegularUtils.isMatched(strDate, DATE_TIME_MSEC_T_Z_REG)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_MSEC_T_Z);
        }
        if (null != sdf) {
            return sdf.parse(strDate);
        }
        throw new RuntimeException(
                String.format("[%s] can not matching right time format", strDate));
    }

    /**
     * 获取时分秒
     *
     * @return HH:mm:ss 格式的时分秒
     */
    public static String getTimeShort() {
        String dateString = TIMESTAMP_FORMAT.format(System.currentTimeMillis());
        return dateString;
    }

    /**
     * 获取服务器long的开始时间
     *
     * @return HH:mm:ss 格式的时分秒
     */
    public static long getStartLongTime() {
//        return System.currentTimeMillis() + (60 * 60 * 8 * 1000);
        return System.currentTimeMillis() - (60 * 60 * 12 * 1000);
    }

    /**
     * 获取当月开始时间戳
     *
     * @param month 月份
     * @return
     */
    public static Long getMonthStartTime(int month) {
        Long currentTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        calendar.setTimeInMillis(currentTime);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取服务器long的结束时间
     *
     * @return HH:mm:ss 格式的时分秒
     */
    public static long getEndLongTime() {
//        return System.currentTimeMillis() + (60 * 60 * 8 * 1000) + (60 * 60 * 12 * 1000);
        return System.currentTimeMillis();
    }

    /**
     * Long 类型转化为 String 类型
     *
     * @param time
     * @return
     */
    public static Date longToDate(Long time) {
        return new Date(time);
    }

    public static long getToLong(String DateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = null;
        try {
            time = sdf.parse(DateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time.getTime();
    }
}
