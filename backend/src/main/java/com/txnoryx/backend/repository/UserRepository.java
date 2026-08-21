package com.txnoryx.backend.repository;

import com.txnoryx.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}