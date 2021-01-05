package xyz.ersut.core.result;

import xyz.ersut.core.result.code.Resultcode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@ApiModel(description = "数据")
public class ResultJson<T> {

    public ResultJson(){

    }

    public ResultJson(Resultcode resultcode, T data){
        this.code = resultcode.getCode();
        this.message = resultcode.getMessage();
        this.data = data;
    }

    public ResultJson(Resultcode resultcode){
        this.code = resultcode.getCode();
        this.message = resultcode.getMessage();
    }

    public ResultJson(Resultcode resultcode,String msg){
        this.code = resultcode.getCode();
        this.message = msg;
    }

    public static ResultJson generateResultJson(Resultcode resultcode){
        return new ResultJson(resultcode);
    }

    public static <T> ResultJson<T> generateResultJson(Resultcode resultcode,T data){
        return new ResultJson(resultcode,data);
    }

    @ApiModelProperty("状态码")
    public int code;
    @ApiModelProperty("消息")
    public String message;
    @ApiModelProperty("显示数据")
    public T data;

    public ResultJson<T> setData(T data){
        this.data = data;
        return this;
    }

}
