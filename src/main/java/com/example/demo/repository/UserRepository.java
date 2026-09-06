package com.example.demo.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Order_travelers;
import com.example.demo.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
	Optional<Users> findByEmail(String inputemail);
	
}
