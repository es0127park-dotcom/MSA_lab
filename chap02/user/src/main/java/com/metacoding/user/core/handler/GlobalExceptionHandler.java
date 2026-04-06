package com.metacoding.user.core.handler;

import com.metacoding.user.core.handler.ex.*;
import com.metacoding.user.core.util.Resp;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<?> exApi400(Exception400 e) {
        return Resp.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<?> exApi401(Exception401 e) {
        return Resp.fail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<?> exApi403(Exception403 e) {
        return Resp.fail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(Exception404.class)
    public ResponseEntity<?> exApi404(Exception404 e) {
        return Resp.fail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(Exception500.class)
    public ResponseEntity<?> exApi500(Exception500 e) {
        return Resp.fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

}
