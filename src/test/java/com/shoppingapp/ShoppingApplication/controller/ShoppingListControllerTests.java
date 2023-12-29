package com.shoppingapp.ShoppingApplication.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingapp.ShoppingApplication.BaseTest;
import com.shoppingapp.ShoppingApplication.dto.shoppinglist.RequestShoppingListDTO;
import com.shoppingapp.ShoppingApplication.dto.shoppinglist.ShoppingListDTO;
import com.shoppingapp.ShoppingApplication.dto.shoppinglist.ShoppingListDTOMapper;
import com.shoppingapp.ShoppingApplication.model.Category;
import com.shoppingapp.ShoppingApplication.model.Product;
import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import com.shoppingapp.ShoppingApplication.model.User;
import com.shoppingapp.ShoppingApplication.repository.*;
import com.shoppingapp.ShoppingApplication.service.CategoryService;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingListControllerTests extends BaseTest {

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
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    Category category;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        this.category = createCategory("Pieczywo");
    }

    protected ShoppingList createShoppingList() throws InstanceAlreadyExistsException {
        ShoppingList newShoppingList = new ShoppingList();
        newShoppingList.setName("List1");
        newShoppingList.setUser(createUser());
        newShoppingList.setTimeOfLastEditing(Instant.parse("2022-11-11T15:55:33.061971700Z"));

        Product product1 = createProduct(newShoppingList, "Bułka");
        Product product2 = createProduct(newShoppingList, "Chałka");
        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);
        newShoppingList.setProducts(productList);
        return shoppingListRepository.save(newShoppingList);
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
        return product;
    }


    @Test
    void shouldGetSingleShoppingList() throws Exception {
        ShoppingList newShoppingList = createShoppingList();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user-id", String.valueOf(newShoppingList.getUser().getId()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/" + newShoppingList.getId())
                        .headers(httpHeaders))
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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user-id", String.valueOf(newShoppingList.getUser().getId()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping")
                        .headers(httpHeaders))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        List<ShoppingListDTO> shoppingListDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<ShoppingListDTO>>() {
        });
        assertThat(shoppingListDTOs).isNotEmpty();
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

        MvcResult postMvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/shopping")
                                .headers(httpHeaders)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newShoppingList))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        ShoppingListDTO receivedShoppingList = objectMapper.readValue(postMvcResult.getResponse().getContentAsString(), ShoppingListDTO.class);

        MvcResult getMvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/" + receivedShoppingList.getId())
                        .headers(httpHeaders))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        ShoppingListDTO fetchedShoppingList = objectMapper.readValue(getMvcResult.getResponse().getContentAsString(), ShoppingListDTO.class);

        assertThat(fetchedShoppingList).isNotNull();
        assertThat(fetchedShoppingList.getName()).isEqualTo(newShoppingList.getName());
        assertThat(fetchedShoppingList.getUserId()).isEqualTo(newShoppingList.getUser().getId());
    }

    @Test
    void shouldEditShoppingList() throws Exception {
        ShoppingList newShoppingList = createShoppingList();
        RequestShoppingListDTO shoppingListToEdit = ShoppingListDTOMapper.mapToCreateShoppingListDTO(newShoppingList);
        shoppingListToEdit.setName("Edited list");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user-id", String.valueOf(newShoppingList.getUser().getId()));

        MvcResult putMvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/shopping/" + newShoppingList.getId())
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shoppingListToEdit)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        ShoppingListDTO receivedShoppingList = objectMapper.readValue(putMvcResult.getResponse().getContentAsString(), ShoppingListDTO.class);

        MvcResult getMvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/" + receivedShoppingList.getId())
                        .headers(httpHeaders))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        ShoppingListDTO fetchedShoppingListDTO = objectMapper.readValue(getMvcResult.getResponse().getContentAsString(), ShoppingListDTO.class);

        assertThat(fetchedShoppingListDTO.getName()).isEqualTo("Edited list");
        assertThat(fetchedShoppingListDTO.getUserId()).isEqualTo(newShoppingList.getUser().getId());
    }


    @Test
    void shouldRemoveShoppingListById() throws Exception {
        ShoppingList shoppingList = createShoppingList();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user-id", String.valueOf(shoppingList.getUser().getId()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/shopping/{id}", shoppingList.getId())
                        .headers(httpHeaders))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200));

        mockMvc.perform(MockMvcRequestBuilders.get("/shopping/{id}", shoppingList.getId())
                        .headers(httpHeaders))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldRemoveShoppingLists() throws Exception {
        ShoppingList shoppingList = createShoppingList();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user-id", String.valueOf(shoppingList.getUser().getId()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/shopping")
                        .headers(httpHeaders))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping")
                        .headers(httpHeaders))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<ShoppingListDTO> shoppingListDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<ShoppingListDTO>>() {
        });
        assertThat(shoppingListDTOs).isEmpty();
    }

    @Test
    void shouldRemoveOldShoppingLists() throws Exception {
        ShoppingList shoppingList = createShoppingList();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user-id", String.valueOf(shoppingList.getUser().getId()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/shopping/old")
                        .headers(httpHeaders))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping")
                        .headers(httpHeaders))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<ShoppingListDTO> shoppingListDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<ShoppingListDTO>>() {
        });
        assertThat(shoppingListDTOs).isEmpty();
    }
}
