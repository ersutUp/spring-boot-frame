package xyz.ersut.core.util.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 王二飞
 */
public class RegexUtils {

    //根据正则替换字符串
    private static String getRegExString(String src, String regEx) {
        if(src==null) {
            return "";
        }
        Pattern p_script = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(src);
        src = m_script.replaceAll("");
        return src.trim();
    }
}
