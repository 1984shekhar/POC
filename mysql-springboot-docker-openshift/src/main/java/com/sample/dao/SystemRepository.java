package com.sample.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sample.model.System;

@Repository
public interface SystemRepository extends CrudRepository<System,Long> {
	

}
