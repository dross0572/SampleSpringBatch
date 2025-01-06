package com.dana.batch.web.controller;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dana.batch.domain.Car;
import com.dana.batch.service.CarService;
import com.dana.batch.web.dto.CarDto;

@RestController
@RequestMapping("/api")
public class CarController {
	
	private CarService carService;
	
	public CarController(CarService carService) {
		this.carService = carService;
	}
	
	@GetMapping(path= "/cars", produces="application/json")
	public Collection<CarDto> getCars() {
		Collection<Car> cars = carService.getAll();
		Collection<CarDto> result = new ArrayList<CarDto>();
		cars.forEach(car -> {
			result.add(new CarDto(car.getId(), car.getName(), car.getManufacturer(), car.getModel()));
		});
		
		return result;
	}
	
	@GetMapping(path = "/cars/{id}", produces="application/json")
	public CarDto getCar(@PathVariable String id) {
		Car car = carService.getCar(Integer.valueOf(id));
		CarDto carDto = new CarDto(car.getId(), car.getName(), car.getManufacturer(), car.getModel());
		return carDto;
	}
	
	@PostMapping(path = "/cars")
	public Car createCar(@RequestBody Car car) {
		
		Car newCar = carService.save(car);
		return newCar;
	}

}
