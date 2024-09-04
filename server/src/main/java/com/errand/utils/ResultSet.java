package com.errand.utils;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResultSet {

    private Boolean success;

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<String, Object>();

    private ResultSet(){}

    public static ResultSet ok(){
        ResultSet r = new ResultSet();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("成功");
        return r;
    }

    public static ResultSet error(){
        ResultSet r = new ResultSet();
        r.setSuccess(false);
        r.setCode(ResultCode.ERROR);
        r.setMessage("失败");
        return r;
    }

    public ResultSet success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public ResultSet message(String message){
        this.setMessage(message);
        return this;
    }

    public ResultSet code(Integer code){
        this.setCode(code);
        return this;
    }

    public ResultSet data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public ResultSet data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

}
