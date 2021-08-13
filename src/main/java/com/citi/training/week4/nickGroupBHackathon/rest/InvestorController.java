package com.citi.training.week4.nickGroupBHackathon.rest;

import com.citi.training.week4.nickGroupBHackathon.entities.Investor;
import com.citi.training.week4.nickGroupBHackathon.services.InvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/investors")
public class InvestorController {

    @Autowired
    private InvestorService investorService;


    @GetMapping
    public Collection<Investor> getInvestors() {
        return investorService.getAllInvestors();
    }



}