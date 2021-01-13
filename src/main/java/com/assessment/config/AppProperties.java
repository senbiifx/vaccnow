package com.assessment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String emailSubject;
    private String emailContent;
    private String scheduleReportTemplate;
    private String scheduleReportFont;
}
