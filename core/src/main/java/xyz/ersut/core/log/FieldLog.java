package xyz.ersut.core.log;

import xyz.ersut.core.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FieldLog {

    private final static String REQ_ID_KEY = "reqID";

    //日志格式
    public enum Format {
        JSON,
        TEXT
    }

    //请求id
    private static ThreadLocal<String> reqID = new ThreadLocal<>();
    public static void closeReqID() {
        reqID.remove();
    }
    public static void setReqID(String reqID) {
        FieldLog.reqID.set(reqID);
    }


    //日志对象
    private Logger log;

    //日志使用的格式
    private Format format;

    //字段信息
    private Map<String,Object> fieldData = new HashMap<>();

    private FieldLog(){

    }

    public FieldLog(Class cls,Format format){
        this.format = format;
        log = LoggerFactory.getLogger(cls);
    }

    //添加字段信息
    public FieldLog field(String key, String val){
        fieldData.put(key,val);
        return this;
    }
    public FieldLog field(String key, Integer val){
        fieldData.put(key,val);
        return this;
    }
    public FieldLog field(String key, Long val){
        fieldData.put(key,val);
        return this;
    }
    public FieldLog field(String key, Double val){
        fieldData.put(key,val);
        return this;
    }
    public FieldLog fieldAll(Map<String,Object> fieldData){
        this.fieldData.putAll(fieldData);
        return this;
    }

    //清除field数据
    public void clear(){
        fieldData.clear();
    }

    //复制这个对象
    public FieldLog copy(){
        FieldLog fieldLog = new FieldLog();
        fieldLog.fieldData.putAll(this.fieldData);
        fieldLog.format = this.format;
        fieldLog.log = this.log;
        return fieldLog;
    }

    //info日志打印
    public void info(String msg){
        if (log.isInfoEnabled()) {
            fieldData.put("msg", msg);
            log.info(field2String());
        }
        this.clear();
    }

    //error日志打印
    public void error(String msg,Throwable e){
        if (log.isErrorEnabled()) {
            fieldData.put("msg", msg);
            log.error(field2String(), e);
        }
        this.clear();
    }

    //warn日志打印
    public void warn(String msg){
        if (log.isWarnEnabled()) {
            fieldData.put("msg", msg);
            log.warn(field2String());
        }
        this.clear();
    }

    //debug日志打印
    public void debug(String msg){
        if (log.isDebugEnabled()) {
            fieldData.put("msg", msg);
            log.debug(field2String());
        }
        this.clear();
    }

    //trace日志打印
    public void trace(String msg){
        if(log.isTraceEnabled()){
            fieldData.put("msg",msg);
            log.trace(field2String());
        }
        this.clear();
    }

    //字段信息转换为 string 准备打印
    private String field2String(){
        //添加请求id
        if(reqID.get() != null){
            fieldData.put(REQ_ID_KEY,reqID.get());
        }

        if(fieldData.size() <= 0){
            return "";
        }

        StringBuilder logStr = new StringBuilder();

        if(Format.JSON.equals(format)){
            return JsonUtil.toJSon(fieldData);
        } else if (Format.TEXT.equals(format)){
            Set<Map.Entry<String, Object>> data = fieldData.entrySet();
            for (Map.Entry<String, Object> entry:data) {
                logStr.append(entry.getKey());
                logStr.append("=");
                logStr.append(entry.getValue().toString());
                logStr.append("\t");
            }
        } else {
            logStr.append("FieldLog format err");
        }

        return logStr.toString();
    }


}
