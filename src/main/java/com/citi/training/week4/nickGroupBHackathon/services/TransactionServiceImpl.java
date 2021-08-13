package com.citi.training.week4.nickGroupBHackathon.services;

import com.citi.training.week4.nickGroupBHackathon.entities.Transaction;
import com.citi.training.week4.nickGroupBHackathon.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Collection<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

}
