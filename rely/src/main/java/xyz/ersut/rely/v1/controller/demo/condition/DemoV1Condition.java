package xyz.ersut.rely.v1.controller.demo.condition;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 测试controller查询数据
 * @author 王二飞
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DemoV1Condition {
    @NotNull(message = "空了！！！")
    private Integer demo;

    @Length(min = 5)
    private String txt;
}
