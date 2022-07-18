package com.glady.backend.challenge.dao;

import com.glady.backend.challenge.model.Company;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Integer> {
}