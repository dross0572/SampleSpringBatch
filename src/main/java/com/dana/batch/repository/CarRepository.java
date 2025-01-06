package com.dana.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dana.batch.domain.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {

}
