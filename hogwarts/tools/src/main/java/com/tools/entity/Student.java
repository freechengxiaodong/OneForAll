package com.tools.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;

@AllArgsConstructor //全参构造器
@NoArgsConstructor //无参构造器
@Data //lombok包中的 相当于getter + setter
public class Student {

    ///*分组ID groups = {AddGroup.class},*/
    @NotBlank(/*分组ID groups = {AddGroup.class},*/ message = "用户名不能为空")
    private String name;
    @Min(value = 18, message = "年龄不能小于18岁")
    private Integer age;
    @Pattern(regexp = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$", message = "手机号格式错误")
    private String phone;
    @Email(message = "邮箱格式错误")
    private String email;
    //校验类字段需要加  @Valid
    @Valid
    @NotNull
    private School school;

    @Data
    private static class School {
        @NotBlank(message = "学校名不能为空")
        private String name;
        @NotBlank(message = "学校地址不能为空")
        private String address;
    }
}
