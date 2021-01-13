package com.assessment.config;

import com.assessment.common.ErrorCode;
import com.assessment.common.PreconditionException;
import com.assessment.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity defaultExceptionHandler(Exception ex) {
        log.error("Exception thrown", ex);
        Response response = Response.<String>builder()
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getErrorDesc())
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode())
                .build();

        return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
    }

    @ExceptionHandler(PreconditionException.class)
    protected ResponseEntity preconditionExceptionHandler(PreconditionException ex) {
        Response response = Response.<String>builder()
                .message(ex.getErrorCode().getErrorDesc())
                .errorCode(ex.getErrorCode().getErrorCode())
                .build();

        return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
    }

}
