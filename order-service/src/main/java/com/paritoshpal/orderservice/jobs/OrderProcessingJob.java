package com.paritoshpal.orderservice.jobs;

import com.paritoshpal.orderservice.domain.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProcessingJob {

    private final OrderService orderService;

   private static final Logger log = LoggerFactory.getLogger(OrderProcessingJob.class);

   @Scheduled(cron = "${orders.new-orders-job-cron}")
   public void processNewOrders(){
         log.info("Order processing job started");
         orderService.processNewOrders();

         log.info("Order processing job completed");
   }
}
