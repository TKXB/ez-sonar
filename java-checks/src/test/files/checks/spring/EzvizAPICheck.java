package com.ys.product.common.annotation;
import com.ys.product.common.annotation.RequestHeaderEntity;
import com.ys.product.common.annotation.UserDeviceAccessControl;
import com.ys.product.common.model.request.CommonParameters;
import com.ys.product.common.model.response.IResponseResult;
import com.ys.product.common.model.response.swagger.SwaggerBaseResponse;
import com.ys.share.biz.common.helper.ResultHelper;
import com.ys.share.common.model.qo.ModifyUserQo;
import com.ys.share.common.model.qo.ShareUserQo;
import com.ys.share.biz.common.request.WeekPlansRequest;
import com.ys.share.biz.common.response.ShareInfoResponse;
import com.ys.share.common.model.qo.ApplyShareQo;
import com.ys.share.biz.common.service.share.ShareOperateService;
import com.ys.share.biz.common.service.share.ShareQueryService;
import com.ys.share.biz.common.utils.PermissionUtils;
import com.ys.share.common.emuns.ShareModeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sonar.java.checks.targets.CustomAnnotation;

@interface UserDeviceAccessControl {
    String myName();
    int myInteger() default 0;
    String aaaLast();
}

@Controller
public class HelloWorld {

    @RequestMapping(value = "/helloBitch", method = RequestMethod.GET)
    public String hello(String greetee) { // Compliant
        String responseResult = shareOperateService.applyShare(commonParameters.getZuulUserId(), greetee);
        return responseResult;
    }


    @GetMapping(value = "/deviceinfo", method= GET)
    @CustomAnnotation(field1="", field2="", field3="")
    @UserDeviceAccessControl
    public String getDeviceInfo(String deviceserial) {  // Noncompliant [[sc=19;ec=32]] {{请检查是否有权限校验}}
        String responseResult = shareOperateService.applyShare(deviceserial);
        return responseResult;
    }


}