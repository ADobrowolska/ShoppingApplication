package com.shoppingapp.ShoppingApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingapp.ShoppingApplication.dto.user.CreateUserDTO;
import com.shoppingapp.ShoppingApplication.dto.user.UserDTO;
import com.shoppingapp.ShoppingApplication.model.User;
import com.shoppingapp.ShoppingApplication.repository.UserRepository;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

//    private User createUser() throws Exception {
//        User user = new User();
//        user.setFirstName("Anna");
//        user.setLastName("Nowak");
//        user.setEmail("an@x.com");
//        return userService.addUser(user);
//    }

    private User createUser(String firstName, String lastName, String email) throws Exception {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        return userService.addUser(user);
    }

    @Test
    void shouldGetUser() throws Exception {
        String firstName = UUID.randomUUID().toString();
        String lastName = UUID.randomUUID().toString();
        String email = "%s.%s@x.com".formatted(firstName, lastName);
        User newUser = createUser(firstName, lastName, email);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + newUser.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        UserDTO user = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(newUser.getId());
        assertThat(user.getFirstName()).isEqualTo(newUser.getFirstName());
    }

    @Test
    void getUser_NotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/-1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldAddUser() throws Exception {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setFirstName(UUID.randomUUID().toString());
        createUserDTO.setLastName(UUID.randomUUID().toString());
        createUserDTO.setEmail("%s@x.com".formatted(UUID.randomUUID().toString()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        UserDTO userDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        assertThat(userDTO).isNotNull();
        assertThat(userDTO.getFirstName()).isEqualTo(createUserDTO.getFirstName());
    }

    @Test
    void whenUserExists_thenBadRequest() throws Exception {
        String firstName = UUID.randomUUID().toString();
        String lastName = UUID.randomUUID().toString();
        String email = "%s.%s@x.com".formatted(firstName, lastName);
        User user = createUser(firstName, lastName, email);

        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setFirstName(user.getFirstName());
        createUserDTO.setLastName(user.getLastName());
        createUserDTO.setEmail(user.getEmail());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("User already exists!");
    }

    @Test
    void whenInvalidEmail_thenBadRequest() throws Exception {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail("annanowak");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Invalid email address!");
    }

}
