package com.assessment.vaccination.domain;

public interface ReportGenerator {
    byte[] createPdf(String template, Object data);
}
