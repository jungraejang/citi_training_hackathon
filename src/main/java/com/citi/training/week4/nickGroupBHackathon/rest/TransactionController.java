package com.citi.training.week4.nickGroupBHackathon.rest;

import com.citi.training.week4.nickGroupBHackathon.entities.Transaction;
import com.citi.training.week4.nickGroupBHackathon.services.TransactionService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import java.time.LocalDateTime;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {


    Hashtable<String, ArrayList<Integer>> symbol = new Hashtable<>();
    Hashtable<String, ArrayList<Double>> cashInfo = new Hashtable<>();
    Hashtable<String, ArrayList<LocalDateTime>> symbolTime = new Hashtable<>();
    Hashtable<String, ArrayList<LocalDateTime>> cashInfoTime = new Hashtable<>();

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
                if(cashInfo.containsKey(next.getSymbol())){
                    cashInfo.get(next.getSymbol()).add(next.getPrice());
                    cashInfoTime.get(next.getSymbol()).add(next.getTimeStamp());
                }
                else{
                    ArrayList<Double> prices = new ArrayList<>();
                    prices.add(next.getPrice());
                    cashInfo.put(next.getSymbol(), prices);
                    ArrayList<LocalDateTime> times = new ArrayList<>();
                    times.add(next.getTimeStamp());
                    cashInfoTime.put(next.getSymbol(), times);
                }
            }else {
                if(symbol.containsKey(next.getSymbol())){
                    symbol.get(next.getSymbol()).add(next.getAmount());
                    symbolTime.get(next.getSymbol()).add(next.getTimeStamp());
                }else {
                    ArrayList<Integer> amount = new ArrayList<>();
                    amount.add(next.getAmount());
                    symbol.put(next.getSymbol(), amount);
                    ArrayList<LocalDateTime> times = new ArrayList<>();
                    times.add(next.getTimeStamp());
                    symbolTime.put(next.getSymbol(), times);
                }
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

        Collection<ArrayList<Integer>> values = symbol.values();
        Iterator<ArrayList<Integer>> iteratorValues = values.iterator();

        Double totalValue = 0.0;
        while(iteratorKeys.hasNext()) {
            Stock stock = YahooFinance.get(iteratorKeys.next());
            ArrayList<Integer> amountVals = iteratorValues.next();
            for(int i =0;i<amountVals.size();i++){
                totalValue += amountVals.get(i) * stock.getQuote().getPrice().doubleValue();
                System.out.println(stock.getSymbol()+" "+stock.getQuote().getPrice().doubleValue());
            }
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
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/total_over_time")
    public Collection<Double> portfolioTotalOverTime(@PathVariable("id") int id) throws IOException {
        Double currentValue = portfolioTotal(id);
        Collection<Double> changeValue = new ArrayList<>();
        Double prevVal = 0.0;
        Set<String> keys = symbol.keySet();
        Iterator<String> iteratorKeys = keys.iterator();

        Collection<ArrayList<Integer>> values = symbol.values();
        Iterator<ArrayList<Integer>> iteratorValues = values.iterator();
        Collection<ArrayList<LocalDateTime>> times = symbolTime.values();
        Iterator<ArrayList<LocalDateTime>> iteratorTimes = times.iterator();


        //last week
        prevVal = 0.0;
        while(iteratorKeys.hasNext()) {
            Stock stock = YahooFinance.get(iteratorKeys.next(),true);
            List<HistoricalQuote> weeks = stock.getHistory(Interval.WEEKLY);
            HistoricalQuote week = weeks.get(weeks.size()-2);
            Double price = week.getClose().doubleValue();
            ArrayList<Integer> amountsS = iteratorValues.next();
            ArrayList<LocalDateTime> timeList = iteratorTimes.next();
            for(int i =0;i<amountsS.size();i++){
                Date date = Date.from(timeList.get(i).atZone(ZoneId.systemDefault()).toInstant());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                if(calendar.before(week.getDate())) {
                    prevVal += price * amountsS.get(i);
                }
            }
        }
        changeValue.add((currentValue-prevVal));
/*
        //last month
        prevVal = 0.0;
        while(iteratorKeys.hasNext()) {
            Stock stock = YahooFinance.get(iteratorKeys.next());
            prevVal += iteratorValues.next() * stock.getQuote().getPrice().doubleValue();
        }
        changeValue.add((currentValue-prevVal));

        //last quarter
        prevVal = 0.0;
        while(iteratorKeys.hasNext()) {
            Stock stock = YahooFinance.get(iteratorKeys.next());
            prevVal += iteratorValues.next() * stock.getQuote().getPrice().doubleValue();
        }
        changeValue.add((currentValue-prevVal));

        //year to date
        prevVal = 0.0;
        while(iteratorKeys.hasNext()) {
            Stock stock = YahooFinance.get(iteratorKeys.next());
            prevVal += iteratorValues.next() * stock.getQuote().getPrice().doubleValue();
        }
        changeValue.add((currentValue-prevVal));
*/
        return changeValue ;
    }





}