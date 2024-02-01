package com.master.dataloader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkCodeRepository extends JpaRepository<NetworkCode, String> {
}
