package com.yubi.demo.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yubi.demo.interceptor.LoggingInterceptor;
import com.yubi.demo.domain.CoreHealthStatus;




import io.grpc.health.v1.HealthCheckResponse;
import io.temporal.serviceclient.WorkflowServiceStubs;



@RestController
@RequestMapping("/health")
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    
    
    
    @Autowired
    private WorkflowServiceStubs workflowServiceStubs;

    @Value("${temporal.address}")
    private String temporalUrl;
    

    @GetMapping
    public ResponseEntity<Map<String, Object>> overallCheck(){
        Map<String, Object> respMap = new HashMap<>();
        var statuses = new ArrayList();

        var allCheckUp = new AtomicBoolean(true);
        var methods = Arrays.stream(this.getClass().getDeclaredMethods()).filter(e -> e.getName().startsWith("check"));
        //Invoke the methods surrounded by try-catch
        //Stream the responses
        //If you find a false scenario, allCheckUp.set(false);
        var me = this;
        methods.forEach(e -> {
            try{
                var resp = (ResponseEntity<CoreHealthStatus>) e.invoke(me);

                if(!resp.getBody().isAlive()){
                    allCheckUp.set(false);
                }
                statuses.add(resp.getBody());
            }catch (Exception ex){
                //Log the error here
                allCheckUp.set(false);
            }
        });
        respMap.put("status", allCheckUp.get() ? "UP" : "DOWN" );
        respMap.put("services", statuses);

        return allCheckUp.get() ? ResponseEntity.ok(respMap) : ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(respMap);
    }

    @GetMapping(value = {"/test"})
    public ResponseEntity<String> test() {
        logger.info("Test");
        return ResponseEntity.ok("Response with status OK");
    }

    
    
    
    @GetMapping("/temporal")
    public ResponseEntity<CoreHealthStatus> checkTemporalHealth() {
        var status = new CoreHealthStatus("temporal");
        status.setAlive(false);
        status.setMessage("Temporal Connection is not healthy");
        status.setUrl(temporalUrl);
        try {
//            workerFactory.isStarted();
            HealthCheckResponse healthCheck = workflowServiceStubs.healthCheck();
            status.setAlive(true);
            status.setMessage("Temporal Connection is healthy");
            return ResponseEntity.ok(status);
        } catch (Exception e){
            status.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(status);
        }
    }
    
}
