package com.gameloft9.demo.controllers;

import com.gameloft9.demo.beans.ApiBean;
import com.gameloft9.demo.mgrframework.beans.response.IResult;
import com.gameloft9.demo.mgrframework.beans.response.ResultBean;
import com.gameloft9.demo.request.ApiRegisterRequest;
import com.gameloft9.demo.request.ApiUnregisterRequest;
import com.gameloft9.demo.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/my")
public class RegisterController {

    @Value("${api.path:}")
    private String apiConfigPath;

    @RequestMapping(value = "/registerApi.do", method = RequestMethod.POST)
    public IResult registerApi(@Valid ApiRegisterRequest request, BindingResult result) {
        String apiKey = request.getRequestMethod() + DynamicApiController.SEPARATOR + request.getApi();
        registerApi(apiKey,request.getMsg());
        return new ResultBean(ApiBean.builder().api(request.getApi()).requestMethod(request.getRequestMethod()).msg(request.getMsg()).build());
    }

    /**
     * 注销api
     */
    @RequestMapping(value = "/unregisterApi.do", method = RequestMethod.POST)
    public IResult unregisterApi(@Valid ApiUnregisterRequest request, BindingResult result) {
        String apiKey = request.getRequestMethod() + DynamicApiController.SEPARATOR + request.getApi();
        unRegisterApi(apiKey);
        return new ResultBean(Boolean.TRUE);
    }

    @RequestMapping("/list")
    public Object list() throws IOException {
        StringBuffer sb = new StringBuffer();
        FileUtil.readTxtLineList(new File(apiConfigPath)).forEach(m -> {
            sb.append(m).append( "</br>");
        });
        return new ResultBean<>(sb.toString());
    }


    private synchronized void registerApi(String key, String val) {
        DynamicApiController.apiMap.put(key, val);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(apiConfigPath), true));) {
            bw.newLine();
            bw.write(key + "=" + val);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private synchronized void unRegisterApi(String key) {
        try {
            DynamicApiController.apiMap.remove(key);
            File file = new File(apiConfigPath);
            List<String> lineList = FileUtil.readTxtLineList(file);
            Iterator<String> iterator = lineList.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                if (Objects.equals(key, next.split("=")[0])) {
                    iterator.remove();
                }
            }
            FileUtil.writeTxtLineList(lineList, file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
