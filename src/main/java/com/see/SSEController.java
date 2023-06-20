package com.see;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

@RestController
public class SSEController {

    @GetMapping("/token")
    public void token(ServerHttpResponse response) {
        final String randomToken = UUID.randomUUID().toString();
        response.addCookie(ResponseCookie.from("token").value(randomToken).maxAge(1000000).build());
    }

    @GetMapping("/stream-sse")
    public Flux<ServerSentEvent<String>> streamEvents(ServerHttpRequest request) {
        final String token = readServletCookie(request, "token");
        System.err.println("Connected with token: " + token);
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<String> builder()
                        .id(String.valueOf(sequence))
                        .event("msg")
                        .data("SSE - " + LocalTime.now().toString())
                        .build());
    }

    public String readServletCookie(ServerHttpRequest request, String name){
        HttpCookie httpCookie = request.getCookies().getFirst(name);
        return httpCookie != null ? httpCookie.getValue() : null;
    }

}
