package xyz.ersut.rely.v1.controller.demo.result;

import lombok.*;

/**
 * 测试controller响应数据
 * @author 王二飞
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DemoV1Result {
    private Integer demo;
}
