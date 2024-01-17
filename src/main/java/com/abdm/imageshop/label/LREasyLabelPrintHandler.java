/*----------------------------------------------------------------------------------------------------------------------
 * LR Easy Label Print © 2024 by l Simonnet is licensed under Attribution-NonCommercial-NoDerivatives 4.0 International
 *
 * LR Easy Label Print use a deployed service to print labels.
 * See http://localhost:8087/getting_started.html or http://auborddesmauves.fr/products/lrplugins for documentation.
 * ------------------------------------------------------------------------------------------------------------------ */
package com.abdm.imageshop.label;

// Imports
import static java.nio.charset.StandardCharsets.UTF_8;

import com.abdm.imageshop.label.configuration.WorkerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.util.Map;


/**
 * Simple handler.
 */
@Slf4j
@Component
@EnableScheduling
@ExternalTaskSubscription("lr-easy-label-print-worker")
public class LREasyLabelPrintHandler implements ExternalTaskHandler {

    /**
     * SOM-WORKFLOW configuration.
     */
    @Autowired
    private WorkerConfiguration config;

    @Autowired
    private ResourceLoader resourceLoader;



    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        Map<String, Object> variables = externalTask.getAllVariables();
        Long timeout =  config.getRetry().getTimeoutInMs();

        variables.forEach((key, value) -> log.debug("Variable {} ==> {}", key, value));
        String topicName = externalTask.getTopicName();
        long start = System.currentTimeMillis();


        try {
            log.info("Starting task {} execution for topic {}", externalTask.getActivityId(), externalTask.getTopicName());

            //TODO Insert here your code



            externalTaskService.complete(externalTask);


        } catch (Exception ex) {

            log.warn("Error while executing task {} for topic {}: {}",
                externalTask.getActivityId(),
                externalTask.getTopicName(),
                ex.getMessage());

            Integer retries = this.getRetries(externalTask);
            externalTaskService.handleFailure(
                externalTask,
                ex.getMessage(),
                getStackTrace(ex),
                retries,
                config.getRetry().getTimeoutInMs());

        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("Ending task {} execution for topic {}",
                externalTask.getActivityId(),
                externalTask.getTopicName());
        }


    }


    private Integer getRetries(ExternalTask task) {
        Integer retries = task.getRetries();
        if (retries == null) {
            retries = config.getRetry().getNbMax();
        } else {
            retries = retries - 1;
        }
        return retries;
    }


    private String getStackTrace(Exception e) {
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
