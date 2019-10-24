package com.sample;

import cn.samples.depot.web.bean.report.BRailwayTallyBillInfoSave;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class WLJK_IRTA {

//    public static void main(String[] args) {
//        Sound sound = new Sound();
//        sound.setText("text");
//        sound.setName("name");
//        sound.setId("111");
//
//        Person p3 = new Person();
//        p3.setName("rest");
//        p3.setSound(sound);
//        p3.setPhones(Arrays.asList("111", "222", "333"));
//
//
//        PersonList personList = new PersonList();
//        personList.add(p3);
//
////        Sound sound = new Sound();
////        sound.setId("111");
////        sound.setName("zhangsan");
////        sound.setText("text");
//
//        SealID sealID = new SealID();
//        sealID.setAgencyCode("AB");
//        sealID.setText("M/11");
//
//        RailWayBillInfos railWayBilInfos = new RailWayBillInfos();
//        railWayBilInfos.setBillNo("AGB14524");
//        railWayBilInfos.setCarriageNo("5243547");
//        railWayBilInfos.setPackNo("789");
//        railWayBilInfos.setGrossWt("20406.00");
//        railWayBilInfos.setContaNo("TCKU3125044");
//        railWayBilInfos.setContaType("L2G0");
//        railWayBilInfos.setSealIDs(Arrays.asList(sealID, sealID));
//        railWayBilInfos.setNotes("铁路集装箱");
//
//        MessageList messageList = new MessageList();
//        messageList.setBillList(Arrays.asList(railWayBilInfos, railWayBilInfos));
//        messageList.setDeclPort("2301");
//        messageList.setUnloadingPlace("CNLYG230132/2301");
//        messageList.setLoadingPlace("CNLYG230132/2301");
//        messageList.setActualDateTime("2017-07-21 15:30:45");
//        messageList.setCompletedDateTime("2017-07-21 20:30:45");
//
//        MessageHead messageHead = new MessageHead();
//        messageHead.setMessageId("2301663843789_20170729153120603");
//        messageHead.setFunctionCode("2");
//        messageHead.setMessageType("WLJK_IRTA");
//        messageHead.setAuditTime("20170729153120603");
//        messageHead.setSender("2301663843789");
//        messageHead.setReceiver("2301");
//        messageHead.setVersion("1.0");
//
//        Message message = new Message();
//        message.setMessageHead(Arrays.asList(messageHead));
//        message.setMessageList(Arrays.asList(messageList));
//
////        Persister persister = new Persister();
//
//        String s = XstreamUtil.xmlAppendHead(XmlUtil.serializeToStr(message, "utf-8"));
//        System.out.println(s);
//
////        String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + message.toString();
//        boolean createFile = FileUtil.writeContent("d://", "p1.xml", s);
//        System.out.println(createFile);
////        // 序列化xml保存到指定的地址file
////        XmlUtil.serializeFile(message, "d://p.xml");
////
////        File file = new File("d://p.xml");
////        XmlUtil.serializeFile(message, "d://p.xml");
//
////        String s = XmlUtil.serializeToStr(message, "utf-8");
////        System.out.println(s);
//
//
//
//
//
//        // 创建一个传送器对象，使用这个对象可以很快的创建和解析XML文档
////        Persister persister = new Persister();
//////
////        try {
//////            // 将Person列表直接写出
////            persister.write(message, new File("d://p.xml"));
//////
////            String xml = "<person>\n" +
////                    "      <name>rest</name>\n" +
////                    "      <sound id=\"111\" name=\"name\">text</sound>\n" +
////                    "      <phone>111</phone>\n" +
////                    "      <phone>222</phone>\n" +
////                    "      <phone>333</phone>\n" +
////                    "   </person>";
////            Person personList1 = XmlUtil.deserialize(xml, Person.class);
////            System.out.println(personList1.toString());
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//    }

    public static void main(String[] args) {
//        File[] files = cn.hutool.core.io.FileUtil.ls("C:\\Users\\zp&xyh\\Desktop\\");
//        Arrays.stream(files).forEach(file -> {
//            String fileName = file.getName();
//            System.out.println("文件夹下的文件名为：" + fileName);
//            if(fileName.equals("放行指令.xml")){
//                String xmlStr = FileUtil.readUtf8String(file);
//                System.out.println(xmlStr);
//                RelMessage relMsg = XmlUtil.deserialize(RelMessage.class, file, false);
//                System.out.println(relMsg);
//            }
//        });

//        System.out.println(DateUtil.parse("2019-08-1210:22:01").getTime());

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("xx");
//                try {
//                    Thread.sleep(20000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.exit(0);
//            }
//        }, "test-thread");
//
//        try {
//            Thread.sleep(50000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        String path = "d://test/used/";
//        File oldFile = new File("d://p1.xml");
//        System.out.println(oldFile.getName());
//        FileUtil.move(oldFile, new File(path + oldFile.getName()), true);
//        System.out.println(file.getName());

//        String y = "M/1213/AA";
//        String agencyCode = y.substring(0, y.indexOf("/"));
//        String text = y.substring(y.indexOf("/") + 1, y.length());
//        log.info("agencyCode:{}, text:{}", agencyCode, text);


        // 获取得到理货申请添加表体集合
//        List<BRailwayTallyBillInfoSave> billInfoList = new ArrayList<>();
//        billInfoList.add(BRailwayTallyBillInfoSave.builder().contaNo("111111").sealNo("aaaaaa").build());
//        billInfoList.add(BRailwayTallyBillInfoSave.builder().contaNo("111111").sealNo("aaaaaa").build());
//        billInfoList.add(BRailwayTallyBillInfoSave.builder().contaNo("222222").sealNo("aaaaaa").build());
//
//        // 校验同一个集装箱， 封志号不能重复
//        Map<String, List<BRailwayTallyBillInfoSave>> maps = billInfoList.stream().collect(Collectors.groupingBy(e -> fetchGroupKey(e)));
//        maps.values().stream().forEach(x ->{
//            System.out.println(x.size());
//        });

        String sealNo = "M/32/D,M/32/D,T/22/E";
        String[] sealNos = sealNo.split(Constants.COMMA);
        Map<Object, List<String>> maps = Arrays.stream(sealNos).collect(Collectors.groupingBy(e -> e));
        maps.keySet().stream().forEach(x -> {
            List<String> stringList = maps.get(x);
            System.out.println(stringList.size());
        });

    }

    /**
     * 多个属性拼接出一个组合属性
     *
     * @param save
     * @return
     */
    private static String fetchGroupKey(BRailwayTallyBillInfoSave save) {
        // 集装箱号+封志号拼接
        return save.getContaNo() + save.getSealNo();
    }

}

