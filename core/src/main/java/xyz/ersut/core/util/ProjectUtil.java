package xyz.ersut.core.util;

import xyz.ersut.core.constans.ConfigConstans;
import xyz.ersut.core.log.FieldLog;

import java.util.Objects;

public class ProjectUtil {

    public static FieldLog.Format format(){
        if(Objects.equals(ConfigConstans.serverMode,"dev")){
            return FieldLog.Format.TEXT;
        } else {
            return FieldLog.Format.JSON;
        }
    }

}
