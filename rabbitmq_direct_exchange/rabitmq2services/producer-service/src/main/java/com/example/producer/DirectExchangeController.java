package com.example.producer;

// import com.example.producer.DirectMessageProducer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/direct")
public class DirectExchangeController {

    private final DirectMessageProducer producer;

    public DirectExchangeController(
            DirectMessageProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/created")
    public String orderCreated(
            @RequestParam String message) {

        producer.sendOrderCreated(message);

        return "Published order.created";
    }

    @PostMapping("/cancelled")
    public String orderCancelled(
            @RequestParam String message) {

        producer.sendOrderCancelled(message);

        return "Published order.cancelled";
    }
}