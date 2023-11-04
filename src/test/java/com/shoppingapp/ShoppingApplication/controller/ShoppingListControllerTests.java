package com.shoppingapp.ShoppingApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingapp.ShoppingApplication.dto.shoppinglist.ShoppingListDTO;
import com.shoppingapp.ShoppingApplication.model.Category;
import com.shoppingapp.ShoppingApplication.model.Product;
import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import com.shoppingapp.ShoppingApplication.model.User;
import com.shoppingapp.ShoppingApplication.repository.CategoryRepository;
import com.shoppingapp.ShoppingApplication.repository.ProductRepository;
import com.shoppingapp.ShoppingApplication.repository.ShoppingListRepository;
import com.shoppingapp.ShoppingApplication.repository.UserRepository;
import com.shoppingapp.ShoppingApplication.service.ShoppingListService;
import com.shoppingapp.ShoppingApplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingListControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ShoppingListService shoppingListService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    Category category;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        shoppingListRepository.deleteAll();
        Category category = new Category();
        category.setName("Pieczywo");
        this.category = categoryRepository.save(category);
    }

    protected ShoppingList createShoppingList() throws InstanceAlreadyExistsException {
        ShoppingList newShoppingList = new ShoppingList();
        newShoppingList.setName("List1");
        newShoppingList.setUser(createUser());
        newShoppingList = shoppingListRepository.save(newShoppingList);
        Product product1 = createProduct(newShoppingList, "Bułka");
        Product product2 = createProduct(newShoppingList, "Chałka");
        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);
        newShoppingList.setProducts(productList);
        return shoppingListRepository.findById(newShoppingList.getId()).orElseThrow();
    }

    protected User createUser() throws InstanceAlreadyExistsException {
        User user = new User();
        user.setFirstName("Anna");
        user.setLastName("Nowak");
        user.setEmail("an@x.com");
        return userService.addUser(user);
    }

    protected Product createProduct(ShoppingList shoppingList, String name) {
        Product product = new Product();
        product.setShoppingList(shoppingList);
        product.setName(name);
        product.setCategory(category);
        productRepository.save(product);
        return product;
    }


    @Test
    void shouldGetSingleShoppingList() throws Exception {
        ShoppingList newShoppingList = createShoppingList();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user-id", String.valueOf(newShoppingList.getUser().getId()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/" + newShoppingList.getId()).headers(httpHeaders))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        ShoppingListDTO shoppingList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ShoppingListDTO.class);
        assertThat(shoppingList).isNotNull();
        assertThat(shoppingList.getId()).isEqualTo(newShoppingList.getId());
        assertThat(shoppingList.getName()).isEqualTo(newShoppingList.getName());
        assertThat(shoppingList.getProducts().size()).isEqualTo(2);
        assertThat(shoppingList.getUserId()).isEqualTo(newShoppingList.getUser().getId());
    }

    @Test
    void getShoppingList_NotFound() throws Exception {
        User user = createUser();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user-id", String.valueOf(user.getId()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/-1").headers(httpHeaders))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andReturn();
    }

    @Test
    void shouldGetShoppingLists() throws Exception {
        ShoppingList newShoppingList = createShoppingList();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        List<ShoppingListDTO> shoppingListDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertThat(shoppingListDTOs).isNotNull();
        assertThat(shoppingListDTOs.size()).isEqualTo(1);
    }

    @Test
    void shouldAddShoppingList() throws Exception {
        ShoppingList newShoppingList = new ShoppingList();
        User user = createUser();
        newShoppingList.setName("Dodana lista");
        newShoppingList.setUser(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user-id", String.valueOf(newShoppingList.getUser().getId()));

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/shopping")
                                .headers(httpHeaders)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newShoppingList))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        ShoppingListDTO shoppingList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ShoppingListDTO.class);
        assertThat(shoppingList).isNotNull();
        assertThat(shoppingList.getName()).isEqualTo(newShoppingList.getName());
        assertThat(shoppingList.getUserId()).isEqualTo(newShoppingList.getUser().getId());
    }

    @Test
    void shouldEditShoppingList() throws Exception {
        ShoppingList newShoppingList = createShoppingList();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/shopping/" + newShoppingList.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ShoppingList(newShoppingList.getId(), "Edited list", null, null, null))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        ShoppingListDTO shoppingList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ShoppingListDTO.class);
        assertThat(shoppingList.getName()).isEqualTo("Edited list");
    }


    @Test
    void shouldRemoveShoppingListById() throws Exception {
        ShoppingList shoppingList = createShoppingList();

        mockMvc.perform(MockMvcRequestBuilders.delete("/shopping/{id}", shoppingList.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void shouldRemoveShoppingLists() throws Exception {
        ShoppingList shoppingList = createShoppingList();

        mockMvc.perform(MockMvcRequestBuilders.delete("/shopping"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void shouldRemoveOldShoppingLists() throws Exception {
        ShoppingList shoppingList = createShoppingList();

        mockMvc.perform(MockMvcRequestBuilders.delete("/shopping/old"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200));
    }
}
