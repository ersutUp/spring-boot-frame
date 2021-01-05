package xyz.ersut.core.config.body;

import org.springframework.context.annotation.DependsOn;
import xyz.ersut.core.config.body.request.RequestWrapper;
import xyz.ersut.core.config.body.response.ResponseBodyWrapper;
import xyz.ersut.core.constans.ConfigConstans;
import xyz.ersut.core.log.FieldLog;
import xyz.ersut.core.result.ResultJson;
import xyz.ersut.core.util.ProjectUtil;
import xyz.ersut.core.util.json.JsonUtil;
import xyz.ersut.core.util.verify.StringUtils;
import xyz.ersut.core.util.web.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@WebFilter(value = "/*",filterName = "bodyFilter")
@Order(1)
//优先初始化 configConstans ，便于 ProjectUtil.format() 使用
@DependsOn("configConstans")
public class BodyFilter implements Filter {

    //log对象
    private FieldLog log;

    private final int timeOut = 3000;

    @PostConstruct
    public void init(){
        log = new FieldLog(BodyFilter.class, ProjectUtil.format());
    }

    public BodyFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RequestWrapper myRequestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);

        String reqID = UUID.randomUUID().toString();
        ((HttpServletResponse)servletResponse).setHeader("X-Request-Id",reqID);
        FieldLog.setReqID(reqID);

        //开始请求时间
        Long beginTime= System.currentTimeMillis();
        //获取客户端ip
        String ip = WebUtils.getIpAddr(myRequestWrapper);
        //请求参数
        String requestBody = myRequestWrapper.getBody();

        log.field("url",myRequestWrapper.getRequestURI())
                .field("ip",ip)
                .field("param", JsonUtil.toJSon(myRequestWrapper.getParameterMap()))
                .field("reqBody",requestBody)
                .info("reqest start");

        filterChain.doFilter(myRequestWrapper, servletResponse);

        int responseCode = 0;
        String responseMessage = "";
        String contentType = servletResponse.getContentType();

        //如果是响应体是 json 格式
        if(StringUtils.isNotBlank(contentType)
                && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            //获取响应体
            ResponseBodyWrapper myResponseBodyWrapper = new ResponseBodyWrapper((HttpServletResponse)servletResponse);
            //获取响应数据
            String responseBody = new String(myResponseBodyWrapper.getResponseData());

            ResultJson resultJson = JsonUtil.json2Object(requestBody, ResultJson.class);
            responseCode = resultJson.code;
            responseMessage = resultJson.message;

            //这三行是必须的！！！！获取原本的ResponseBodyWrapper包装的 response ，将响应数据放入
            OutputStream out = servletResponse.getOutputStream();
            out.write(responseBody.getBytes());
            out.flush();
        }

        //计算请求时间
        long elapsedTime = -1;
        if(beginTime!=null&&beginTime>0){
            Long endTime = System.currentTimeMillis();
            elapsedTime = endTime-beginTime;
        }

        //打印日志
        Map<String,Object> resultLog = new HashMap<>();
        resultLog.put("resMS",elapsedTime);
        resultLog.put("code",responseCode);
        resultLog.put("msg",responseMessage);
        resultLog.put("status",((HttpServletResponse) servletResponse).getStatus());
        if(elapsedTime > timeOut){
            log.fieldAll(resultLog).warn("request end but tiomout");
        } else {
            log.fieldAll(resultLog).info("request end but tiomout");
        }

    }

    @Override
    public void destroy() {
        FieldLog.closeReqID();
    }
}
