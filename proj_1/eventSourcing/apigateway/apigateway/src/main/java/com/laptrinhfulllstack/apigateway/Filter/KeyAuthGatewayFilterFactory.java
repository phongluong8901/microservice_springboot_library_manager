package com.laptrinhfulllstack.apigateway.Filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class KeyAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<KeyAuthGatewayFilterFactory.Config> {
    @Value("${apiKey}")
    private String apiKey;

    public KeyAuthGatewayFilterFactory() {
        super(Config.class);
    }

    public static class Config {
        // Bạn có thể thêm các thuộc tính cấu hình ở đây nếu muốn truyền tham số từ file
        // yml
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Kiểm tra xem header có chứa 'apiKey' hay không
            if (!request.getHeaders().containsKey("apiKey")) {
                return onError(exchange, "Missing authorization information", HttpStatus.UNAUTHORIZED);
            }

            String key = request.getHeaders().getFirst("apiKey");

            // Kiểm tra apiKey có hợp lệ hay không
            if (!key.equals(apiKey)) {
                return onError(exchange, "Invalid API Key", HttpStatus.FORBIDDEN);
            }

            // Nếu hợp lệ, tiếp tục cho request đi tiếp
            return chain.filter(exchange);
        };
    }

    // Hàm hỗ trợ trả về lỗi HTTP chuẩn cho WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }
}