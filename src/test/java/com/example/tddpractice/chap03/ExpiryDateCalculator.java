package com.example.tddpractice.chap03;

import java.time.LocalDate;
import java.time.YearMonth;

public class ExpiryDateCalculator {
    public LocalDate calculateExpiryDate(PayData payData) {
        int addMonth = payData.getPayAmount() == 100000? 12 : payData.getPayAmount() / 10000;
        if (payData.getFirstBillingDate() != null) {
            return expiryDateUsingFirstBillingDate(payData, addMonth);
        } else{
            return payData.getBillingDate().plusMonths(addMonth);
        }
    }

    private LocalDate expiryDateUsingFirstBillingDate(PayData payData, int addMonth) {
        LocalDate candidateExp = payData.getBillingDate().plusMonths(addMonth);
        final int dayOfFirstBilling = payData.getFirstBillingDate().getDayOfMonth();
        final int dayLenOfCandiMon = YearMonth.from(candidateExp).lengthOfMonth();
        if (dayOfFirstBilling != candidateExp.getDayOfMonth()) {
            if (dayLenOfCandiMon < dayOfFirstBilling) {
                return candidateExp.withDayOfMonth(dayLenOfCandiMon);
            }
            return candidateExp.withDayOfMonth(dayOfFirstBilling);
        }else{
            return candidateExp;
        }
    }
}
