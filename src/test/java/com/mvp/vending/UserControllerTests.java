package com.mvp.vending;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvp.vending.controller.AuthenticationRequest;
import com.mvp.vending.model.ProductId;
import com.mvp.vending.model.Transaction;
import com.mvp.vending.model.User;
import com.mvp.vending.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void depositCoins() throws Exception {

        AuthenticationRequest request = new AuthenticationRequest("user1", "password");

        MvcResult result = mockMvc.perform(post("/vending/user/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();

        String replaced = response.replace("{\"token\": \"", "");
        String token = replaced.replace("\"}", "");

        Integer[] coins = { 50, 20, 50, 10 };

        mockMvc.perform(post("/vending/user/deposit/user1")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(coins)))
                .andExpect(status().isOk());

        Optional<User> user1 = userRepository.findByUsername("user1");
        assertThat(user1.get().getDeposit()).isEqualTo(130);
    }

    @Test
    void resetDeposit() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("user1", "password");

        MvcResult result = mockMvc.perform(post("/vending/user/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();

        String replaced = response.replace("{\"token\": \"", "");
        String token = replaced.replace("\"}", "");

        

        mockMvc.perform(post("/vending/user/reset/user1")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        Optional<User> user1 = userRepository.findByUsername("user1");
        assertThat(user1.get().getDeposit()).isEqualTo(0);

    }

    @Test
  void productBuyTest() throws Exception {

    AuthenticationRequest request = new AuthenticationRequest("user1", "password");

    MvcResult result = mockMvc.perform(post("/vending/user/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk()).andReturn();

    String token = result.getResponse().getContentAsString();

    Transaction transaction = new Transaction(new ProductId("001", "s001"), 1);
    Integer[] coins = { 50, 20, 50, 10 };

    mockMvc.perform(post("/vending/user/deposit/user1")
        .header("Authorization", "Bearer " + token)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(coins)))
        .andExpect(status().isOk());


    mockMvc.perform(post("/vending/user/buy/user1")
        .header("Authorization", "Bearer " + token)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(transaction)))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1));
  }

    @Test
    public void nonexistentUserCannotSignin() throws Exception {

        AuthenticationRequest request = new AuthenticationRequest("nonexistentuser", "password");
        mockMvc.perform(post("/vending/user/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden()).andReturn();
    }

}
