package com.hogwarts.utils;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Description:XML和bean相互转换
 * Date: 2015/11/5 19:51
 * Author: zhaozhiwei 程涛
 */
@SuppressWarnings("all")
public class XmlUtil {

    public static final String ENCODEING_UTF8 = "UTF-8";
    public static final String ENCODEING_GBK = "GBK";
    public static final String ENCODEING_GB2312 = "GB2312";
    public static final String ENCODEING_ISO = "ISO-8859-1";

    private static final String XML_NAMESPACE_BEFORE = "ns2:";
    private static final String XML_NAMESPACE_AFTER = ":ns2";
    private static final String BLANK_STRING = "";

    /**
     * Object转换成xml,默认编码UTF-8
     *
     * @param obj 待转换对象
     * @return
     */
    public static String convertToXml(Object obj) throws Exception {
        return convertToXml(obj, ENCODEING_UTF8);
    }

    /**
     * Object转换成xml,可以指定编码
     *
     * @param obj      待转换对象
     * @param encoding 编码,ENCODEING_UTF8或ENCODEING_GBK
     * @return
     */
    public static String convertToXml(Object obj, String encoding) throws Exception {
        if (obj == null) {
            return null;
        }
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
            //marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
            //去掉XML命名空间的头
            result = result.replaceAll(XML_NAMESPACE_BEFORE, BLANK_STRING).replaceAll(XML_NAMESPACE_AFTER, BLANK_STRING);
        } catch (Exception e) {
            throw new Exception("Bean转换xml失败:" + e.getMessage());
        }
        return result;
    }

    /**
     * xml转换成Object
     *
     * @param xml  XML数据
     * @param bean 待转换对象
     * @return
     */
    public static <T> T convertToObject(String xml, Class<T> bean) throws Exception {
        if (StringUtils.isBlank(xml)) {
            return null;
        }
        T t = null;
        try {
            JAXBContext context = JAXBContext.newInstance(bean);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            throw new Exception("xml转换成Bean失败:" + e.getMessage());
        }
        return t;
    }

    /**
     * 解析xml,返回第一级元素键值对map。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strXml xlm数据
     * @return
     * @throws Exception
     */
    public static Map<String, String> covertToMap(String strXml) throws Exception {
        if (StringUtils.isBlank(strXml)) {
            return null;
        }
        strXml = strXml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        InputStream in = new ByteArrayInputStream(strXml.getBytes(ENCODEING_UTF8));
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(in);
        Map<String, String> connectionInfo = new HashMap<String, String>();

        Element root = document.getRootElement();
        Iterator<Element> rootIter = root.elementIterator();
        while (rootIter.hasNext()) {
            Element ele = rootIter.next();
            String value = ele.getText();
            String key = ele.getName();
            connectionInfo.put(key, value);
        }
        return connectionInfo;
    }

    /**
     * 从XML获得MAP中包含的节点的值
     *
     * @param xml      xm数据
     * @param paramMap 待获得值的节点集合
     * @throws Exception
     */
    public static void covertToMap(String xml, Map<String, String> paramMap) throws Exception {
        if (StringUtils.isBlank(xml)) {
            return;
        }
        SAXParserFactory sf = SAXParserFactory.newInstance();
        SAXParser sp = sf.newSAXParser();
        try {
            sp.parse(new ByteArrayInputStream(xml.getBytes(ENCODEING_UTF8)), new MapHandler(paramMap));
        } catch (SAXException e) {
            throw new Exception("从xml中获得节点值失败:" + e.getMessage());
        } catch (IOException e) {
            throw new Exception("从xml中获得节点值失败:" + e.getMessage());
        }
    }
}

/*Description:
 Date: 2015年11月5日 下午3:04:53
 Author: 程涛
 */
class MapHandler extends DefaultHandler {
    private Map<String, String> paramMap;
    private String key;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (paramMap.containsKey(qName)) {
            key = qName;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        key = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (key != null) {
            String value = new String(ch, start, length);
            paramMap.put(key, value);
        }
    }

    public MapHandler(Map<String, String> paramMap) {
        super();
        this.paramMap = paramMap;
    }
}
