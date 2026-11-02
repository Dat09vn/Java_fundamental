package com.example.order_service;

// import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.order_service.ServiceRegistry.ServiceInstance;


// @Component
@Service
public class PaymentClient {

    private final RestTemplate restTemplate;
    private final RoundRobinLoadBalancer loadBalancer;


    public PaymentClient(RestTemplate restTemplate, RoundRobinLoadBalancer loadBalancer) {
        this.restTemplate = restTemplate;
        this.loadBalancer = loadBalancer;
    }

    public PaymentResponse callPayment() {
        ServiceInstance instance =
                loadBalancer.choose();

        String url =
                "http://" +
                instance.host() +
                ":" +
                instance.port() +
                "/api/payment";

        return restTemplate.getForObject(url, PaymentResponse.class);
    }
}


// package com.example.order_service;

// import org.springframework.stereotype.Component;
// import org.springframework.web.client.RestClient;

// @Component
// public class PaymentClient {

//     private final RestClient restClient;

//     public PaymentClient(RestClient restClient) {
//         this.restClient = restClient;
//     }

//     public PaymentResponse callPayment() {

//         return restClient
//                 .get()
//                 .uri("/api/payment")
//                 .retrieve()
//                 .body(PaymentResponse.class);
//     }
// }




// package com.example.order_service;

// import org.springframework.web.client.RestClient;
// import org.springframework.stereotype.Service;
// import com.example.order_service.ServiceRegistry.ServiceInstance;

// @Service
// public class PaymentClient {

//     private final RestClient restClient;
//     private final RoundRobinLoadBalancer loadBalancer;

//     public PaymentClient(
//             RestClient restClient,
//             RoundRobinLoadBalancer loadBalancer) {

//         this.restClient = restClient;
//         this.loadBalancer = loadBalancer;
//     }

//     public PaymentResponse callPayment() {

//         ServiceInstance instance =
//                 loadBalancer.choose();

//         String url =
//                 "http://" +
//                 instance.host() +
//                 ":" +
//                 instance.port() +
//                 "/api/payment";

//         return restClient
//                 .get()
//                 .uri(url)
//                 .retrieve()
//                 .body(PaymentResponse.class);
//     }
// }