package com.eagle.voters.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eagle.voters.entity.UserMaster;

public interface UserMasterRepo extends JpaRepository<UserMaster, Long> {

}
