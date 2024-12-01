package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.hw.models.dto.PlayerDto;
import ru.otus.hw.models.entities.Player;
import ru.otus.hw.services.PlayerService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {
    @MockBean
    private PlayerService playerService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @WithMockUser(
            username = "user",
            password = "password",
            roles = {"USER"}
    )
    @Test
    @DisplayName("Список игроков")
    public void listPlayerGet() throws Exception {
        List<Player> players = new ArrayList<>();
        players.add(new PlayerDto(1L, "Test 1").toDomainObject());
        players.add(new PlayerDto(2L, "Test 2").toDomainObject());
        players.add(new PlayerDto(3L, "Test 3").toDomainObject());
        given(playerService.findAll()).willReturn(players);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/")
                        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).contains("1", "2", "3");
    }

    @WithMockUser(
            username = "admin",
            password = "password",
            roles = {"ADMIN"}
    )
    @Test
    @DisplayName("Удаление игрока")
    public void deletePlayerDelete() throws Exception {
        mockMvc.perform(delete("/player/1").with(csrf().asHeader()))
                .andExpect(status().isMovedTemporarily()).andDo(print());
    }

    @WithMockUser(
            username = "admin",
            password = "password",
            roles = {"ADMIN"}
    )
    @Test
    @DisplayName("Редактирование игрока")
    public void editPlayerPut() throws Exception {
        PlayerDto playerDto = new PlayerDto(3L, "Test");
        playerDto.setGender(true);
        mockMvc.perform(MockMvcRequestBuilders.put("/player/3")
                .param("id", playerDto.getId().toString())
                .param("title", playerDto.getName())
                .param("gender", playerDto.getGender().toString())
                .with(csrf().asHeader()))
            .andExpect(status().isOk()).andDo(print());
    }

    @WithMockUser(
            username = "admin",
            password = "password",
            roles = {"ADMIN"}
    )
    @Test
    @DisplayName("Создание игрока")
    public void createPlayerPost() throws Exception {
        PlayerDto playerDto = new PlayerDto(0L, "Test");
        playerDto.setGender(true);
        mockMvc.perform(MockMvcRequestBuilders.post("/player")
                        .param("title", playerDto.getName())
                        .param("gender", playerDto.getGender().toString())
                        .with(csrf().asHeader()))
                .andExpect(status().isOk()).andDo(print());
    }

}