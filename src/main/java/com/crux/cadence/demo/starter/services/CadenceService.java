package com.crux.cadence.demo.starter.services;

import com.crux.cadence.demo.starter.entity.City;
import com.crux.cadence.demo.starter.excheptions.BusinessException;
import com.crux.cadence.demo.workflow.WeatherWorkflow;
import com.uber.cadence.RegisterDomainRequest;
import com.uber.cadence.WorkflowExecution;
import com.uber.cadence.WorkflowIdReusePolicy;
import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowOptions;
import com.uber.cadence.common.RetryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CadenceService {
    @NonNull
    private WorkflowClient workflowClient;
    @NonNull
    private String taskList;

    public CadenceService(@NonNull WorkflowClient workflowClient, @Value("${app.cadence.tasklist}") String taskList) {
        this.workflowClient = workflowClient;
        this.taskList = taskList;
    }

    public WorkflowExecution executeWorkflow(final City city) {
        WorkflowOptions workflowOptions = new WorkflowOptions.Builder()
                .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.AllowDuplicate)
                .setTaskStartToCloseTimeout(Duration.ofSeconds(10))
                .setExecutionStartToCloseTimeout(Duration.ofDays(10))
                .setDelayStart(Duration.ZERO)
                .setTaskList(taskList)
                .setRetryOptions(new RetryOptions.Builder()
                        .setMaximumAttempts(5)
                        .setInitialInterval(Duration.ofSeconds(1))
                        .build())
                //.setWorkflowId(OptionsUtils.merge(a.workflowId(), o.getWorkflowId(), String.class))
                //.setCronSchedule(OptionsUtils.merge(cronAnnotation, o.getCronSchedule(), String.class))
                //.setMemo(o.getMemo())
                //.setSearchAttributes(o.getSearchAttributes())
                //.setContextPropagators(o.getContextPropagators())
                .validateBuildWithDefaults();
        WeatherWorkflow workflow = workflowClient.newWorkflowStub(WeatherWorkflow.class, workflowOptions);
        return WorkflowClient.start(workflow::process, city.getLatitude(), city.getLongitude(), city.getCity());
    }

    public void registerDomain(String domain) throws BusinessException {
        RegisterDomainRequest request = new RegisterDomainRequest();
        request.setDescription("Java Samples");
        request.setEmitMetric(false);
        request.setName(domain);
        int retentionPeriodInDays = 1;
        request.setWorkflowExecutionRetentionPeriodInDays(retentionPeriodInDays);
        try {
            workflowClient.getService().RegisterDomain(request);
        } catch (Exception e) {
            System.out.println("Domain is already registered");
            throw new BusinessException("Domain is already registered");
        }
    }
}
