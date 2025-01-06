package com.dana.batch.web.controller;

import java.time.Instant;
import java.util.Collection;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dana.batch.service.JobService;

@RestController
@RequestMapping("/job")
public class JobController {
	
	private JobRepository jobRepository;
	private JobLauncher jobLauncher;
	private JobService jobService;
	
	public JobController(JobRepository jobRepository, JobLauncher jobLauncher, JobService jobService) {
		this.jobRepository = jobRepository;
		this.jobService = jobService;
		this.jobLauncher = jobLauncher;
	}
	
	
	@GetMapping(path = "/jobs", produces="application/json")
	public Collection<String> getJobs() {
		
		
		return jobRepository.getJobNames();
	}
	
	
	@GetMapping(path = "/start/{jobName}", produces="application/json")
	public String startJob(@PathVariable String jobName) {
	    JobParameters jobParameters = new JobParametersBuilder()
	            .addString("start", Instant.now().toString())
	            .toJobParameters();

		try {
			
			JobExecution je = jobLauncher.run(jobService.findJobByName(jobName), jobParameters);
			System.out.println("Starting job " + je.getId());
		} catch (JobExecutionAlreadyRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobRestartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobInstanceAlreadyCompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Starting job "+jobName;
	}

}
