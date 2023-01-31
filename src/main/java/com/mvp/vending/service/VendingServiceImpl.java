package com.mvp.vending.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mvp.vending.exception.InvalidProductException;
import com.mvp.vending.exception.NotEnoughDeposit;
import com.mvp.vending.exception.NotValidCoinsException;
import com.mvp.vending.exception.NotValidProductCost;
import com.mvp.vending.model.Output;
import com.mvp.vending.model.Product;
import com.mvp.vending.model.ProductId;
import com.mvp.vending.model.Transaction;
import com.mvp.vending.model.User;
import com.mvp.vending.repository.ProductRepository;
import com.mvp.vending.repository.TransactionRepository;
import com.mvp.vending.repository.UserRepository;

@Service
public class VendingServiceImpl implements VendingService {

    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    ArrayList<Integer> validCoins = new ArrayList<>(Arrays.asList(5, 10, 20, 50, 100));

    VendingServiceImpl(ProductRepository productRepository, TransactionRepository transactionRepository,
            UserRepository userRepository) {
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Output buyProducts(Transaction transaction, String username) throws Exception {
        double totalCost = 0;
        Output out = null;
        if (transaction.getProductId() != null) {
            Optional<Product> product = productRepository.findById(transaction.getProductId());
            if(!product.isPresent())
            {
                throw new InvalidProductException();
            }
            if (product.get().getAmountAvailable() > transaction.getAmount()) 
            {
                totalCost = totalCost + product.get().getProductCost() * transaction.getAmount();
            }

            Optional<User> buyer = userRepository.findByUsername(username);
            User u = buyer.get();
            double payment = u.getDeposit();
            if (payment >= totalCost) {
                double remainingCash = payment - totalCost;
                int[] changeDueCoins = validRemainingCoins(remainingCash);
                out = new Output(transaction.getProductId().getProductId(), transaction.getAmount(), totalCost,
                        changeDueCoins);
                System.out.println(out);
                updateProductQuantity(transaction.getProductId(), transaction.getAmount());
                saveTransaction(transaction);
                u.setDeposit(0);
                userRepository.save(u);
                return out;
            }
            throw new NotEnoughDeposit();
        }
        return out;
    }

    public double validCoinsSum(Integer[] coins) throws Exception {
        double total = 0;
        if (coins.length != 0) {
            for (Integer i : coins) {
                if (validCoins.contains(i)) {
                    total = total + i;
                } else {
                    throw new NotValidCoinsException();
                }
            }
            return total;
        }
        return 0;
    }

    public int[] validRemainingCoins(double remainingCash) {
        ArrayList<Integer> coins = new ArrayList<>();

        if (remainingCash > 0) {
            int change = (int) (Math.ceil(remainingCash));
            int hundreds = Math.round((int) change / 100);
            change = change % 100;
            int fiftees = Math.round((int) change / 50);
            change = change % 50;
            int twenties = Math.round((int) change / 20);
            change = change % 20;
            int tens = Math.round((int) change / 10);
            change = change % 10;
            int fives = Math.round((int) change / 5);
            change = change % 5;

            for (int i = 0; i < hundreds; i++) {
                coins.add(100);
            }
            for (int i = 0; i < fiftees; i++) {
                coins.add(50);
            }
            for (int i = 0; i < twenties; i++) {
                coins.add(20);
            }
            for (int i = 0; i < tens; i++) {
                coins.add(10);
            }
            for (int i = 0; i < fives; i++) {
                coins.add(5);
            }
        }

        int[] out = coins.stream().mapToInt(i -> i).toArray();
        return out;
    }

    @Override
    public void addOrUpdateProducts(String productId, String sellerId, String productName, int amount, double cost)
            throws Exception {
        Optional<Product> product = productRepository.findById(new ProductId(productId, sellerId));
        if (cost % 5 != 0) {
            throw new NotValidProductCost();
        }
        if (product.isPresent()) {
            Product p= product.get();
            p.setAmountAvailable(amount);
            p.setProductName(productName);
            p.setProductCost(cost);
            productRepository.save(p);
        } else {
            productRepository.save(new Product(new ProductId(productId, sellerId), productName, cost, amount));
        }

    }

    @Override
    public void removeProducts(String productId, String sellerId) throws Exception {
        Optional<Product> product = productRepository.findById(new ProductId(productId, sellerId));
        if (product != null) {
            productRepository.delete(product.get());
        }
    }

    @Override
    public void updateProductQuantity(ProductId productId, int dispatchamount) throws Exception {
        Optional<Product> product = productRepository.findById(productId);
        if (product != null) {
            Product p = product.get();
            int amountAvailable = p.getAmountAvailable();
            p.setAmountAvailable(amountAvailable - dispatchamount);
            productRepository.save(p);
        }
    }

    public void saveTransaction(Transaction transaction) throws Exception {
        transactionRepository.save(transaction);
    }

    @Override
    public Output resetDeposit(String username) throws Exception {
        Output out = null;
        Optional<User> user = userRepository.findByUsername(username);
        if (user != null) {
            User u = user.get();
            double availableDeposit = u.getDeposit();
            int[] returnDeposit = validRemainingCoins(availableDeposit);
            u.setDeposit(0);
            userRepository.save(u);
            out = new Output(null, 0, 0, returnDeposit);
        }
        return out;

    }

    @Override
    public void depositCoins(Integer[] coins, String username) throws Exception {
        double deposit = validCoinsSum(coins);
        Optional<User> buyer = userRepository.findByUsername(username);
        if (buyer != null) {
            User u = buyer.get();
            u.setDeposit(deposit);
            userRepository.save(u);
        }
    }
}
