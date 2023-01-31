package com.mvp.vending.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.mvp.vending.exception.UserAlreadyExists;
import com.mvp.vending.model.Output;
import com.mvp.vending.model.Transaction;
import com.mvp.vending.model.User;
import com.mvp.vending.repository.UserRepository;
import com.mvp.vending.security.JwtTokenProvider;
import com.mvp.vending.service.VendingService;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.ResponseEntity.ok;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/vending/user")
@Api(tags = "Users")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private VendingService vendingService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, this.userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found")).getRoles());

            Map<Object, Object> model = new HashMap<>();
            model.put("token", token);
            return ok(token);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();
            String password = data.getPassword();
            Optional<User> user = userRepository.findByUsername(data.getUsername());
            if (user != null) {
                throw new UserAlreadyExists();
            }
            userRepository.save(User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .roles(Arrays.asList("BUYER"))
                    .build());

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, this.userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found")).getRoles());

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @PostMapping("/deposit/{username}")
    public void deposit(@RequestBody Integer[] coins, @PathVariable String username) throws Exception {
        vendingService.depositCoins(coins, username);
    }

    @PostMapping("/reset/{username}")
    public Output reset(@PathVariable String username) throws Exception {
        return vendingService.resetDeposit(username);
    }

    @PostMapping("/buy/{username}")
    @ResponseStatus(HttpStatus.OK)
    Output buyProducts(@RequestBody Transaction transaction, @PathVariable String username) throws Exception {
    return vendingService.buyProducts(transaction, username);
  }

}
