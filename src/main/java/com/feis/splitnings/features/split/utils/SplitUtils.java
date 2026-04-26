package com.feis.splitnings.features.split.utils;

import com.feis.splitnings.features.split.data.Split;

public class SplitUtils {

    public static Split buildDefaultSplit(Double amount, Integer accountId) {
        Split defaultSplit = new Split();
        defaultSplit.setName("Default");
        defaultSplit.setSplitPercentage((short) 100);
        defaultSplit.setAccountId(accountId);

        defaultSplit.setAvailableAmount(amount);
        defaultSplit.setTheoreticalAmount(amount);

        defaultSplit.setActive(true);
        defaultSplit.setIsDefault(true);

        return defaultSplit;
    }

    /**
     * Computes split amount (either theoretical or available).
     *
     * Similar to the other overload method, you'd prefer
     * using this when you want to calculate the split amount without considering
     * its previous value.
     */
    public static double computeSplitAmount(double accountWealth, short splitPercentage) {
        return (accountWealth * splitPercentage) / 100;
    }

    /**
     * Computes the split amount (either theoretical or available) based on the total amount to topUp
     */
    public static double computeSplitAmount(double topUpAmount, short splitPercentage, double currentSplitAmount) {
        double computedTopUpAmount = (topUpAmount * splitPercentage) / 100;
        return currentSplitAmount + computedTopUpAmount;
    }
}
