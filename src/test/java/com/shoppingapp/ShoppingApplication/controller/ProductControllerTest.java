package com.shoppingapp.ShoppingApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingapp.ShoppingApplication.dto.product.ProductDTO;
import com.shoppingapp.ShoppingApplication.model.Category;
import com.shoppingapp.ShoppingApplication.model.Product;
import com.shoppingapp.ShoppingApplication.model.ShoppingList;
import com.shoppingapp.ShoppingApplication.model.User;
import com.shoppingapp.ShoppingApplication.repository.CategoryRepository;
import com.shoppingapp.ShoppingApplication.repository.ProductRepository;
import com.shoppingapp.ShoppingApplication.repository.ShoppingListRepository;
import com.shoppingapp.ShoppingApplication.repository.UserRepository;
import com.shoppingapp.ShoppingApplication.service.ProductService;
import com.shoppingapp.ShoppingApplication.service.UserService;
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

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private UserService userService;


    Category category1;
    Category category2;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        shoppingListRepository.deleteAll();
        category1 = createCategory("Pieczywo");
        category2 = createCategory("Nabial");
    }

    protected ShoppingList createShoppingList() throws InstanceAlreadyExistsException {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setName("Lista zakupowa 1");
        shoppingList.setUser(createUser());
        shoppingListRepository.save(shoppingList);
        Product product1 = createProduct(shoppingList, category1, "Chleb");
        Product product2 = createProduct(shoppingList, category1, "Bagietka");
        Product product3 = createProduct(shoppingList, category2, "Serek topiony");
        shoppingList.setProducts(List.of(product1, product2, product3));
        return shoppingListRepository.findById(shoppingList.getId()).orElseThrow();
    }

    protected User createUser() throws InstanceAlreadyExistsException {
        User user = new User();
        user.setFirstName("Anna");
        user.setLastName("Nowak");
        user.setEmail("an@x.com");
        return userService.addUser(user);
    }

    protected Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return category;
    }

    protected Product createProduct(ShoppingList shoppingList, Category category, String name) {
        Product product = Product.builder()
                .name(name)
                .shoppingList(shoppingList)
                .category(category)
                .build();
        productRepository.save(product);
        return product;
    }

    @Test
    void shouldGetSingleProduct() throws Exception {
        //given
        ShoppingList shoppingList = createShoppingList();
        Product product = shoppingList.getProducts().get(0);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/" + shoppingList.getId() + "/products/" + product.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        //then
        ProductDTO newProduct = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProductDTO.class);
        assertThat(newProduct.getId()).isEqualTo(product.getId());
        assertThat(newProduct.getName()).isEqualTo(product.getName());
        assertThat(newProduct.getCategoryName()).isEqualTo(product.getCategory().getName());
    }

    @Test
    void shouldGetSingleProduct_NotFound() throws Exception {
        //given
        ShoppingList shoppingList = createShoppingList();
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/shopping/" + shoppingList.getId() + "/products/-1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldGetProductsOnShoppingList() throws Exception {
        //given
        ShoppingList shoppingList = createShoppingList();
        List<Product> products = shoppingList.getProducts();
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/shopping/{shoppingListId}/products", shoppingList.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        //then
        List<ProductDTO> newProducts = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertThat(newProducts).isNotNull();
        assertThat(newProducts.size()).isEqualTo(products.size());
//        assertThat(newProducts.get(0).getName()).isEqualTo(products.get(0).getName());
    }

    @Test
    void shouldAddProductToShoppingListExistedCategory() throws Exception {
        //given
        ShoppingList shoppingList = createShoppingList();
        Product newProduct = new Product();
        newProduct.setName("Jogurt");
        newProduct.setCategory(category2);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/shopping/" + shoppingList.getId() + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        //then
        ProductDTO receivedProduct = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProductDTO.class);
        assertThat(receivedProduct).isNotNull();
        assertThat(receivedProduct.getName()).isEqualTo(newProduct.getName());
        assertThat(receivedProduct.getCategoryName()).isEqualTo(newProduct.getCategory().getName());
    }

    @Test
    void shouldAddProductToShoppingListNonExistentCategory() throws Exception {
        //given
        ShoppingList shoppingList = createShoppingList();
        Product newProduct = new Product();
        newProduct.setName("Jablko");
        Category category3 = new Category();
        category3.setName("Owoce");
        categoryRepository.save(category3);
        newProduct.setCategory(category3);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/shopping/" + shoppingList.getId() + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        //then
        ProductDTO receivedProduct = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProductDTO.class);
        assertThat(receivedProduct).isNotNull();
        assertThat(receivedProduct.getName()).isEqualTo(newProduct.getName());
    }

    @Test
    void shouldEditProduct() throws Exception {
        //given
        ShoppingList shoppingList = createShoppingList();
        Product toEdit = shoppingList.getProducts().get(0);
        toEdit.setName("Grahamka");
        toEdit.setCategory(category1);

//        Product toEdit2 = new Product();
//        toEdit2.setId(toEdit.getId());
//        toEdit2.setName("Grahamka");
//        toEdit2.setCategory(category1);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/shopping/" + shoppingList.getId() + "/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toEdit)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        //then
        ProductDTO receivedProduct = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProductDTO.class);
        assertThat(receivedProduct.getName()).isEqualTo(toEdit.getName());
    }

    @Test
    void shouldRemoveProduct() throws Exception {
        //given
        ShoppingList shoppingList = createShoppingList();
        Product product = shoppingList.getProducts().get(0);
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/shopping/" + shoppingList.getId() + "/products/" + product.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200));
    }
}