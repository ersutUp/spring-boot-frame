package xyz.ersut.common.intercepter;

import xyz.ersut.core.config.body.BodyFilter;
import xyz.ersut.core.constans.ConfigConstans;
import xyz.ersut.core.log.FieldLog;
import xyz.ersut.core.result.ResultJson;
import xyz.ersut.core.result.code.ResultSystemCode;
import xyz.ersut.core.util.ProjectUtil;
import xyz.ersut.core.util.json.JsonUtil;
import xyz.ersut.core.util.verify.ObjectUtils;
import xyz.ersut.core.util.verify.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Component
/**
 * @author 王二飞
 *
 * 1、时效性验证（上下60秒）
 * 2、referrer的验证
 * 3、token的验证
 * */
public class ApiVerifyInterceptor extends HandlerInterceptorAdapter {
    private FieldLog log;
    //超时时间
    private static final int TIMEOUT = 60000;

    @Value("${project.legal.referrer}")
    private String[] legalReferrer;

    @PostConstruct
    public void init(){
        log = new FieldLog(BodyFilter.class, ProjectUtil.format());
    }

    public ApiVerifyInterceptor() {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /*时效性验证begin*/
        String timestamp = request.getHeader("timestamp");
        //非空
        if(StringUtils.isEmpty(timestamp)){
            this.resultTiemOut(response);
            log.info("timestamp not find");
            return false;
        }
        //传来的时间戳(世界协调时间)
        long timestampInt = new Long(timestamp);

        //时间偏移量
        Calendar cal = Calendar.getInstance();
        int offset = cal.get(Calendar.ZONE_OFFSET);
        //服务端时间戳计算后为世界协调时间
        long nowTime = System.currentTimeMillis()+offset;

        //时间戳验证
        if( Math.abs(nowTime - timestampInt) > ApiVerifyInterceptor.TIMEOUT){
            this.resultTiemOut(response);
            log.field("timestamp",timestamp).info("timestamp timeout");
            return false;
        }
        /*时效性验证end*/

        /*Referrer验证begin*/
        //获取Referrer值
        String referrer = request.getHeader("Referer");
        //不存在则非法请求
        if(StringUtils.isEmpty(referrer)){
            this.resultIllegal(response);
            return false;
        } else {
            List<String> legalReferrerList = Arrays.asList(this.legalReferrer);
            //请求的 referrer 不存在系统的配置中 则返回非法请求
            if (ObjectUtils.isNotNull(legalReferrerList)) {
                boolean isLegal = legalReferrerList.contains(legalReferrerList);
                if(!isLegal){
                    this.resultIllegal(response);
                    return false;
                }
            } else {
                //未配置project.legal.referrer 返回成功
                return true;
            }
        }
        /*Referrer验证end*/

        return true;
    }

    private void resultIllegal(HttpServletResponse response) throws IOException {
        ResultJson resultJson = ResultJson.generateResultJson(ResultSystemCode.ILLEGAL);
        this.setErrorResponse(response,resultJson);
    }

    private void resultTiemOut(HttpServletResponse response) throws IOException {
        ResultJson resultJson = ResultJson.generateResultJson(ResultSystemCode.TIMEOUT);
        this.setErrorResponse(response,resultJson);
    }


    private <T>  void setErrorResponse(HttpServletResponse response,ResultJson<T> resultJson) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream= response.getOutputStream();
            outputStream.write(JsonUtil.toJSon(resultJson).getBytes());
        }catch (Exception e ){
            if (outputStream != null) {
                outputStream.close();
            }
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

}
