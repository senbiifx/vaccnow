package com.assessment.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("VACC-ERR-001", "Something went wrong"),
    INVALID_BRANCH_CODE("VACC-ERR-002", "Invalid Branch Code"),
    INVALID_VACCINE_CODE("VACC-ERR-003", "Invalid Vaccine Code"),
    INVALID_PAYMENT_METHOD("VACC-ERR-004", "Invalid Payment Method"),
    INVALID_TIMESLOT_ID("VACC-ERR-005", "Invalid Timeslot Id"),
    TIMESLOT_UNAVAILABLE("VACC-ERR-006", "Timeslot Unavailable"),
    INVALID_SCHEDULE("VACC-ERR-007", "Invalid Schedule"),
    ;


    private String errorCode;
    private String errorDesc;
}
