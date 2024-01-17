/*----------------------------------------------------------------------------------------------------------------------
 * LR Easy Label Print © 2024 by l Simonnet is licensed under Attribution-NonCommercial-NoDerivatives 4.0 International
 *
 * LR Easy Label Print use a deployed service to print labels.
 * See http://localhost:8087/getting_started.html or http://auborddesmauves.fr/products/lrplugins for documentation.
 * ------------------------------------------------------------------------------------------------------------------ */
package com.abdm.imageshop.label;

// Imports

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.net.*;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.Map;


/**
 * Simple handler.
 */
@Slf4j
@Component
@EnableScheduling
@ExternalTaskSubscription("message-worker")
public class MessageHandler extends AbstractTaskHandler implements ExternalTaskHandler {

    @Value("${service.proxyHost:unknown}")
    String proxyHost;

    @Value("${service.proxyPort:0}")
    String proxyPort;

    @Value("#{new Boolean('${service.useProxy:false}')}")
    Boolean useProxy;

    @Value("${service.url}")
    String serviceURL;

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

            HttpClient.Builder builder = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                            "baeldung",
                            "123456".toCharArray());
                    }
                });
            if (useProxy) {
                builder.proxy(ProxySelector.of(new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort))));
            }
            HttpClient client =  builder.build();
//
//            HttpRequest request = HttpRequest.newBuilder()
//                .uri(new URI(serviceURL )).POST(ofString(variables)).build();

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



//    public HttpRequest.BodyPublisher buildBody(
//        String messageName,
//        Map<String, Object> variables) {
//
//        JSONObject json = new JSONObject();
//        json.put("messageName", messageName);
////        json.put("processVariables"
//        for (String dataKey : variables.keySet()) {
//
//            // String / Boolean /Short / Integer / Long /Double / Date / Object /File
//            json.put(dataKey, data.get(dataKey));
//        }
////        String body = json.toString();
//
//        return HttpRequest.BodyPublishers.ofString("");
//    }

    private String encode(Object obj) {
        return URLEncoder.encode(obj.toString(), StandardCharsets.UTF_8);
    }
}
