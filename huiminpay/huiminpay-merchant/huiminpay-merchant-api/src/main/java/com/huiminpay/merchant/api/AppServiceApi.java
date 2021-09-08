package com.huiminpay.merchant.api;

import com.huiminpay.merchant.dto.AppDTO;

import java.util.List;

public interface AppServiceApi {
    //添加应用
    public AppDTO createApp(Long merchantId, AppDTO appDTO);

    public List<AppDTO> findAppsByMerchantId(Long merchantId);

    public AppDTO findAppByAppId(String appId);
}
