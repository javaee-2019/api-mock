package com.gameloft9.demo.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 提交api申请bean
 * Created by gameloft9 on 2018/8/8.
 */
@Data
public class ApiRegisterRequest {

    @NotBlank
    private String api;

    @NotBlank
    private String requestMethod;

    private String msg;
}
