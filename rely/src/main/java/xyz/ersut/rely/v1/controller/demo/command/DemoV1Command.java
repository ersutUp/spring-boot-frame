package xyz.ersut.rely.v1.controller.demo.command;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 测试controller提交数据
 * @author 王二飞
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DemoV1Command {
    @NotNull(message = "不能为空")
    private Integer demo;

    @Length(min = 5)
    private String txt;
}
