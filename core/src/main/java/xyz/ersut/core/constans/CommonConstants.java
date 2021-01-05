package xyz.ersut.core.constans;

public class CommonConstants {

    /*---------------------------------------------------------正则常量--------------------------------------------------------------------------*/
    /**
     * 账户正则表达式 限50个字符，支持中英文、数字、减号或下划线（注意不能超过11个字符，否则跟手机重复了，也不能包含@符号，否则跟邮箱重复了）
     */
    public static final String USERNAME_REGEX = "^[\\u4e00-\\u9fa5_a-zA-Z0-9-]{6,24}$";
    /**账户正则表达式提示*/
    public static final String USERNAME_REGEX_INFO="账号支持英文、数字、减号或下划线且6-24个字符";
    /**邮箱正则表达式*/
    public static final String EMAIL_REGEX="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    /**电话号码正则表达式*/
    public static final String TEL_REGEX="^1[0-9]{10}$";
    /**数字验证正则表达式*/
    public static final String NUMBER_REGEX="^[0-9]*$";
    /**用户密码正则表达式*/
    public static final String USEPWD_REGEX="^[_0-9a-zA-Z]{6,}$";
    /**用户密码正则表达式提示*/
    public static final String USEPWD_REGEX_INFO="密码由字母、数字、下划线组合且不小于6位";
    /**后台用户登录名正则表达式*/
    public static final String LOGIN_REGEX = "^(?=.*[a-zA-Z])[a-zA-Z0-9]{6,20}$";

    /**图片验证码Session的K*/
    public static final String RAND_CODE="COMMON_RAND_CODE";



}
