package com.citi.training.week4.nickGroupBHackathon.repo;

import com.citi.training.week4.nickGroupBHackathon.entities.Investor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface InvestorRepository extends JpaRepository<Investor, Integer> {
    Collection<Investor> findByName(String name);
}