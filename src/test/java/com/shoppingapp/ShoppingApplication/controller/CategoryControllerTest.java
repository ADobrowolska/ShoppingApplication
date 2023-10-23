package com.shoppingapp.ShoppingApplication.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.shoppingapp.ShoppingApplication.model.Category;
import com.shoppingapp.ShoppingApplication.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    Category category1;
    Category category2;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
        category1 = createCategory("Pieczywo");
        category2 = createCategory("Napoje");
    }

    private Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return category;
    }

    @Test
    void getAllCategories() throws Exception {
        //given
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category1);
        categoryList.add(category2);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/category/x")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        //then

        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, Category.class);
        List<Category> receivedCategoryList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), collectionType);
        assertThat(receivedCategoryList).isNotNull();
        assertThat(receivedCategoryList.size()).isEqualTo(categoryList.size());
        assertThat(receivedCategoryList.get(0).getName()).isEqualTo(categoryList.get(0).getName());
    }

    @Test
    void getCategories() throws Exception {
        //given
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category1);
        categoryList.add(category2);
        String word = "pie";
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/category")
                        .param("searchBy", word))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        //then
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, Category.class);
        List<Category> receivedCategoryList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), collectionType);
        assertThat(receivedCategoryList).isNotNull();
        assertThat(receivedCategoryList.size()).isEqualTo(1);
        assertThat(receivedCategoryList.get(0).getName()).isEqualTo(category1.getName());
    }
}