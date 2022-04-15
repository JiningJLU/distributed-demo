package com.sample.mall.order.controller;

import com.sample.mall.common.base.BaseResponse;
import com.sample.mall.common.dto.OrderDTO;
import com.sample.mall.order.service.IOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单服务Controller
 */
@RestController
public class OrderController {

    @Resource
    private IOrderService orderService;

    /**
     * 创建订单接口
     *
     * @param orderDTO
     * @return
     */
    @PostMapping("/order")
    BaseResponse createOrder(@RequestBody OrderDTO orderDTO) {
        //TODO 对 DTO 进行前置校验
        orderService.createOrder(orderDTO);
        return BaseResponse.success();
    }

    /**
     * 获取订单
     *
     * @return
     */
    @GetMapping("/order/{orderNo}")
    BaseResponse<OrderDTO> getOrder(@PathVariable(value = "orderNo") Long orderNo){
        return null;
    }
}
