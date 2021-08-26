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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @GetMapping
    public Collection<Transaction> getTransactions() {
        return transactionService.getAllTransactions();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ArrayList<Transaction> getTransactionsByInvestor(@PathVariable("id") int id) {
        Collection<Transaction> investorTransactions = transactionService.getTransactionsByInvestorId(id);
        Iterator<Transaction> iterator = investorTransactions.iterator();
        ArrayList<Transaction> investments=new ArrayList<>();

        while(iterator.hasNext()) {
            investments.add(iterator.next());
        }
        return investments;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/stock_totals")
    public ArrayList<Object> stockTotals(@PathVariable("id") int id) throws IOException {

        ArrayList<Transaction> transactions=getTransactionsByInvestor(id);

        Double allTotal = 0.0;
        Hashtable<String,Double> allStockValues = new Hashtable<>();
        for(Transaction t:transactions) {
            if(!t.getType().equals("Cash")){
                String symbol = t.getSymbol();
                Stock stock = YahooFinance.get(symbol);
                Double price = t.getAmount()*stock.getQuote().getPrice().doubleValue();
                if(allStockValues.containsKey(symbol)){
                    allStockValues.replace(symbol,allStockValues.get(symbol)+price);
                }
                else{
                    allStockValues.put(symbol,price);
                }
                allTotal+=price;
            }
        }
        ArrayList<Object> totals = new ArrayList<>();
        totals.add(allTotal);
        totals.add(allStockValues);
        return totals;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/cash_total")
    public ArrayList<Object> getInvestorValuation(@PathVariable("id") int id) {
        ArrayList<Transaction> transactions=getTransactionsByInvestor(id);
        Double cashValue = 0.0;
        Hashtable<String,Double> allCashValues = new Hashtable<>();

        for(Transaction t:transactions) {
            if(t.getType().equals("Cash")){
                String symbol = t.getSymbol();
                Double price = t.getPrice();
                if(allCashValues.containsKey(symbol)){
                    allCashValues.replace(symbol,allCashValues.get(symbol)+price);
                }
                else{
                    allCashValues.put(symbol,price);
                }
                cashValue+=price;
            }
        }
        ArrayList<Object> totals = new ArrayList<>();
        totals.add(cashValue);
        totals.add(allCashValues);

        return totals;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/total_over_time")
    public Collection<Double> portfolioTotalOverTime(@PathVariable("id") int id) throws IOException {
        ArrayList<Transaction> transactions=getTransactionsByInvestor(id);
        Double currentValue = Double.valueOf(stockTotals(id).get(0).toString());
        ArrayList<Double> changeValue = new ArrayList<>(Arrays.asList(currentValue,currentValue,currentValue,currentValue));

        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime lastWeek = currentTime.minusWeeks(1);
        Date weekDate = Date.from(lastWeek.atZone(ZoneId.systemDefault()).toInstant());
        Calendar weekCalendar = Calendar.getInstance();
        weekCalendar.setTime(weekDate);

        LocalDateTime lastMonth = currentTime.minusMonths(1);
        Date monthDate = Date.from(lastMonth.atZone(ZoneId.systemDefault()).toInstant());
        Calendar monthCalendar = Calendar.getInstance();
        monthCalendar.setTime(monthDate);

        LocalDateTime lastQuarter = currentTime.minusMonths(3);
        Date quarterDate = Date.from(lastQuarter.atZone(ZoneId.systemDefault()).toInstant());
        Calendar quarterCalendar = Calendar.getInstance();
        quarterCalendar.setTime(quarterDate);

        Calendar firstDayOfYear = Calendar.getInstance();
        firstDayOfYear.set(firstDayOfYear.get(Calendar.YEAR),0,1);
        for(Transaction t:transactions) {
            if(!t.getType().equals("Cash")){
                String symbol = t.getSymbol();
                Stock stock = YahooFinance.get(symbol,true);
                Date date = Date.from(t.getTimeStamp().atZone(ZoneId.systemDefault()).toInstant());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                List<HistoricalQuote> weeks = stock.getHistory(weekCalendar,Interval.DAILY);
                HistoricalQuote week = weeks.get(0);
                Double priceWeek = week.getClose().doubleValue();

                List<HistoricalQuote> months = stock.getHistory(monthCalendar,Interval.DAILY);
                HistoricalQuote month = months.get(0);
                Double priceMonth = month.getClose().doubleValue();

                List<HistoricalQuote> quarters = stock.getHistory(quarterCalendar,Interval.DAILY);
                HistoricalQuote quarter = quarters.get(0);
                Double priceQuarter = quarter.getClose().doubleValue();

                List<HistoricalQuote> yearToDate = stock.getHistory(firstDayOfYear,Interval.DAILY);
                HistoricalQuote beginningOfYear = yearToDate.get(0);
                Double priceYear = beginningOfYear.getClose().doubleValue();

                if(calendar.before(week.getDate())) {
                    Double price = t.getAmount()*priceWeek;
                    changeValue.set(0,changeValue.get(0)-price);
                }
                if(calendar.before(month.getDate())) {
                    Double price = t.getAmount()*priceMonth;
                    changeValue.set(1,changeValue.get(1)-price);
                }
                if(calendar.before(quarter.getDate())) {
                    Double price = t.getAmount()*priceQuarter;
                    changeValue.set(2,changeValue.get(2)-price);
                }
                if(calendar.before(beginningOfYear.getDate())) {
                    Double price = t.getAmount()*priceYear;
                    changeValue.set(3,changeValue.get(3)-price);
                }
            }
        }

        return changeValue ;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/cash_total_over_time")
    public ArrayList<Double> getInvestorValuationOverTime(@PathVariable("id") int id) {
        ArrayList<Transaction> transactions=getTransactionsByInvestor(id);
        Double currentValue = Double.valueOf(getInvestorValuation(id).get(0).toString());

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime lastWeek = currentTime.minusWeeks(1);
        LocalDateTime lastMonth= currentTime.minusMonths(1);
        LocalDateTime lastQuarter = currentTime.minusMonths(3);
        LocalDateTime yearToDate = LocalDateTime.of(currentTime.getYear(), Month.JANUARY, 1, 0,0,0);

        ArrayList<Double> allCashTotalOverTime = new ArrayList<>(Arrays.asList(currentValue,currentValue,currentValue,currentValue));

        for(Transaction t:transactions) {
            if(t.getType().equals("Cash")){
                Double nextValue = t.getPrice();
                LocalDateTime nextTime = t.getTimeStamp();
                if(nextTime.isBefore(lastWeek)) {
                    allCashTotalOverTime.set(0,allCashTotalOverTime.get(0)-nextValue);
                }
                if(nextTime.isBefore(lastMonth)) {
                    allCashTotalOverTime.set(1,allCashTotalOverTime.get(1)-nextValue);
                }
                if(nextTime.isBefore(lastQuarter)) {
                    allCashTotalOverTime.set(2,allCashTotalOverTime.get(2)-nextValue);
                }
                if(nextTime.isBefore(yearToDate)) {
                    allCashTotalOverTime.set(3,allCashTotalOverTime.get(3)-nextValue);
                }
            }
        }

        return allCashTotalOverTime;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/market_movers")
    public ArrayList<Object> marketMovers(@PathVariable("id") int id) throws IOException {
        ArrayList<Transaction> transactions=getTransactionsByInvestor(id);

        Hashtable<String,Double> marketMovers = new Hashtable<>();
        Hashtable<String,Double> maxs = new Hashtable<>();
        Hashtable<String,Double> mins = new Hashtable<>();
        for(Transaction t:transactions) {
            if(!t.getType().equals("Cash")&&!marketMovers.containsKey(t.getSymbol())){
                Stock stock = YahooFinance.get(t.getSymbol());
                Double stockChange = stock.getQuote().getPrice().doubleValue() - stock.getQuote().getPreviousClose().doubleValue();
                Double percentChange = (stockChange*100) / (Math.abs(stock.getQuote().getPreviousClose().doubleValue()));
                marketMovers.put(stock.getSymbol(),percentChange);
            }
        }
        for(int i = 0; i < 5 ; i++) {
            Double localMax = 0.0;
            String maxString = "";
            Double localMin = 0.0;
            String minString = "";

            Enumeration e = marketMovers.keys();
            while(e.hasMoreElements()) {
                String s = e.nextElement().toString();
                if(marketMovers.get(s) > localMax) {
                    localMax = marketMovers.get(s);
                    maxString = s;
                }
                if(marketMovers.get(s) < localMin) {
                    localMin = marketMovers.get(s);
                    minString = s;
                }
            }
            if(!maxString.isEmpty()){
                maxs.put(maxString,localMax);
                marketMovers.remove(maxString);
            }
            if(!minString.isEmpty()){
                mins.put(minString, localMin);
                marketMovers.remove(minString);
            }
        }

        ArrayList<Object> maxsAndMins = new ArrayList<>();
        maxsAndMins.add(maxs);
        maxsAndMins.add(mins); //sort in frontend
        return maxsAndMins;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/graph_week")
    public Collection<Double> graphNetWorthWeek(@PathVariable("id") int id) throws IOException {
        Double currentValue = Double.valueOf(stockTotals(id).get(0).toString());
        Double currentCashValue = Double.valueOf(getInvestorValuation(id).get(0).toString());
        ArrayList<Transaction> transactions=getTransactionsByInvestor(id);
        ArrayList<Double> changeValue = new ArrayList<>(Arrays.asList(currentValue+currentCashValue,0.0,0.0,0.0,0.0,0.0,0.0));
        LocalDateTime pastTime = LocalDateTime.now();
        for (int j = 1; j < 7; j++) {
            pastTime = pastTime.minusDays(1);
            Date date = Date.from(pastTime.atZone(ZoneId.systemDefault()).toInstant());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            for(Transaction t:transactions) {
                String symbol = t.getSymbol();
                if(!t.getType().equals("Cash")){
                    Stock stock = YahooFinance.get(symbol);
                    HistoricalQuote dayQuote = stock.getHistory(calendar, Interval.DAILY).get(0);
                    Double dayPrice = dayQuote.getClose().doubleValue();
                    Double price = t.getAmount()*dayPrice;
                    Calendar dayCalendar = Calendar.getInstance();
                    Date stockDate = Date.from(t.getTimeStamp().atZone(ZoneId.systemDefault()).toInstant());
                    dayCalendar.setTime(stockDate);
                    if (dayCalendar.before(dayQuote.getDate())) {
                        changeValue.set(j,changeValue.get(j)+price);
                    }
                }else{
                    Double price = t.getPrice();
                    LocalDateTime cashTime = t.getTimeStamp();
                    if(cashTime.isBefore(pastTime)) {
                        changeValue.set(j,changeValue.get(j)+price);
                    }
                }
            }
        }
        return changeValue;
    }
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/year_over_year")
    public ArrayList<Double> networthYearPlot(@PathVariable("id") int id) throws IOException {
        ArrayList<Transaction> transactions=getTransactionsByInvestor(id);
        ArrayList<Double> yearlyValue = new ArrayList<>(Arrays.asList(0.0,0.0,0.0,0.0,0.0));
        Calendar firstDayOfYear = Calendar.getInstance();
        for (int j = 0; j < 5; j++) {
            firstDayOfYear.set(firstDayOfYear.get(Calendar.YEAR)-j,0,1);
            Double prevVal = 0.0;
            for(Transaction t:transactions) {
                String symbol = t.getSymbol();
                LocalDateTime transactionTime = t.getTimeStamp();
                Calendar dayCalendar = Calendar.getInstance();
                Date stockDate = Date.from(transactionTime.atZone(ZoneId.systemDefault()).toInstant());
                dayCalendar.setTime(stockDate);
                if(!t.getType().equals("Cash")){
                    Stock stock = YahooFinance.get(symbol, true);
                    HistoricalQuote dayQuote = stock.getHistory(firstDayOfYear, Interval.DAILY).get(0);
                    Double dayPrice = dayQuote.getClose().doubleValue();
                    Double price = t.getAmount()*dayPrice;
                    if (dayCalendar.before(firstDayOfYear)) {
                        yearlyValue.set(j,yearlyValue.get(j)+price);
                    }
                }else{
                    if(dayCalendar.before(firstDayOfYear)) {
                        yearlyValue.set(j,yearlyValue.get(j)+t.getPrice());
                    }
                }
            }
        }
        return yearlyValue;
    }

}