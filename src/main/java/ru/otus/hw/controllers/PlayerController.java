package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.models.dto.PlayerDto;
import ru.otus.hw.models.entities.Player;
import ru.otus.hw.services.PlayerService;

import java.sql.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/")
    public String listPage(Model model) {
        List<Player> players = playerService.findAll();
        model.addAttribute("players", players);
        return "list";
    }

    @GetMapping("/player")
    public String editPage(@RequestParam("id") Long id, Model model) {
        PlayerDto playerDto = null;
        String sReturn = "edit";
        if (id != null && !id.equals(0L)) {
            Player player = playerService.findById(id);
            if (player != null) {
                playerDto = new PlayerDto(player.getId(), player.getName(), player.getBirthPlace(), player.getBirthDate(), player.getLocation(), player.getGender() == 1, player.getRatingCurrent(), null);
            }
        } else {
            playerDto = new PlayerDto(0L, "", "", new Date(90, 00, 01), "", true, 0F, null);
            sReturn = "create";
        }
        if (playerDto != null) {
            model.addAttribute("playerDto", playerDto);
        }
        return sReturn;
    }

    @DeleteMapping("/player/{id}")
    public String deletePlayer(@PathVariable("id") Long id, Model model) {
        if (id != null && !id.equals(0L)) {
            Player player = playerService.findById(id);
            playerService.delete(player);
        }
        return "redirect:/";
    }

    @PutMapping("/player/{id}")
    public String editPlayer(@PathVariable("id") String id, @Valid @ModelAttribute("playerDto") PlayerDto playerDto,
                           BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            if (playerDto.getId()!=null && !playerDto.getId().equals(0L) &&
                id != null && id.equals(playerDto.getId().toString())) {
                playerService.update(playerDto.toDomainObject());
                return "redirect:/";
            } else {
                return "create";
            }
        } else {
            return "edit";
        }
    }

    @PostMapping("/player")
    public String createPlayer(@Valid @ModelAttribute("playerDto") PlayerDto playerDto,
                             BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            if (playerDto.getId()==null || playerDto.getId().equals(0L)) {
                playerService.insert(playerDto.getName(), playerDto.getBirthPlace(), playerDto.getBirthDate(), playerDto.getLocation(), playerDto.getGender() ? 1 : 0);
            }
            return "redirect:/";
        } else {
           return "create";
        }
    }
}

