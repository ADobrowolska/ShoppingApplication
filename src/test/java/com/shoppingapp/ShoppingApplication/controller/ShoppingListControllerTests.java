//package com.shoppingapp.ShoppingApplication.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.shoppingapp.ShoppingApplication.dto.shoppinglist.ShoppingListDTO;
//import com.shoppingapp.ShoppingApplication.model.Category;
//import com.shoppingapp.ShoppingApplication.model.Product;
//import com.shoppingapp.ShoppingApplication.model.ShoppingList;
//import com.shoppingapp.ShoppingApplication.repository.CategoryRepository;
//import com.shoppingapp.ShoppingApplication.repository.ProductRepository;
//import com.shoppingapp.ShoppingApplication.repository.ShoppingListRepository;
//import com.shoppingapp.ShoppingApplication.service.ShoppingListService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class ShoppingListControllerTests {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private ShoppingListRepository shoppingListRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private ShoppingListService shoppingListService;
//
//    Category category;
//
//    @BeforeEach
//    void setUp() {
//        productRepository.deleteAll();
//        categoryRepository.deleteAll();
//        shoppingListRepository.deleteAll();
//        Category category = new Category();
//        category.setName("Pieczywo");
//        this.category = categoryRepository.save(category);
//    }
//
//    protected ShoppingList createShoppingList() {
//        ShoppingList newShoppingList = new ShoppingList();
//        newShoppingList.setName("List1");
//        newShoppingList = shoppingListRepository.save(newShoppingList);
//        Product product1 = createProduct(newShoppingList,"Bułka");
//        Product product2 = createProduct(newShoppingList,"Chałka");
//        List<Product> productList = new ArrayList<>();
//        productList.add(product1);
//        productList.add(product2);
//        newShoppingList.setProducts(productList);
//        return shoppingListRepository.findById(newShoppingList.getId()).orElseThrow();
//    }
//
//    protected Product createProduct(ShoppingList shoppingList, String name) {
//        Product product = new Product();
//        product.setShoppingList(shoppingList);
//        product.setName(name);
//        product.setCategory(category);
//        productRepository.save(product);
//        return product;
//    }
//
//
//    @Test
//    void shouldGetSingleShoppingList() throws Exception {
//        //given
//        ShoppingList newShoppingList = createShoppingList();
//        //when
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/" + newShoppingList.getId()))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().is(200))
//                .andReturn();
//        //then
//        ShoppingListDTO shoppingList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ShoppingListDTO.class);
//        assertThat(shoppingList).isNotNull();
//        assertThat(shoppingList.getId()).isEqualTo(newShoppingList.getId());
//        assertThat(shoppingList.getName()).isEqualTo(newShoppingList.getName());
//        assertThat(shoppingList.getProducts().size()).isEqualTo(2);
//    }
//
//    @Test
//    void getShoppingList_NotFound() throws Exception {
//        //given
//        //when
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/-1"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().is(404))
//                .andReturn();
//        //then
//    }
//
//    @Test
//    void shouldGetShoppingLists() throws Exception {
//        //given
//        ShoppingList newShoppingList = createShoppingList();
//        //when
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().is(200))
//                .andReturn();
//        //then
//        List<ShoppingListDTO> shoppingListDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
//        assertThat(shoppingListDTOs).isNotNull();
//        assertThat(shoppingListDTOs.size()).isEqualTo(1);
//    }
//
//    @Test
//    void shouldAddShoppingList() throws Exception {
//        //given
//        ShoppingList shoppingList = new ShoppingList();
//        shoppingList.setName("Dodana lista");
//        //when
//        MvcResult mvcResult = mockMvc.perform(
//                MockMvcRequestBuilders.post("/shopping")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(shoppingList))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().is(200))
//                .andReturn();
//        //then
//        ShoppingList shoppingList1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ShoppingList.class);
//        assertThat(shoppingList1).isNotNull();
//        assertThat(shoppingList1.getName()).isEqualTo(shoppingList.getName());
//    }
//
//    @Test
//    void shouldEditShoppingList() throws Exception {
//        //given
//        ShoppingList shoppingList = createShoppingList();
//
//        //when
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/shopping/" + shoppingList.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new ShoppingList(shoppingList.getId(), "Edited list", null, null))))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().is(200))
//                .andReturn();
//        //then
//        ShoppingList shoppingList1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ShoppingList.class);
//        assertThat(shoppingList1.getName()).isEqualTo("Edited list");
//    };
//
//    @Test
//    void shouldRemoveShoppingListById() throws Exception {
//        //given
//        ShoppingList shoppingList = createShoppingList();
//        //when
//        mockMvc.perform(MockMvcRequestBuilders.delete("/shopping/{id}", shoppingList.getId()))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().is(200));
//    }
//
//    @Test
//    void shouldRemoveShoppingLists() throws Exception {
//        //given
//        ShoppingList shoppingList = createShoppingList();
//        //when
//        mockMvc.perform(MockMvcRequestBuilders.delete("/shopping"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().is(200));
//    }
//
//    @Test
//    void shouldRemoveOldShoppingLists() throws Exception {
//        //given
//        ShoppingList shoppingList = createShoppingList();
//        //when
//        mockMvc.perform(MockMvcRequestBuilders.delete("/shopping/old"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().is(200));
//    }
//
//
//
//
//
//}
