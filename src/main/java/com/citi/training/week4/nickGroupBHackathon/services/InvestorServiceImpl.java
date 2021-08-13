package com.citi.training.week4.nickGroupBHackathon.services;

import com.citi.training.week4.nickGroupBHackathon.entities.Investor;
import com.citi.training.week4.nickGroupBHackathon.repo.InvestorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class InvestorServiceImpl implements InvestorService {

    @Autowired
    private InvestorRepository investorRepository;

    @Override
    public Collection<Investor> getAllInvestors() {
        return investorRepository.findAll();
    }

}
