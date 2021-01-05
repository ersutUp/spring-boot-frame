package xyz.ersut.core.util.web;

import xyz.ersut.core.util.verify.StringUtils;
import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Pattern;

public class WebUtils {

	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
	private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
	private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

	/**获取请求IP地址*/
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if(index != -1){
				return ip.substring(0,index);
			}else{
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
			return ip;
		}
        ip = request.getHeader("Proxy-Client-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
		return request.getRemoteAddr();
	}

	/**获取用户访问浏览器信息 （如有问题及时反馈）*/
	public static String getUserAgent(HttpServletRequest request) {
		//获取浏览器信息
    	String uabrow = request.getHeader("User-Agent");
    	UserAgent userAgent =UserAgent.parseUserAgentString(uabrow);
    	Browser browser = userAgent.getBrowser();
        OperatingSystem os = userAgent.getOperatingSystem();
    	return browser.getName().toLowerCase()+";"+os.getName().toLowerCase();
    }

	/** 判断referer*/
	public static boolean compareReferer(String referer,String trueUrl){
		String[]baseUrl = referer.split("/");
		String refererUrl = baseUrl[0]+"//"+baseUrl[2];
		return refererUrl.equals(trueUrl);
	}

	/**
	 * 链接去除域名和协议
	 * @param url 链接
	 * @return 去除域名以及协议后的链接
	 */
	public static String urldelHost(String url){

		String regex = "^(http://|https://).*$";
		String http = "http://";
		String https = "https://";

		//查询是否含有协议
		if(Pattern.matches(regex,url)){
			int len = 0;
			//查询协议
			if (url.indexOf(http) == 0 ){
				len = http.length();
			}else if (url.indexOf(https) == 0 ){
				len = https.length();
			}

			//不是协议开头返回原值
			if(len == 0){
				return url;
			}

			//截掉协议
			url = url.substring(len);

			//查询查询域名后的第一个 斜杠位置
			int lineIndex = url.indexOf("/");

			//没有斜杆返回空
			if(lineIndex == -1){
				return "";
			}

			//截取域名
			return url.substring(lineIndex);

		} else {
			return url;
		}
	}

}