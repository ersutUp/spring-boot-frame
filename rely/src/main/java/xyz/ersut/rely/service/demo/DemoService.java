package xyz.ersut.rely.service.demo;

import xyz.ersut.rely.v1.controller.demo.condition.DemoV1Condition;
import xyz.ersut.rely.v1.controller.demo.result.DemoV1Result;

public interface DemoService {

    public DemoV1Result demoCard(DemoV1Condition demoV1Condition);

}
