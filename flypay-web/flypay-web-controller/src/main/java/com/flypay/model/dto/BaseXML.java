package com.flypay.model.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class BaseXML {

    @XStreamAlias("return_code")
    public String returnCode;
    @XStreamAlias("return_msg")
    public String returnMsg;
}
