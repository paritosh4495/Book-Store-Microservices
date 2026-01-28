package com.paritoshpal.orderservice.jobs;

import com.paritoshpal.orderservice.domain.OrderEventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class OrderEventsPublishingJob {

    private static final Logger log = LoggerFactory.getLogger(OrderEventsPublishingJob.class);

    private final OrderEventService orderEventService;

    @Scheduled(cron = "${orders.publish-order-events-job-cron}")
    public void publishOrderEvents(){
        log.info("Publishing order events job started at {}", Instant.now());
        // Implementation for publishing order events goes here
        orderEventService.publishOrderEvents();
        log.info("Publishing order events job completed");
    }
}
