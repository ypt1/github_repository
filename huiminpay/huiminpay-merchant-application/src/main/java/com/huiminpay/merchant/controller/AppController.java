package com.huiminpay.merchant.controller;

import com.huiminpay.common.cache.util.SecurityUtil;
import com.huiminpay.merchant.api.AppServiceApi;
import com.huiminpay.merchant.dto.AppDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "应用模块管理")
public class AppController {

    @Reference
    private AppServiceApi appServiceApi;
    
    @ApiOperation("添加应用")
    @PostMapping("/my/apps")
    //paraType:path query body
    @ApiImplicitParam(name = "appDTO",value = "应用信息",dataType = "AppDTO",paramType = "body",required = true)
    public AppDTO createApp(@RequestBody AppDTO appDTO) {
        Long merchantId = SecurityUtil.getMerchantId();
        AppDTO dto = appServiceApi.createApp(merchantId, appDTO);
        return dto;
    }

    @ApiOperation("根据商户id获取应用列表信息")
    @GetMapping("/my/apps")
    public List<AppDTO> findApps() {
        Long merchantId = SecurityUtil.getMerchantId();
        List<AppDTO> list = appServiceApi.findAppsByMerchantId(merchantId);
        return list;
    }

    @ApiOperation("根据应用id获取应用信息")
    @GetMapping("/my/apps/{appId}")
    @ApiImplicitParam(name = "appId",value = "应用Id",dataType = "String",paramType = "path",required = true)
    public AppDTO findAppByAppId(@PathVariable("appId") String appId) {
        AppDTO appDTO = appServiceApi.findAppByAppId(appId);
        return appDTO;
    }
}
