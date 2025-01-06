package com.dana.batch.job;


import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import com.dana.batch.domain.Car;
import com.dana.batch.service.CarService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewCarWriter extends CarLoaderJob implements ItemWriter<Car> {
	
	private CarService carService;
	
	public NewCarWriter(CarService carService) {
		this.carService = carService;
		log.info("Creating instnce of writer");
	}

	@Override
	public void write(Chunk<? extends Car> chunk) throws Exception {

		log.info("chunk size: {}",chunk.size());
		if (chunk.isEmpty()) return;
		
		try {
			chunk.getItems().forEach(car -> {
				car.setId(null);
				carService.save(car);
			});
		} catch (Exception e) {
			log.error("Failed write: {}",e.getMessage());
			throw e;
		}
	}

}
