package com.mvp.vending;


import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvp.vending.controller.AuthenticationRequest;
import com.mvp.vending.model.Product;
import com.mvp.vending.model.ProductId;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void addProductTest() throws Exception {

    AuthenticationRequest request = new AuthenticationRequest("admin1", "password");

    MvcResult result = mockMvc.perform(post("/vending/user/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk()).andReturn();

    String token = result.getResponse().getContentAsString();

    Product product1 = new Product(new ProductId("003", "s001"),"Chips",25.00, 100);
    Product product2 = new Product(new ProductId("004", "s001"),"Chocalate",70.00, 70);
    ArrayList<Product> products = new ArrayList<>();
    products.add(product1);
    products.add(product2);

    mockMvc.perform(post("/vending/product/add/")
        .header("Authorization", "Bearer " + token)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(products)))
        .andExpect(status().isCreated());
  }

  @Test
  void removeProductTest() throws Exception {

    AuthenticationRequest request = new AuthenticationRequest("admin1", "password");

    MvcResult result = mockMvc.perform(post("/vending/user/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk()).andReturn();

    String token = result.getResponse().getContentAsString();

    mockMvc.perform(delete("/vending/product/remove/001/s001")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
