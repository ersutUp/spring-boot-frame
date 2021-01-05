package xyz.ersut.core.exception.code;

import xyz.ersut.core.exception.base.BaseRuntimeException;
import xyz.ersut.core.result.code.Resultcode;

public class CodeException extends BaseRuntimeException {
    private final Resultcode resultcode;

    public CodeException(Resultcode resultcode){
        super("错误消息["+resultcode.getMessage()+"]，对应状态码:["+resultcode.getCode()+"]");
        this.resultcode = resultcode;
    }

    public Resultcode getResultcode() {
        return resultcode;
    }

}
