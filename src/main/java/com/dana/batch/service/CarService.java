package com.dana.batch.service;

import java.util.Collection;

import org.springframework.retry.RetryException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dana.batch.domain.Car;
import com.dana.batch.repository.CarRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CarService {
	private CarRepository carRepository;
	
	public CarService (CarRepository carRepository) {
		this.carRepository = carRepository;
	}
	
	@Transactional(readOnly=true)
	public Collection<Car> getAll() {
		return carRepository.findAll();
	}
	
	
	@Transactional(readOnly=true)
	public Car getCar(Integer id) {
		Car car = carRepository.getReferenceById(id);
		car.getId();
		
		return car;
	}
	
	@Transactional(readOnly=false)
	public Car save(Car car) {
		log.info("Saving a car:{}",car.getName());
		Car result = null;
		result = carRepository.save(car);
		carRepository.flush();
		log.info("Saved car: {}", result);
		return result;
	}
	

}
