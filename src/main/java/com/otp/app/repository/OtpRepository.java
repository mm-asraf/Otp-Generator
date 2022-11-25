package com.otp.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otp.app.model.OtpModel;

@Repository
public interface OtpRepository extends JpaRepository<OtpModel, Integer>  {

}
