package com.yubi.demo.config;

import com.google.protobuf.util.Durations;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.temporal.api.workflowservice.v1.RegisterNamespaceRequest;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.SimpleSslContextBuilder;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.WorkerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLException;
import java.time.Duration;

@Configuration
public class TemporalConfig {

    @Value("${temporal.address}")
    private String address;

    @Value("${temporal.namespace}")
    private String namespace;

    @Value("${temporal.sslEnabled}")
    private Boolean sslEnabled;

    private static final Logger log = LogManager.getLogger(TemporalConfig.class.getName());

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() throws SSLException {
        var builderOptions = WorkflowServiceStubsOptions.newBuilder()
                .setTarget(address)
                .setRpcTimeout(Duration.ofSeconds(1))
                .setEnableKeepAlive(true)
                .setKeepAliveTime(Duration.ofMinutes(5))
                .setKeepAliveTimeout(Duration.ofMinutes(5));

        if(sslEnabled){
            SslContext sslContext = SimpleSslContextBuilder
                    .noKeyOrCertChain()
                    .setUseInsecureTrustManager(true)
                    .build();
            builderOptions.setSslContext(sslContext);
        }
        WorkflowServiceStubs serviceStubs = WorkflowServiceStubs.newServiceStubs(builderOptions.build());

        RegisterNamespaceRequest req = RegisterNamespaceRequest.newBuilder()
                .setNamespace(namespace)
                .setWorkflowExecutionRetentionPeriod(Durations.fromDays(30))

                .build();
        try {
            serviceStubs.blockingStub().registerNamespace(req);
        } catch (Exception e) {
            log.error("Error registering namespace", e);
        }
        return serviceStubs;
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs workflowServiceStubs) {
        return WorkflowClient.newInstance(workflowServiceStubs,
                WorkflowClientOptions.newBuilder().setNamespace(namespace).build());
    }

    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        return WorkerFactory.newInstance(workflowClient);
    }
}