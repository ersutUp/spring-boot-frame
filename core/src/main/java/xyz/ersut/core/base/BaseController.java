package xyz.ersut.core.base;


import xyz.ersut.core.result.ResultJson;
import xyz.ersut.core.result.code.Resultcode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


@Slf4j
/**
 * @author 王二飞
 */
public class BaseController extends Base {



    @InitBinder({"page"})
    public void initBinderPage(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("page.");
    }

    /**
     * 设置ajax请返回结果
     *
     * @param resultcode
     *            状态码及提示信息
     * @param data
     *            返回数据结果对象
     */
    public <T> ResultJson<T> setJson(Resultcode resultcode, T data) {
        return ResultJson.generateResultJson(resultcode,data);
    }

}
