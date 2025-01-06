package com.dana.batch.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobService {

	   @Autowired
	    private JobRegistry jobRegistry;

	    public Job findJobByName(String jobName) {
	        try {
	            return jobRegistry.getJob(jobName);
	        } catch (NoSuchJobException e) {
	            // Handle job not found exception
	            return null;
	        }
	    }
}
