package cn.samples.depot.common.utils;

import cn.samples.depot.common.constant.Constants;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Description: 获取数据库唯一id工具类
 *
 * @className: UniqueIdUtil
 * @Author: zhangpeng
 * @Date 2019/7/16 19:27
 * @Version 1.0
 **/
public class UniqueIdUtil {

    /**
     * 获取得到guid
     *
     * @return
     */
    public static final String getUUID() {
        UUID localUUID = UUID.randomUUID();
        return localUUID.toString().replace("-", "");
    }

    /**
     * 获取得到报文编号
     *
     * @param customsCode
     * @param orgCode
     * @return
     */
    public static final String getMsgId(String customsCode, String orgCode) {
        // 报文编号的生成逻辑：4位海关代码+9位组织结构代码+“_”+年月日时分秒毫秒（17位）
        String msgId = customsCode + orgCode + Constants.UNDER_LINE + DateUtils.getFullTimeStamp();
        return msgId;
    }

    /**
     * 按照规则生成发运计划表编号
     *
     * @param orgCode   组织机构代码号
     * @param randomLen 生成的随机数长度
     * @return
     */
    public static String getShipmentPlanNum(String orgCode, int randomLen) {
        LocalDate date2 = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        // 20190424
        String dateStr = date2.format(DateTimeFormatter.BASIC_ISO_DATE);

        return orgCode + dateStr + getCard(randomLen);
    }

//    public static void main(String[] args) {
//        System.out.println(getMsgId("2301", "999999999"));
//    }

    /**
     * 生成随机数
     */
    public static String getCard(int randomLen) {
        // 生成随机数
        Random rand = new Random();
        String cardNnumer = "";
        for (int a = 0; a < randomLen; a++) {
            cardNnumer += rand.nextInt(10);
        }
        return cardNnumer;
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    private static int serial = 0;
    private static String date = "";

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 6位编号生成日期【顺序位年（2位）+月+日】+6位流水号
     **/

    public static synchronized String get6Date6SerialNumber() {
        String dateStr = sdf.format(new Date());
        if (!date.equals(dateStr)) {
            serial = 0;
            date = dateStr;
        }
        return String.format("%s%06d", dateStr, ++serial);

    }

    public static void main(String[] args) {
        System.out.println(get6Date6SerialNumber());
        System.out.println(get6Date6SerialNumber());
        System.out.println(get6Date6SerialNumber());
        System.out.println(get6Date6SerialNumber());
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread() + "_" + get6Date6SerialNumber());
                }
            }).start();
        }
    }
}
