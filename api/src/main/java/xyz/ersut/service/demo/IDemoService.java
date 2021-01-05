package xyz.ersut.service.demo;

import xyz.ersut.core.exception.code.CodeException;
import xyz.ersut.core.result.code.ResultThirdCode;
import xyz.ersut.rely.service.demo.DemoService;
import xyz.ersut.rely.v1.controller.demo.condition.DemoV1Condition;
import xyz.ersut.rely.v1.controller.demo.result.DemoV1Result;
import org.springframework.stereotype.Service;

@Service
public class IDemoService implements DemoService {

    public DemoV1Result demoCard(DemoV1Condition demoV1Condition) {
        //获取数据库数据
        Integer cardNum = demoV1Condition.getDemo();
        //验证数据是否合法
        if(cardNum <= 0){
            throw new CodeException(ResultThirdCode.ERROR);
        }
        //构造返回体
        DemoV1Result demoV1Result = DemoV1Result.builder()
                .demo(cardNum)
                .build();
        return demoV1Result;
    }
}
