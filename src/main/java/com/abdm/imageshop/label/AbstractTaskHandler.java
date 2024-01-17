/*----------------------------------------------------------------------------------------------------------------------
 * LR Easy Label Print © 2024 by l Simonnet is licensed under Attribution-NonCommercial-NoDerivatives 4.0 International
 *
 * LR Easy Label Print use a deployed service to print labels.
 * See http://localhost:8087/getting_started.html or http://auborddesmauves.fr/products/lrplugins for documentation.
 * ------------------------------------------------------------------------------------------------------------------ */
package com.abdm.imageshop.label;

// Imports

import com.abdm.imageshop.label.configuration.WorkerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.task.ExternalTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * Simple handler.
 */
@Slf4j
public abstract class AbstractTaskHandler {

    /**
     * Configuration.
     */
    @Autowired
    protected WorkerConfiguration config;



    protected Integer getRetries(ExternalTask task) {
        Integer retries = task.getRetries();
        if (retries == null) {
            retries = config.getRetry().getNbMax();
        } else {
            retries = retries - 1;
        }
        return retries;
    }


    protected String getStackTrace(Exception e) {
        StringWriter stackTraceWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTraceWriter));
        return stackTraceWriter.toString();
    }



    public String asString(Resource resource) {

        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
