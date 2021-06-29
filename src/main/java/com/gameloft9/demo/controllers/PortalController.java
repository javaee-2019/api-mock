package com.gameloft9.demo.controllers;

import com.gameloft9.demo.beans.ApiBean;
import com.gameloft9.demo.mgrframework.beans.response.IResult;
import com.gameloft9.demo.mgrframework.beans.response.ResultBean;
import com.gameloft9.demo.request.ApiRegisterRequest;
import com.gameloft9.demo.request.ApiUnregisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * 入口
 * Created by gameloft9 on 2018/8/7.
 */
@Controller
@EnableAutoConfiguration
@Slf4j
@ComponentScan(basePackages = "com.gameloft9.demo")
public class PortalController {

    /**
     * 入口
     */
    @RequestMapping(value = {"", "/", "home"})
    public String index(HttpServletRequest request) {
        return "forward:/views/home.html";
    }

    public static void main(String[] args) {
        SpringApplication.run(PortalController.class, args);
    }
}
