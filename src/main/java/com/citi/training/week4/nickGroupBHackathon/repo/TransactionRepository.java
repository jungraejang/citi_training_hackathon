package com.citi.training.week4.nickGroupBHackathon.repo;

import com.citi.training.week4.nickGroupBHackathon.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Collection<Transaction> findByInvestorId(Integer investorId);

}