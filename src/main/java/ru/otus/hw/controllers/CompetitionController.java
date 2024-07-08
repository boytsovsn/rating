package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.models.dto.CompetitionDto;
import ru.otus.hw.models.entities.Competition;
import ru.otus.hw.repositories.CompetitionRepository;

import java.sql.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionRepository competitionRepository;

    @GetMapping("/competition")
    public String listPage(@RequestParam(value = "id", required = false) Long id, Model model) {
        CompetitionDto competitionDto = null;
        String sReturn = "competition_edit";
        if (id != null && !id.equals(0L) && id.longValue() > 0) {
            if (competitionRepository.findById(id).isPresent()) {
                Competition competition = competitionRepository.findById(id).get();
                competitionDto = new CompetitionDto(competition.getId(), competition.getTitle(), competition.getDate(), competition.getLocation());
            }
            if (competitionDto != null) {
                model.addAttribute("competitionDto", competitionDto);
            }
            return sReturn;
        } else if (id != null && id.equals(0L)) {
            competitionDto = new CompetitionDto(0L, "", new Date(90, 00, 01), "");
            sReturn = "competition_create";
            if (competitionDto != null) {
                model.addAttribute("competitionDto", competitionDto);
            }
        } else {
            List<Competition> competitions = competitionRepository.findAll();
            model.addAttribute("competitions", competitions);
            sReturn = "competitions";
        }
        return sReturn;
    }

    @DeleteMapping("/competition/{id}")
    public String deleteCompetition(@PathVariable("id") Long id, Model model) {
        if (id != null && !id.equals(0L)) {
            if (competitionRepository.findById(id).isPresent()) {
                Competition competition = competitionRepository.findById(id).get();
                competitionRepository.delete(competition);
            }
        }
        return "redirect:/competition";
    }

    @PutMapping("/competition/{id}")
    public String editCompetition(@PathVariable("id") String id, @Valid @ModelAttribute("competitionDto") CompetitionDto competitionDto,
                                  BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            if (competitionDto.getId() != null && !competitionDto.getId().equals(0L) &&
                    id != null && id.equals(competitionDto.getId().toString())) {
                competitionRepository.save(competitionDto.toDomainObject());
                return "redirect:/competition";
            } else {
                return "competition_create";
            }
        } else {
            return "competition_edit";
        }
    }

    @PostMapping("/competition")
    public String createCompetition(@Valid @ModelAttribute("competitionDto") CompetitionDto competitionDto,
                                    BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            if (competitionDto.getId() == null || competitionDto.getId().equals(0L)) {
                competitionRepository.save(competitionDto.toDomainObject());
            }
            return "redirect:/competition";
        } else {
            return "competition_create";
        }
    }
}
