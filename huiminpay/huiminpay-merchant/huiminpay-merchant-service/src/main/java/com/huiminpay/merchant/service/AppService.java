package com.huiminpay.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.huiminpay.common.cache.domain.CommonErrorCode;
import com.huiminpay.common.cache.exception.BusinessCast;
import com.huiminpay.merchant.api.AppServiceApi;
import com.huiminpay.merchant.convert.AppConvert;
import com.huiminpay.merchant.dto.AppDTO;
import com.huiminpay.merchant.entity.App;
import com.huiminpay.merchant.entity.Merchant;
import com.huiminpay.merchant.mapper.AppMapper;
import com.huiminpay.merchant.mapper.MerchantMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class AppService implements AppServiceApi {

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private AppMapper appMapper;

    @Override
    public AppDTO createApp(Long merchantId, AppDTO appDTO) {
        //必要的参数校验
        if (appDTO == null || merchantId == null || merchantId == 0) {
            BusinessCast.cast(CommonErrorCode.E_100101);
        }

        //校验商户信息
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            BusinessCast.cast(CommonErrorCode.E_200002);
        }
        //审核状态  0 未申请 1以申请待审核 2 审核通过 3审核拒绝
        if (!"2".equals(merchant.getAuditStatus())) {
            BusinessCast.cast(CommonErrorCode.E_200003);
        }


        //校验应用名是否存在
        boolean flag=false;
        Integer count = appMapper.selectCount(new LambdaUpdateWrapper<App>().eq(App::getAppName, appDTO.getAppName()).eq(App::getMerchantId, merchantId));
        if (count > 0) {
            flag=true;
        }

        if (flag) {
            BusinessCast.cast(CommonErrorCode.E_200004);
        }

        //添加应用
        appDTO.setMerchantId(merchantId);
        App app = AppConvert.INSTANCE.dto2entity(appDTO);
        String uuid = UUID.randomUUID().toString();
        app.setAppId(uuid);
        appMapper.insert(app);
        return AppConvert.INSTANCE.entity2dto(app);
    }

    @Override
    public List<AppDTO> findAppsByMerchantId(Long merchantId) {
        if (merchantId == 0 || merchantId == null) {
            BusinessCast.cast(CommonErrorCode.E_110006);
        }
        List<App> apps = appMapper.selectList(new QueryWrapper<App>().lambda().eq(App::getMerchantId, merchantId));
        List<AppDTO> appDTOS = AppConvert.INSTANCE.listentity2listdto(apps);
        return appDTOS;
    }

    @Override
    public AppDTO findAppByAppId(String appId) {
        //必要校验
        if (appId.isEmpty()) {
            BusinessCast.cast(CommonErrorCode.E_110006);
        }
        App app = appMapper.selectOne(new LambdaQueryWrapper<App>().eq(App::getAppId, appId));

        AppDTO appDTO = AppConvert.INSTANCE.entity2dto(app);

        return appDTO;
    }

}
