package xyz.ersut.v1.controller;

import xyz.ersut.core.base.BaseController;
import xyz.ersut.core.log.FieldLog;
import xyz.ersut.core.result.ResultJson;
import xyz.ersut.core.result.code.ResultSystemCode;
import xyz.ersut.core.util.ProjectUtil;
import xyz.ersut.rely.v1.controller.demo.command.DemoV1Command;
import xyz.ersut.rely.v1.controller.demo.condition.DemoV1Condition;
import xyz.ersut.rely.v1.controller.demo.result.DemoV1Result;
import xyz.ersut.service.demo.IDemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
@Api(tags = {"100、测试"})
@Slf4j
public class DemoV1Controler extends BaseController {

    private static final FieldLog log = new FieldLog(DemoV1Controler.class, ProjectUtil.format());

    @Autowired
    private IDemoService demoService;

    @PostMapping
    @ApiOperation(value = "post测试接口")
    public ResultJson<DemoV1Result> demoPost(HttpServletRequest request, @ApiParam(name = "json",required = true)@RequestBody @Validated DemoV1Command demoV1Command) {
        log.info("test");

        DemoV1Result demoV1Result = null;
        //具体业务进service
//        demoV1Result = service.xxxx(demoV1Command);


        return setJson(ResultSystemCode.SUCCESS, demoV1Result);
    }

    @GetMapping
    @ApiOperation(value = "get测试接口")
    public ResultJson<DemoV1Result> demoGet(HttpServletRequest request, @ApiParam(name = "json",required = true) @Validated DemoV1Condition demoV1Condition) {

        DemoV1Result demoV1Result = null;
        //具体业务进service
//        demoV1Result = service.xxxx(demoV1Condition);

        return setJson(ResultSystemCode.SUCCESS, demoV1Result);
    }

    @DeleteMapping
    @ApiOperation(value = "delete测试接口")
    public ResultJson<DemoV1Result> demoDel(HttpServletRequest request, @ApiParam(name = "json",required = true)@RequestBody @Validated DemoV1Condition demoV1Condition) {

        DemoV1Result demoV1Result = null;

        //具体业务进service
//        demoV1Result = service.xxxx(demoV1Condition);


        return setJson(ResultSystemCode.SUCCESS, demoV1Result);
    }

    @PutMapping
    @ApiOperation(value = "put测试接口")
    public ResultJson<DemoV1Result> demoPut(HttpServletRequest request, @ApiParam(name = "json",required = true)@RequestBody @Validated DemoV1Condition demoV1Condition) {

        DemoV1Result demoV1Result = null;
        //具体业务进service
//        demoV1Result = service.xxxx(demoV1Condition);

        return setJson(ResultSystemCode.SUCCESS, demoV1Result);
    }

    @PostMapping("/codeException")
    @ApiOperation(value = "CodeException")
    public ResultJson<DemoV1Result> demoCodeException(HttpServletRequest request, @ApiParam(name = "json",required = true)@RequestBody @Validated DemoV1Condition demoV1Condition) {

        //具体业务进service
        DemoV1Result demoV1Result =  demoService.demoCard(demoV1Condition);

        return setJson(ResultSystemCode.SUCCESS, demoV1Result);
    }


}
