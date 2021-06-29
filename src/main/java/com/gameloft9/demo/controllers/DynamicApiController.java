package com.gameloft9.demo.controllers;

import com.gameloft9.demo.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mock")
@Slf4j
public class DynamicApiController {

    @Value("${api.timeout:100000}")
    private long apiTimeout;

    @Value("${api.path:}")
    private String apiConfigPath;

    public static String SEPARATOR = "/";

    private AtomicLong lastTime = new AtomicLong(System.currentTimeMillis());

    public static Map<String, String> apiMap = new ConcurrentHashMap<String, String>();

    @PostConstruct
    public void init() {
        refreshApiMap();
    }

    @RequestMapping("{api1}")
    public String mock1(@PathVariable("api1") String api1) {
        return getApiReturnValue(getApiKey(api1));
    }

    @RequestMapping("{api1}/{api2}")
    public String mock2(@PathVariable("api1") String api1,
                        @PathVariable("api2") String api2) {
        return getApiReturnValue(getApiKey(api1, api2));
    }

    @RequestMapping("{api1}/{api2}/{api3}")
    public String mock3(@PathVariable("api1") String api1,
                        @PathVariable("api2") String api2,
                        @PathVariable("api3") String api3) {
        return getApiReturnValue(getApiKey(api1, api2, api3));
    }

    @RequestMapping("{api1}/{api2}/{api3}/{api4}")
    public String mock4(@PathVariable("api1") String api1,
                        @PathVariable("api2") String api2,
                        @PathVariable("api3") String api3,
                        @PathVariable("api4") String api4) {
        return getApiReturnValue(getApiKey(api1, api2, api3, api4));
    }

    @RequestMapping("{api1}/{api2}/{api3}/{api4}/{api5}")
    public String mock5(@PathVariable("api1") String api1,
                        @PathVariable("api2") String api2,
                        @PathVariable("api3") String api3,
                        @PathVariable("api4") String api4,
                        @PathVariable("api5") String api5) {
        return getApiReturnValue(getApiKey(api1, api2, api3, api4, api5));
    }

    @RequestMapping("{api1}/{api2}/{api3}/{api4}/{api5}/{api6}")
    public String mock6(@PathVariable("api1") String api1,
                        @PathVariable("api2") String api2,
                        @PathVariable("api3") String api3,
                        @PathVariable("api4") String api4,
                        @PathVariable("api5") String api5,
                        @PathVariable("api6") String api6) {
        return getApiReturnValue(getApiKey(api1, api2, api3, api4, api5, api6));
    }

    @RequestMapping("{api1}/{api2}/{api3}/{api4}/{api5}/{api6}/{api7}")
    public String mock7(@PathVariable("api1") String api1,
                        @PathVariable("api2") String api2,
                        @PathVariable("api3") String api3,
                        @PathVariable("api4") String api4,
                        @PathVariable("api5") String api5,
                        @PathVariable("api6") String api6,
                        @PathVariable("api7") String api7) {
        return getApiReturnValue(getApiKey(api1, api2, api3, api4, api5, api6, api7));
    }

    @RequestMapping("{api1}/{api2}/{api3}/{api4}/{api5}/{api6}/{api7}/{api8}")
    public String mock8(@PathVariable("api1") String api1,
                        @PathVariable("api2") String api2,
                        @PathVariable("api3") String api3,
                        @PathVariable("api4") String api4,
                        @PathVariable("api5") String api5,
                        @PathVariable("api6") String api6,
                        @PathVariable("api7") String api7,
                        @PathVariable("api8") String api8) {
        return getApiReturnValue(getApiKey(api1, api2, api3, api4, api5, api6, api7,api8));
    }
    /**
     * 刷新api映射
     */
    private void refreshApiMap() {
        try {
            File file = new File(apiConfigPath);
            if (!file.exists()) {
                throw new IOException("文件不存在...");
            }
            List<String> list = FileUtil.readTxtLineList(file);
            Map<String, String> api = list.stream()
                    .filter(m -> StringUtils.isNotBlank(m) && m.split("=").length != 2 && StringUtils.isNotBlank(m.split("=")[0]) && StringUtils.isNotBlank(m.split("=")[1]))
                    .collect(Collectors.toMap(m -> m.split("=")[0], m -> m.split("=")[1], (oldKey, newKey) -> newKey));
            apiMap.putAll(api);
            lastTime.set(System.currentTimeMillis());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获得api返回值
     *
     * @param api api
     * @return {@link String}
     */
    public String getApiReturnValue(String api) {
        if (System.currentTimeMillis() - lastTime.get() > apiTimeout) {
            refreshApiMap();
        }
        String val = apiMap.get(api);
        if (StringUtils.isNotBlank(val)) {
            return val;
        }
        refreshApiMap();
        return apiMap.get(api);
    }

    /**
     * 获得api密匙
     *
     * @param api api
     * @return {@link String}
     */
    private String getApiKey(String... api) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < api.length; i++) {
            sb.append(api[i]).append(SEPARATOR);
        }
        return sb.substring(0, sb.length() - 1);
    }
}
