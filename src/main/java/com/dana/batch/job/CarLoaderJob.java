package com.dana.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;

import com.dana.batch.domain.Car;
import com.dana.batch.service.CarService;

@Configuration
public class CarLoaderJob {


    @Bean
    FlatFileItemReader<Car> reader() {
    	FlatFileItemReader<Car> result = 
		new FlatFileItemReaderBuilder<Car>()
				.name("carReader")
				.resource(new ClassPathResource("cars.csv"))
				.delimited()
				.quoteCharacter('"')
				.names("name","manufacturer","model")
				.linesToSkip(1)
				.targetType(Car.class)
				.build();
    	result.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
    	return result;
	}
    
    @Bean
    ItemWriter<Car> newCarWriter(CarService carService) {
    	return new NewCarWriter(carService);
    }
	
//    @Autowired
//    private CarRepository carRepository;
    
//	@Bean
//	ItemWriter<Car> writer() {
//		return new RepositoryItemWriterBuilder<Car>()
//				.repository(carRepository)
//				.methodName("save")
//				.build();
//	}
	
	
	@Bean
	Job importCarJob(JobRepository jobRepository,Step step1) {
		  return new JobBuilder("importCarJob", jobRepository)
				    .start(step1)
				    .build();
				}


    @Bean
    Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,//DataSourceTransactionManager transactionManager,
            FlatFileItemReader<Car> reader, ItemWriter<Car> newCarWriter) {
    	FixedBackOffPolicy bp = new FixedBackOffPolicy();
    	bp.setBackOffPeriod(1000);
	  return new StepBuilder("step1", jobRepository)
	    .<Car, Car> chunk(1, transactionManager)
	    .reader(reader)
	    .writer(newCarWriter)
	    .faultTolerant()
	    .retryLimit(7)
	    .retry(DataIntegrityViolationException.class)
	    .backOffPolicy(bp)
	    .skipPolicy(new CustomSkipPolicy())
//	    .skipLimit(1)
	    .build();
	}
} 
