package com.assessment.vaccination.domain;

import com.assessment.common.ApplicationException;
import com.assessment.common.ErrorCode;
import com.assessment.config.AppProperties;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static com.itextpdf.text.pdf.BaseFont.EMBEDDED;

@Slf4j
@Component
@AllArgsConstructor
public class HtmlToPdfReportGenerator implements ReportGenerator{

    private TemplateEngine templateEngine;
    private AppProperties appProperties;

    @Override
    public byte[] createPdf(String template, Object data) {
        try{
            Context context = new Context();
            context.setVariable("data", data);
            String renderedHtmlContent = templateEngine.process(template, context);
            String xHtml = convertToXhtml(renderedHtmlContent);

            ITextRenderer renderer = new ITextRenderer();
            renderer.getFontResolver().addFont(appProperties.getScheduleReportFont(), EMBEDDED);
            renderer.setDocumentFromString(xHtml, null);
            renderer.layout();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        }catch (IOException | DocumentException e){
            log.error("Error creating pdf", e);
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String convertToXhtml(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding(StandardCharsets.UTF_8.name());
        tidy.setOutputEncoding(StandardCharsets.UTF_8.name());
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8.name()));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);

        return outputStream.toString(StandardCharsets.UTF_8.name());
    }
}
