package com.flypay.utils;

import com.flypay.model.dto.WxpayfaceAuthinfoParamDTO;
import com.flypay.utils.comparator.ParamKeyComparator;

import java.lang.reflect.Field;
import java.util.*;

public class CommonUtils {
    private static final String RESOURCE = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
    /**
     * 生成指定长度的随机字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length){
        //1.  定义一个字符串（A-Z，a-z，0-9）即62个数字字母；
        //2.  由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //3.  长度为几就循环几次
        for(int i=0; i<length; ++i){
            //从62个的数字或字母中选择
            int number=random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(RESOURCE.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

    /**
     * 获取微信签名
     * @param obj  微信参数
     * @param key  key为商户平台设置的密钥key
     */
    static final List<String> TYPES1 = new ArrayList<String>(){
        {
            add("int");
            add("java.lang.String");
            add("boolean");
            add("char");
            add("float");
            add("double");
            add("long");
            add("short");
            add("byte");

        }
    };
    static final List<String> TYPES2 = new ArrayList<String>(){
        {
            add("Integer");
            add("java.lang.String");
            add("java.lang.Boolean");
            add("java.lang.Character");
            add("java.lang.Float");
            add("java.lang.Double");
            add("java.lang.Long");
            add("java.lang.Short");
            add("java.lang.Byte");

        }
    };
    public static String sign(Object obj, Class clazz,String key) {
        //通过反射将有值的key和value放入map
        Map<String, String> map = new TreeMap<>(new ParamKeyComparator());
        Field[] fields = clazz.getDeclaredFields();
        for(int i = 0 ; i < fields.length ; i++) {
            //设置是否允许访问，不是修改原来的访问权限修饰词。
            Field field = fields[i];
            field.setAccessible(true);
            String fieldType = field.getType().getName();
            String fieldName = field.getName();
            if( TYPES1.contains(fieldType) || TYPES2.contains(fieldType)){
                try {
                    String fieldValue = String.valueOf(field.get(obj));
                    map.put(fieldName,fieldValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //构造签名字符串
        // 构造签名键值对的格式
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> item : map.entrySet()) {
            if (item.getKey() != null || item.getKey() != "") {
                String tempKey = item.getKey();
                String tempVal = item.getValue();
                if (!(tempVal == "" || tempVal == null)) {
                    sb.append(tempKey + "=" + tempVal + "&");
                }
            }
        }
        String stringA = sb.append("&key=").append(key).toString();
        //MD5签名
        return Md5Digest.md5(stringA).toUpperCase();
    }


}
