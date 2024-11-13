package com.example.scsa_community2.repository;

import com.example.scsa_community2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
