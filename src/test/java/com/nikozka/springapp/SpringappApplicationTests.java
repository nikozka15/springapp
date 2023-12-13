package com.nikozka.springapp;

import com.nikozka.springapp.entity.UserEntity;
import com.nikozka.springapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = App.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class SpringappApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    private final String iin = "3483310183";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void whenThereAreNoUsersThenReturnEmptyResponseWith200() throws Exception {
        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    void whenThereAreUsersThenReturnResponseWith200() throws Exception {
        userRepository.save(getEntity());
        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getAllUsersJsonResponse()));
    }

    @Test
    void whenThereAreNoUserThenReturnEmptyResponseWith404() throws Exception {
        mvc.perform(get("/users/{iin}", iin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void whenUserIsFoundThenReturnResponseWith200() throws Exception {
        userRepository.save(getEntity());
        mvc.perform(get("/users/{iin}", iin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getUserJson()));
    }

    @Test
    void whenIinIsInvalidInPostThenReturn422() throws Exception {
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createInvalidUserRequest()))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void whenIinIsValidButUserExistInPostThenReturn409() throws Exception {
        userRepository.save(getEntity());
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUsersRequest()))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void whenBadRequestInPostThenReturn400() throws Exception {
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserBadRequest()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void whenUserCreatedInPostThenReturn201() throws Exception {
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUsersRequest()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void whenUserUpdatedInPutThenReturn204() throws Exception {
        userRepository.save(getEntity());
        mvc.perform(put("/users/{iin}", iin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUsersRequest()))  // tdo nit correct work
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void whenBadRequestInPutThenReturn400() throws Exception {
        mvc.perform(put("/users/{iin}", iin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUpdateUserBadRequest()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void whenUserNotFoundInPutThenReturn404() throws Exception {
        mvc.perform(put("/users/{iin}", iin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUpdateUsersRequest()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void whenInvalidUserInPutThenReturn422() throws Exception {
        mvc.perform(put("/users/{iin}", iin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createInvalidUpdateUserRequest()))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void whenUserNotFoundInDeleteThenReturn404() throws Exception {
        mvc.perform(delete("/users/{iin}", iin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void whenUserFoundInDeleteThenReturn204() throws Exception {
        userRepository.save(getEntity());
        mvc.perform(delete("/users/{iin}", iin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private byte[] createUserBadRequest() {
        return getUserJsonBad().getBytes();
    }

    private byte[] createUpdateUserBadRequest() {
        return getUpdateUserJsonBad().getBytes();
    }

    private byte[] createUsersRequest() {
        return getUserJson().getBytes();
    }

    private byte[] createUpdateUsersRequest() {
        return getUpdateUserJson().getBytes();
    }

    private byte[] createInvalidUserRequest() {
        return getInvalidUserJson().getBytes();
    }

    private byte[] createInvalidUpdateUserRequest() {
        return getInvalidUpdateUserJson().getBytes();
    }

    private String getAllUsersJsonResponse() {
        return "[" + getUserJson() + "]";
    }

    private static String getUserJson() {
        return "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"iin\":\"3483310183\"}";
    }

    private static String getUpdateUserJson() {
        return "{\"firstName\":\"John\",\"lastName\":\"Doe\"}";
    }

    private static String getUserJsonBad() {
        return "{\"firstName\":\"John\",\"lastName\":\"Doe\",}";
    }

    private static String getUpdateUserJsonBad() {
        return "{\"firstName\":\"John\",}";
    }

    private static String getInvalidUserJson() {
        return "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"iin\":\"3483310180\"}";
    }

    private static String getInvalidUpdateUserJson() {
        return "{\"firstName\":\"J\",\"lastName\":\"Doe\"}";
    }

    private UserEntity getEntity() {
        UserEntity userEntity = new UserEntity("John", "Doe", iin);
        userEntity.setId(1L);
        return userEntity;
    }
}
