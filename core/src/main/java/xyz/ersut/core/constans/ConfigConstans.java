package xyz.ersut.core.constans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author 王二飞
 */
@Component
public class ConfigConstans {
    public static String serverMode;

    @PostConstruct
    public void init(){
        serverMode = mode;
    }

    /**前缀*/
    @Value("${prefix:}")
    public String prefix;

    /**文件根目录*/
    @Value("${project.file.root}")
    public String rootPath;

    /**文件根目录*/
    @Value("${project.mode}")
    public String mode;

}
