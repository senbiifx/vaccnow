package com.assessment.common;

import lombok.Getter;

@Getter
public class PreconditionException extends RuntimeException{
    private ErrorCode errorCode;
    public PreconditionException(ErrorCode errorCode){
        super(errorCode.getErrorDesc());
        this.errorCode = errorCode;
    }

}
