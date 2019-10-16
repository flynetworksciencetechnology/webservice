package com.flypay.common.constant;

/**
 * 服务商常量类
 */
public class ServiceConstansts {
    /** 服务商名称是否唯一的返回结果码 */
    public final static String PROVIDER_NAME_UNIQUE = "0";
    public final static String PROVIDER_NAME_NOT_UNIQUE = "1";

    /** 商戶名称是否唯一的返回结果码 */
    public final static String MERCHANT_NAME_UNIQUE = "0";
    public final static String MERCHANT_NAME_NOT_UNIQUE = "1";

    /** 门店称是否唯一的返回结果码 */
    public final static String STORE_NAME_UNIQUE = "0";
    public final static String STORE_NAME_NOT_UNIQUE = "1";

    /** 服务商APPID是否唯一的返回结果码 */
    public final static String PROVIDER_APPID_UNIQUE = "0";
    public final static String PROVIDER_APPID_NOT_UNIQUE = "1";

    /** 商戶APPID是否唯一的返回结果码 */
    public final static String MERCHANT_APPID_UNIQUE = "0";
    public final static String MERCHANT_APPID_NOT_UNIQUE = "1";

    /** 状态*/
    public final static String USING_STATUS = "0";
    public final static String STOP_STATUS = "1";
    /** 删除状态*/
    public final static String DEL_FLAG = "2";
    public final static String NOT_DEL_FLAG = "0";

    public final static String REDIS_SEPARATOR = ":";
    public final static String REDIS_WILDCARD = "*";

    public final static String EQUIPMENT_RUN_STATUS = "equipment_run_status";
    public static enum EQUIPMENT_RUN_NAME{
        运行中,闲置,停止运行;
    }
    public static enum EQUIPMENT_REDIS_TYPE{
        RUN_TYPE,INFO;
    }
}
