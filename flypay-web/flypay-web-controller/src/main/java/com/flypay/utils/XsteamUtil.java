package com.flypay.utils;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

/**
 * xml转换工具
 */
public class XsteamUtil {
    private static final XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
    /**
     * xml转对象
     * @param clazz
     * @param xml
     * @param <T>
     * @return
     */
    public static <T> T  toBean(Class<T> clazz, String xml) {
            xstream.processAnnotations(clazz);
            xstream.autodetectAnnotations(true);
            xstream.setClassLoader(clazz.getClassLoader());
            return (T)xstream.fromXML(xml);
    }

    /**
     * 对象转xml
     * @param obj
     * @return
     */
    public static String toXml(Object obj,Class clazz){
        xstream.processAnnotations(clazz);
        return xstream.toXML(obj);
    }
}