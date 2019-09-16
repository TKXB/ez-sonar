package com.ys.product.device.config.front.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import javax.annotation.Resource;
import javax.validation.Valid;
import com.hikvision.ezviz.device.mix.dto.TimingPlanDto;
import com.ys.product.common.annotation.RequestHeaderEntity;
import com.ys.product.common.model.request.CommonParameters;
import com.ys.product.device.config.front.model.request.DeviceBizDataRequest;
import com.ys.product.device.config.front.model.request.SwitchDeviceEnableRequest;
import com.ys.product.device.config.front.model.request.SwitchStatusRequest;
import com.ys.product.device.config.front.model.request.TimerDefenceRequestInfo;
import com.ys.product.device.config.front.service.v3.DeviceConfigService;
import com.ys.product.device.config.front.util.BizDataUtil;
import com.ys.product.device.config.front.util.CommonResultUtil;
import com.ys.product.device.config.front.util.TimingPlanDtoRequestInfoConverter;
import com.ys.product.common.annotation.UserDeviceAccessControl;
import com.ys.product.common.model.response.IResponseResult;
import com.ys.product.common.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;


@RestController
@RequestMapping(value = "/v3/devices", produces = {
        APPLICATION_JSON_VALUE})
public class DevicesController {

    @Resource
    private DeviceConfigService deviceConfigService;

    /**
     *
     * 开启(关闭)设备下线通知
     *

     *
     * @param deviceSerial 设备短序列号
     * @param status       0-关闭,1-开启
     * @return
     * @author steven 2016年8月31日 下午6:05:36
     */
    @ApiOperation(value = "开启(关闭)设备下线通知 0-关闭,1-开启")
    @RequestMapping(value = "/offlineNotify/switch", method = RequestMethod.PUT)
    public ResponseEntity<IResponseResult> switchOfflineNotify(@RequestHeaderEntity CommonParameters commonParameters, // Noncompliant
                                                               String deviceSerial,
                                                               @RequestParam(name = "status", required = true, defaultValue = "1") int status) {
        IResponseResult responseResult = deviceConfigService.updateOfflineNotify(deviceSerial, status);
        return new ResponseEntity<IResponseResult>(responseResult, responseResult.httpStatus());
    }

    /**
     *
     * 修改设备计划
     *

     *
     * @return
     * @author steven 2016年8月31日 下午6:10:36
     * 手机短信验证码,设置设备上下线时需要
     * 1:设备上下线,2:设备布撤防，默认是1
     */
    @ApiOperation(value = "修改设备计划 1:设备上下线,2:设备布撤防，默认是1")
    @RequestMapping(value = "/planInfo", method = RequestMethod.PUT)
    public ResponseEntity<IResponseResult> updateDevicePlan(@RequestHeaderEntity CommonParameters commonParameters,
                                                            @ModelAttribute @Valid TimerDefenceRequestInfo timerDefenceRequestInfo) {
        TimingPlanDto timingPlanDto = TimingPlanDtoRequestInfoConverter.valueOf(timerDefenceRequestInfo);
        IResponseResult responseResult = new ResponseResult();
        if (timerDefenceRequestInfo.getPlanType() == 1) {
            responseResult = deviceConfigService.updateDeviceAutoOnline(timingPlanDto);
        } else {
            responseResult = deviceConfigService.modifyTimerDefence(timingPlanDto);
        }
        return new ResponseEntity<IResponseResult>(responseResult, responseResult.httpStatus());
    }


    /**
     *
     * 设置设备业务数据
     *

     *
     * @param deviceBizDataRequest
     * @return
     * @author steven 2017年1月13日 上午11:22:34
     */
    @ApiOperation(value = "设置设备业务数据")
    @PutMapping(value = "/bizData")
    @UserDeviceAccessControl
    public ResponseEntity<IResponseResult> setDeviceBizData(@RequestHeaderEntity CommonParameters commonParameters,
                                                            @ModelAttribute @Valid DeviceBizDataRequest deviceBizDataRequest) {
        if (!BizDataUtil.checkBizData(deviceBizDataRequest.getBizKey(), deviceBizDataRequest.getBizValue())) {
            return new ResponseEntity<IResponseResult>(CommonResultUtil.return400InvalidParameter(), HttpStatus.OK);
        }
        IResponseResult responseResult = deviceConfigService.updateDeviceExtStatus(deviceBizDataRequest.getDeviceSerial(), deviceBizDataRequest.getBizKey(), deviceBizDataRequest.getBizValue());
        return new ResponseEntity<IResponseResult>(responseResult, responseResult.httpStatus());
    }

    /**
     *
     * 根据设备短序列号、开关类型查询开关状态
     *

     *
     * @param deviceSerial 设备序列号
     *                     请求参数
     * @return
     * @author dengzeyu 2016年9月18日 上午9:00:28
     */
    @ApiOperation(value = "根据设备短序列号、开关类型查询开关状态")
    @RequestMapping(value = "/{deviceSerial}/switch/status", method = RequestMethod.GET)
    public ResponseEntity<IResponseResult> getDeviceSwitchStatus(@RequestHeaderEntity CommonParameters commonParameters,  // Noncompliant
                                                                 @PathVariable("deviceSerial") String deviceSerial,
                                                                 Integer type) {
        IResponseResult responseResult = deviceConfigService.getDeviceSwitchStatus(deviceSerial, type);
        return new ResponseEntity<IResponseResult>(responseResult, responseResult.httpStatus());
    }


    /**
     *
     * 平台向设备设置各种开关量
     *

     *
     * @param deviceSerial 设备序列号
     * @param request      请求参数
     * @return
     * @author dengzeyu 2016年9月18日 上午9:00:28
     */
    @ApiOperation(value = "平台向设备设置各种开关量")
    @RequestMapping(value = "/{deviceSerial}/switch", method = RequestMethod.PUT)
    @UserDeviceAccessControl
    public DeferredResult<ResponseEntity<IResponseResult>> switchDeviceEnable(@RequestHeaderEntity CommonParameters commonParameters,
                                                                              @PathVariable("deviceSerial") String deviceSerial,
                                                                              @ModelAttribute @Valid SwitchDeviceEnableRequest request) {
        SwitchStatusRequest switchStatusRequest = new SwitchStatusRequest();
        switchStatusRequest.setChannelNo(request.getChannelNo());
        switchStatusRequest.setDeviceSerial(deviceSerial);
        switchStatusRequest.setEnable(request.getEnable());
        switchStatusRequest.setType(request.getSwitchType());
        return deviceConfigService.switchDeviceStatus(switchStatusRequest);
    }
}