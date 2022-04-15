package com.sample.mall.coupon.controller;

import com.sample.mall.common.base.BaseResponse;
import com.sample.mall.common.dto.CouponDTO;
import com.sample.mall.common.dto.CouponRecordDTO;
import com.sample.mall.coupon.service.ICouponRecordService;
import com.sample.mall.coupon.service.ICouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class
CouponController {

    @Resource
    ICouponService couponService;

    @Resource
    ICouponRecordService couponRecordService;

    /**
     * 创建优惠券
     *
     * @param couponDTO
     * @return
     */
    @PostMapping("/coupon")
    BaseResponse createCoupon(@RequestBody CouponDTO couponDTO){
        couponService.createCoupon(couponDTO);
        return BaseResponse.success();
    }

    /**
     * 领取优惠券
     *
     * @param couponRecordDTO
     * @return
     */
    @PostMapping("/couponRecord")
    BaseResponse receiveCoupon(@RequestBody CouponRecordDTO couponRecordDTO){
        couponRecordService.receiveCoupon(couponRecordDTO);
        return BaseResponse.success();
    }

    /**
     * 更新优惠券状态
     *
     * @param couponRecordDTO
     * @return
     */
    @PutMapping("/couponRecord")
    BaseResponse useCoupon(@RequestBody CouponRecordDTO couponRecordDTO){
        couponRecordService.useCoupon(couponRecordDTO);
        return BaseResponse.success();
    }

}
