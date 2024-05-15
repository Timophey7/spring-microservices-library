package com.cartservice.events;

import com.cartservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventListener {

    private final EmailService emailService;

    @EventListener
    public void orderCreateEvent(OrderCreateEvent event) {
        log.info("OrderCreatedEventListener called");
        log.info("email:" + event.getEmail());
        emailService.sendEmail(event.getCartId(),event.getResultSum(),event.getEmail());
        log.info("Email sent");
    }

}
