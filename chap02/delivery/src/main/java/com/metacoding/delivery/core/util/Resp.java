package com.metacoding.delivery.core.util;

import lombok.Data;
import org.springframework.http.*;

@Data
public class Resp<T> {
    private Integer status;
    private String msg;
    private T body;

    public Resp(Integer status, String msg, T body) {
        this.status = status;
        this.msg = msg;
        this.body = body;
    }

    public static <B> ResponseEntity<Resp<B>> ok(B body) {
        Resp<B> resp = new Resp<>(200, "성공", body);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    public static ResponseEntity<?> fail(HttpStatus status, String msg) {
        Resp<?> resp = new Resp<>(status.value(), msg, null);
        return new ResponseEntity<>(resp, status);
    }
}