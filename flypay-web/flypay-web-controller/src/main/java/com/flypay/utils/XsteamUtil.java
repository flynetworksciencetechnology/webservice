package com.flypay.utils;

import com.alibaba.fastjson.JSON;
import com.flypay.annotation.XStreamCDATA;
import com.flypay.model.dto.WxpayfaceAuthinfoParamDTO;
import com.flypay.model.dto.WxpayfaceAuthinfoResultDTO;
import com.flypay.model.dto.WxpayfaceParamDTO;
import com.flypay.model.dto.WxpayfaceResultDTO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.*;

import java.io.Writer;
import java.lang.reflect.Field;

/**
 * xml转换工具
 */
public class XsteamUtil {
    private static XStream getXstream(){
        XStream xstream = new XStream(new XppDomDriver(new NoNameCoder()){
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    boolean cdata = false;
                    Class<?> targetClass = null;
                    @Override
                    public void startNode(String name,
                                          @SuppressWarnings("rawtypes") Class clazz) {
                        super.startNode(name, clazz);
                        //业务处理，对于用XStreamCDATA标记的Field，需要加上CDATA标签
                        if(!name.equals("xml")){
                            cdata = needCDATA(targetClass, name);
                        }else{
                            targetClass = clazz;
                        }
                    }
                    @Override
                    public String encodeNode(String name) {
                        return name;
                    }
                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        if (cdata) {
                            writer.write(CDATA(text));
                        } else {
                            writer.write(text);
                        }
                    }
                };
            }
        });
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(new Class[]{WxpayfaceResultDTO.class, WxpayfaceAuthinfoParamDTO.class, WxpayfaceAuthinfoResultDTO.class, WxpayfaceParamDTO.class});
        return xstream;
    }
    private static String CDATA(String text) {
        return "<![CDATA[" + text + "]]>";
    }

    /**
     * xml转对象
     * @param clazz
     * @param xml
     * @param <T>
     * @return
     */
    public static <T> T  toBean(Class<T> clazz, String xml) {
        XStream xstream = getXstream();
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
        XStream xstream = getXstream();
        xstream.processAnnotations(clazz);
        return xstream.toXML(obj);
    }
    // 业余草：www.xttblog.com
    private static boolean needCDATA(Class<?> targetClass, String fieldAlias){
        boolean cdata = false;
        //首先,判断自己的属性是否存在XStreamCDATA标签
        cdata = existsCDATA(targetClass, fieldAlias);
        if(cdata)
            return cdata;
        //如果cdata为false, 则遍历所有的父类直到java.lang.Object
        Class<?> superClass = targetClass.getSuperclass();
        while(!superClass.equals(Object.class)){
            cdata = existsCDATA(superClass, fieldAlias);
            if(cdata)
                return cdata;
            superClass = superClass.getClass().getSuperclass();
        }
        return false;
    }
    // 业余草：www.xttblog.com
    private static boolean existsCDATA(Class<?> clazz, String fieldAlias){
        //反射获取所有属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //1.存在标有XStreamCDATA标签的field
            if(field.getAnnotation(XStreamCDATA.class) != null ){
                XStreamAlias xStreamAlias = field.getAnnotation(XStreamAlias.class);
                //2.存在XStreamAlias属性
                if(null != xStreamAlias){
                    if(fieldAlias.equals(xStreamAlias.value()))
                        return true;
                }else{//不存在XStreamAlias
                    if(fieldAlias.equals(field.getName()))
                        return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String re = "<xml><return_code><![CDATA[FAIL]]></return_code>" +
                "<return_msg><![CDATA[缺少参数]]></return_msg>" +
                "</xml>";

        WxpayfaceResultDTO wxpayfaceResultDTO = XsteamUtil.toBean(WxpayfaceResultDTO.class, re);
        System.out.println(XsteamUtil.toXml(wxpayfaceResultDTO,WxpayfaceResultDTO.class));
    }
}