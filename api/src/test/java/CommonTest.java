import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.auth.In;
import org.junit.Test;

import java.util.Calendar;
import java.util.regex.Pattern;

public class CommonTest {

    @Test
    public void test(){
        Integer i = null;
        Object o = i;
        o = "1";
        if(o instanceof Integer){
            System.out.println("int");
        } else {
            System.out.println("null");
        }
    }
}
