/*----------------------------------------------------------------------------------------------------------------------
 * LR Easy Label Print © 2024 by l Simonnet is licensed under Attribution-NonCommercial-NoDerivatives 4.0 International
 *
 * LR Easy Label Print use a deployed service to print labels.
 * See http://localhost:8087/getting_started.html or http://auborddesmauves.fr/products/lrplugins for documentation.
 * ------------------------------------------------------------------------------------------------------------------ */
package com.abdm.imageshop.label.label;

// Imports
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserController
 */
@RestController
@RequestMapping("/api/v1/label")
@Slf4j
@CrossOrigin(origins = "*")
public class LabelController implements ApplicationContextAware {

    public static String EXT = ".pdf";

    @Autowired
    private LabelService labelService;

    @Value("${spring.application.version}")
    String version;


    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    /**
     *
     * @return
     */
    @GetMapping("/version")
    public String getVersion() {
        log.debug("Returning app version : " + version);
        return version;
    }

    /**
     *
     */
    @PostMapping("/stop")
    public void stop() {
        log.debug("Stoping server");
        ((ConfigurableApplicationContext) context).close();
    }

    /**
     *
     * @param labelPage
     * @return
     */
    @PostMapping(value = "/print", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> Print(@RequestBody LabelPage labelPage) {

        log.debug(" Printing label page");

        if(labelPage.fileName==null){
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
            String fileName = date.format(formatter)+ EXT;
            labelPage.fileName = fileName;
        }

        log.debug(" Printing label page in file {}", labelPage.fileName);
        return labelService.printLabels(labelPage.labels, labelPage.fileName);
    }

}
