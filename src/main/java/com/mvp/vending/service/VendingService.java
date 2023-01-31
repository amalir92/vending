package com.mvp.vending.service;

import com.mvp.vending.model.Output;
import com.mvp.vending.model.ProductId;
import com.mvp.vending.model.Transaction;

public interface VendingService {

    public void depositCoins(Integer[] coins, String user) throws Exception;

    public Output buyProducts(Transaction transaction, String user) throws Exception;

    public void addOrUpdateProducts(String productId, String sellerId, String productName, int amount, double cost)
            throws Exception;

    public void updateProductQuantity(ProductId productId, int dispatchamount) throws Exception;

    public void removeProducts(String productId, String sellerId) throws Exception;

    public void saveTransaction(Transaction transaction) throws Exception;

    public Output resetDeposit(String user) throws Exception;
}
