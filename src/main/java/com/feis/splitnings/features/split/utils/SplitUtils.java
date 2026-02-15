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
}
