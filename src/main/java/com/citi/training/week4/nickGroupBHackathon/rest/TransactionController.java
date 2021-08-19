package com.citi.training.week4.nickGroupBHackathon.rest;

import com.citi.training.week4.nickGroupBHackathon.entities.Transaction;
import com.citi.training.week4.nickGroupBHackathon.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    Hashtable<String, Integer> symbol = new Hashtable<>();
    Hashtable<String, Double> cashInfo = new Hashtable<>();

    @Autowired
    private TransactionService transactionService;


    @GetMapping
    public Collection<Transaction> getTransactions() {
        return transactionService.getAllTransactions();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Collection<Transaction> getTransactionsByInvestor(@PathVariable("id") int id) {
        Collection<Transaction> investorTransactions = transactionService.getTransactionsByInvestorId(id);
        Iterator<Transaction> iterator = investorTransactions.iterator();
        Collection<Transaction> investments=new ArrayList<>();

        while(iterator.hasNext()) {
            Transaction next = iterator.next();
            if(next.getType().equals("Cash")){
                cashInfo.put(next.getSymbol(), next.getPrice());
            }else {
                symbol.put(next.getSymbol(), next.getAmount());
                investments.add(next);
            }
        }
//        return transactionService.getTransactionsByInvestorId(id);
//        portfolioTotal();
        return investments;
    }
//    throws IOException

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/total")
    public Double portfolioTotal(@PathVariable("id") int id) throws IOException {
        transactionService.getTransactionsByInvestorId(id);
//        for(String s, Integer i:symbol) {
//            Stock stock = YahooFinance.get(s);
//            System.out.println(stock);
//        }
        Set<String> keys = symbol.keySet();
        Iterator<String> iteratorKeys = keys.iterator();

        Collection<Integer> values = symbol.values();
        Iterator<Integer> iteratorValues = values.iterator();

        Double totalValue = 0.0;
        while(iteratorKeys.hasNext()) {
            Stock stock = YahooFinance.get(iteratorKeys.next());
                totalValue += iteratorValues.next() * stock.getQuote().getPrice().doubleValue();
//            symbol.put(iterator.next().getSymbol(), iterator.next().getAmount());
        }
//        Stock stock = YahooFinance.get("TSLA");
//        System.out.println(stock);
//        String[] arr = Arrays.copyOf(symbol.toArray(), symbol.size(), String[].class);
////        arr = symbol.toArray(arr);
//        System.out.println("****************" + arr + "***************");
//        Map<String, Stock> stocks = YahooFinance.get(arr);
//        System.out.println("************ \n" + stocks + "********* \n");
//        Stock stock = YahooFinance.get(symbol);
//
//        BigDecimal price = stock.getQuote().getPrice();
//        BigDecimal change = stock.getQuote().getChangeInPercent();
//        BigDecimal peg = stock.getStats().getPeg();
//        BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();
//
//        stock.print();
        return totalValue ;
    }
    /*@RequestMapping(method = RequestMethod.GET, value = "/{id}/total_over_time")
    public Double portfolioTotalOverTime(@PathVariable("id") int id) throws IOException {
        Double currentValue = portfolioTotal(id);
        Set<String> keys = symbol.keySet();
        Iterator<String> iteratorKeys = keys.iterator();

        Collection<Integer> values = symbol.values();
        Iterator<Integer> iteratorValues = values.iterator();

        Double totalValue = 0.0;
        while(iteratorKeys.hasNext()) {
            Stock stock = YahooFinance.get(iteratorKeys.next());
            totalValue += iteratorValues.next() * stock.getQuote().getPrice().doubleValue();
        }
        return totalValue ;
    }*/





}