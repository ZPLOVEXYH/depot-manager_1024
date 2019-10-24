package cn.samples.depot.common.utils;

import cn.samples.depot.common.model.msmq.ManifestBase;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.io.xml.XppReader;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings(value = {"unchecked", "deprecation", "rawtypes"})
public class XstreamUtil {

    public static final String xmlHeadRegex = "<\\?xml[\\s\\.\\d\\w-=\"]+\\?>\\r?\\n?";

    /**
     * XML转bean
     *
     * @param rootName
     * @param xml
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T parseXMLWithRootName(final String rootName, String xml, Class<T> cls) {
        if (!StringUtils.isNotBlank(xml)) {
            return null;
        }
        XStream xstream = new XStream(new XppDriver() {
            final String root = upperToHump(rootName);

            @Override
            public HierarchicalStreamReader createReader(Reader in) {
                return new XppReader(in) {
                    @Override
                    protected String pullElementName() {
                        String name = XstreamUtil.upperToHump(super.pullElementName());
                        if (root.equals(name)) {
                            return cls.getName();
                        } else {
                            return name;
                        }
                    }
                };
            }

        }) {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        if (definedIn == Object.class) {
                            return false;
                        }
                        return super.shouldSerializeMember(definedIn, fieldName);
                    }
                };
            }
        };
        xstream.aliasSystemAttribute(null, "class");// 去除class属性
        xstream.autodetectAnnotations(true);
        xstream.processAnnotations(cls);
        return (T) xstream.fromXML(xml);
    }

    /**
     * bean 转换成xml
     *
     * @param o
     * @return
     */
    public static String toXML(Object o) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + toXMLNOHeader("", o);
    }

    /**
     * 拼接xml报文头
     *
     * @param xmlBody
     * @return
     */
    public static String xmlAppendHead(String xmlBody) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xmlBody;
    }

    /**
     * bean 转换成xml
     *
     * @param o
     * @return
     */
    public static String toXMLWithRootName(String rootName, Object o) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + toXMLNOHeader(rootName, o);
    }

    /**
     * bean 转换成xml
     *
     * @param o
     * @return
     */
    public static String toXMLNOHeader(String rootName, Object o) {
        if (o == null) {
            return "";
        }
        XStream xstream = new XStream(new XppDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new CompactWriter(out, new NoNameCoder()) {
                    @Override
                    public void startNode(String name) {
                        super.startNode(XstreamUtil.humpToUpper(name));
                    }

                    @Override
                    public void endNode() {
                        super.endNode();
                    }

                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        super.writeText(writer, text);
                    }
                };
            }
        });
        if (!"".equals(rootName)) {
            xstream.alias(upperToHump(rootName), o.getClass());
        }
        xstream.autodetectAnnotations(true);
        xstream.processAnnotations(o.getClass());
        return xstream.toXML(o);
    }

    /**
     * 获取xml文本中某个节点的文本值
     *
     * @param xml      查找的xml文本
     * @param nodeName 节点名称
     * @return 目标节点的文本值
     */
    public static String getNodeText(String xml, String nodeName) {
        String regex = "<(" + nodeName + ")>([^<>]*)</\\1>";
        Matcher m = Pattern.compile(regex, Pattern.DOTALL).matcher(xml);
        if (m.find()) {
            return m.group(2);
        } else {
            return null;
        }
    }

    /**
     * 获取xml文本中某个节点的文本值
     *
     * @param xml      查找的xml文本
     * @param nodeName 节点名称
     * @return 目标节点的文本值
     */
    public static String getNodeTextByNum(String xml, String nodeName, int num) {
        String regex = "<(" + nodeName + ")>([^<>]*)</\\1>";
        Matcher m = Pattern.compile(regex, Pattern.DOTALL).matcher(xml);
        int i = 1;
        while (m.find()) {
            if (i++ == num) {
                return m.group(2);
            }
        }
        return null;
    }

    /**
     * 用给定的文本替换目标节点的文本值
     *
     * @param xml       查找的xml文本
     * @param nodeName  替换的节点
     * @param textValue 新的节点文本值
     * @return 经过替换的xml
     */
    public static String replaceNodeText(String xml, String nodeName, String textValue) {
        String regex = "<(" + nodeName + ")>([^<>]*)</\\1>";
        Matcher m = Pattern.compile(regex, Pattern.DOTALL).matcher(xml);
        xml = m.replaceAll("<" + nodeName + ">" + textValue + "</" + nodeName + ">");
        return xml;
    }

    /**
     * add by
     *
     * @param nodeName
     * @param xmlStr
     * @param defaultValue
     * @return
     */
    public static String getXmlNodeValue(String nodeName, String xmlStr, String defaultValue) {
        String regex = "<(" + nodeName + ")>(.*)</\\1>";
        Matcher m = Pattern.compile(regex, Pattern.DOTALL).matcher(xmlStr);
        if (m.find()) {
            return m.group(2);
        } else {
            return null;
        }
    }

    /**
     * add by
     *
     * @param nodeName
     * @param xmlStr
     * @param replaceValue
     * @return
     */
    public static String replaceXmlNodeValue(String nodeName, String xmlStr, String replaceValue) {
        try {
            String regex = "<(" + nodeName + ")>(.*)</\\1>";
            Matcher m = Pattern.compile(regex, Pattern.DOTALL).matcher(xmlStr);
            xmlStr = m.replaceAll("<" + nodeName + ">" + replaceValue + "</" + nodeName + ">");
            return xmlStr;
        } catch (Exception ex) {
            return xmlStr;
        }
    }

    /**
     * 移除XML格式文件的头一行
     *
     * @param xmlCont 原始xml文本
     * @return 修改后的xml文本
     */
    public static String removeXmlHeader(String xmlCont) {
        return xmlCont.replaceFirst(xmlHeadRegex, "");
    }

    /**
     * 驼峰转大写下划线格式
     *
     * @param name
     * @return
     */
    public static String humpToUpper(String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(name.substring(0, 1).toLowerCase());
        for (int i = 1; i < name.length(); ++i) {
            String s = name.substring(i, i + 1);
            String slc = s.toLowerCase();
            if (!(s.equals(slc))) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString().toUpperCase();
    }

    /**
     * 大写下划线转驼峰格式
     *
     * @param name
     * @return
     */
    public static String upperToHump(String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < name.length(); ++i) {
            String s = name.substring(i, i + 1);
            String slc = s.toLowerCase();
            if ("_".equals(slc)) {
                if (i + 1 < name.length()) {
                    i++;
                    String s2 = name.substring(i, i + 1);
                    String suc = s2.toUpperCase();
                    result.append(suc);
                }
            } else {
                result.append(slc);
            }
        }
        return result.toString();
    }

    public static void main(String[] args) throws IOException {

        String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><REL10010><MANIFEST_HEAD_NEW><MANIFEST_ID>string</MANIFEST_ID><I_E_FLAG>string</I_E_FLAG><AGENT_CODE>string</AGENT_CODE><TRANSIT_CODE>string</TRANSIT_CODE><VOYAGE_NO>string</VOYAGE_NO><DECL_TRAF_MODE>string</DECL_TRAF_MODE><TRANSPORT_MODE>string</TRANSPORT_MODE><TRANSPORT_ID>string</TRANSPORT_ID><TRANSPORT_NAME>string</TRANSPORT_NAME></MANIFEST_HEAD_NEW><MANIFEST_LIST_NEW><MANIFEST_ID>string</MANIFEST_ID><BILL_NO>string</BILL_NO><BILL_TYPE>string</BILL_TYPE><PARENT_BILL_NO>string</PARENT_BILL_NO><I_E_FLAG>string</I_E_FLAG><DECL_TRAF_MODE>string</DECL_TRAF_MODE><VOLUME>0.0</VOLUME><VOLUME_UNIT>string</VOLUME_UNIT><LOAD_CODE>string</LOAD_CODE><LOAD_DATE>2018-03-19 09:23:13.795 UTC</LOAD_DATE><UNLOAD_CODE>string</UNLOAD_CODE><UNLOAD_DATE>2018-03-19 09:23:13.795 UTC</UNLOAD_DATE><WRAP_TYPE>string</WRAP_TYPE><PACK_NO>0</PACK_NO><GROSS_WT>0.0</GROSS_WT><WEIGHT_UNIT>string</WEIGHT_UNIT><CUSTOMS_CODE>string</CUSTOMS_CODE></MANIFEST_LIST_NEW><MANIFEST_CONTA_NEW><MANIFEST_ID>string</MANIFEST_ID><I_E_FLAG>string</I_E_FLAG><DECL_TRAF_MODE>string</DECL_TRAF_MODE><CONTA_ID>string</CONTA_ID><CONTA_TYPE>string</CONTA_TYPE><CONTA_SOUR>string</CONTA_SOUR><EMPTY_CONTA_MARK>string</EMPTY_CONTA_MARK></MANIFEST_CONTA_NEW><MANIFEST_LIST_CONTA_NEW><MANIFEST_ID>string</MANIFEST_ID><CONTA_ID>string</CONTA_ID><BILL_NO>string</BILL_NO><I_E_FLAG>string</I_E_FLAG><DECL_TRAF_MODE>string</DECL_TRAF_MODE><BILL_TYPE>string</BILL_TYPE><PARENT_BILL_NO>string</PARENT_BILL_NO></MANIFEST_LIST_CONTA_NEW><MANIFEST_GOODS_NEW><MANIFEST_ID>string</MANIFEST_ID><CONTA_ID>string</CONTA_ID><BILL_NO>string</BILL_NO><BILL_TYPE>string</BILL_TYPE><PARENT_BILL_NO>string</PARENT_BILL_NO><I_E_FLAG>string</I_E_FLAG><DECL_TRAF_MODE>string</DECL_TRAF_MODE><PACK_NO>0</PACK_NO><WRAP_TYPE>string</WRAP_TYPE><GOODS_MEMO>string</GOODS_MEMO><GOODS_DETAILS>string</GOODS_DETAILS><GROSS_WT>0.0</GROSS_WT><WEIGHT_UNIT>string</WEIGHT_UNIT></MANIFEST_GOODS_NEW><MANIFEST_COP_NEW><MANIFEST_ID>string</MANIFEST_ID><BILL_NO>string</BILL_NO><BILL_TYPE>string</BILL_TYPE><PARENT_BILL_NO>string</PARENT_BILL_NO><I_E_FLAG>string</I_E_FLAG><TRADE_CO>string</TRADE_CO><TRADE_NAME>string</TRADE_NAME><TRADE_TYPE>string</TRADE_TYPE></MANIFEST_COP_NEW></REL10010>";

        ManifestBase o = XstreamUtil.parseXMLWithRootName("rel10010", xmlStr, ManifestBase.class);

        System.out.println(o.getClass().getName());

        String path = "d://";
        // File file = new File(path);
        // OutputStream outputStream = new FileOutputStream(file);
        // outputStream.write(xmlStr.getBytes());
        // outputStream.flush();
        // outputStream.close();

        // FileUtil.createFile(path, "text.txt");
        FileUtils.writeContent(path, "text.txt", xmlStr);
        FileUtils.delFile(path, "text.txt");

    }
}
