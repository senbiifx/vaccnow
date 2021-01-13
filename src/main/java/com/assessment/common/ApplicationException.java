package com.assessment.common;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException{
    private ErrorCode errorCode;

    public ApplicationException(ErrorCode errorCode){
        super(errorCode.getErrorDesc());
        this.errorCode = errorCode;
    }

}
