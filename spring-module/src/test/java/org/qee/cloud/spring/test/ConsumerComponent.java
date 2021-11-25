package org.qee.cloud.spring.test;

 import org.qee.cloud.rpc.api.annotation.CloudReference;
 import org.springframework.stereotype.Component;

@Component
public class ConsumerComponent {

    @CloudReference(check = false)
    private OrderService orderService;

    public void print() {
        orderService.findById("123");
    }

}
