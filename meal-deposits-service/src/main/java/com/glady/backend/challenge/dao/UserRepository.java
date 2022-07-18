package com.glady.backend.challenge.dao;

import com.glady.backend.challenge.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}