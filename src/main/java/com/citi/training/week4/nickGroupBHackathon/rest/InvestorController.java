package com.citi.training.week4.nickGroupBHackathon.rest;

import com.citi.training.week4.nickGroupBHackathon.entities.Investor;
import com.citi.training.week4.nickGroupBHackathon.entities.Transaction;
import com.citi.training.week4.nickGroupBHackathon.services.InvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

//    @GetMapping
//    public Collection<Investor> getInvestorsByEmail() { return investorService.findInvestorsByEmail();}

    @RequestMapping(method = RequestMethod.GET, value = "/{email}")
    public Collection<Investor> getInvestorsByEmail(@PathVariable("email") String email) {
        return investorService.findInvestorsByEmail(email);
    }

}