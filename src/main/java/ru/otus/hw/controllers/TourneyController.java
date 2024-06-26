package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.models.dto.*;
import ru.otus.hw.models.dto.TourneyType;
import ru.otus.hw.models.dto.Rank;
import ru.otus.hw.models.dto.Stage;
import ru.otus.hw.models.entities.*;
import ru.otus.hw.repositories.CompetitionRepository;
import ru.otus.hw.repositories.TourneyRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class TourneyController {
    private final TourneyRepository tourneyRepository;

    private final CompetitionRepository competitionRepository;

    @GetMapping("/tourney")
    public String listPage(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "competition_id", required = false) Long competitionId, Model model) {
        TourneyDto tourneyDto = null;
        String sReturn = "tourney_edit";
        if (competitionId != null && competitionId.longValue() > 0) {
            model.addAttribute("competition_id", competitionId);
        }
        if (id != null && !id.equals(0L) && id.longValue() > 0) {
            if (tourneyRepository.findById(id).isPresent()) {
               tourneyDto = tourneyRepository.findById(id).stream().map(TourneyDto::fromDomainObject).collect(Collectors.toList()).get(0);
            }
            if (tourneyDto != null) {
                List<CompetitionDto> competitionsDto = competitionRepository.findAll().stream().map(CompetitionDto::fromDomainObject).collect(Collectors.toList());
                tourneyDto.setCompetitions(competitionsDto);
                model.addAttribute("tourneyDto", tourneyDto);
            }
            return sReturn;
        } else if (id != null && id.equals(0L)) {
            tourneyDto = new TourneyDto(0L, "", TourneyType.Circle, Rank.Final_I, Stage.SubGroups, 0L);
            sReturn = "tourney_create";

            if (tourneyDto != null) {
                List<CompetitionDto> competitionsDto = competitionRepository.findAll().stream().map(CompetitionDto::fromDomainObject).collect(Collectors.toList());
                tourneyDto.setCompetitions(competitionsDto);
                model.addAttribute("tourneyDto", tourneyDto);
            }
        } else {
            List<Tourney> tourneys = tourneyRepository.findAll();
            if (competitionId != null && competitionId.longValue() > 0) {
                tourneys = tourneys.stream().filter(x->x.getCompetition().getId().equals(competitionId)).toList();
            }
            List<TourneyDto> tourneysDto = tourneys.stream().map(TourneyDto::fromDomainObject)
                    .collect(Collectors.toList());
            if (tourneysDto != null && !tourneysDto.isEmpty()) {
                for (TourneyDto tourneyDto1 : tourneysDto) {
                    CompetitionDto competitionDto = competitionRepository.findById(tourneyDto1.getCompetitionId()).map(CompetitionDto::fromDomainObject).get();
                    tourneyDto1.setCompetitions(Arrays.asList(competitionDto));
                }
            }
            model.addAttribute("tourneys", tourneysDto);
            sReturn = "tourneys";
        }
        return sReturn;
    }

    @DeleteMapping("/tourney/{id}")
    public String deleteTourney(@PathVariable("id") Long id, @RequestParam(value = "competition_id", required = false) Long competitionId, Model model) {
        if (id != null && !id.equals(0L)) {
            if (tourneyRepository.findById(id).isPresent()) {
                Tourney tourney = tourneyRepository.findById(id).get();
                tourneyRepository.delete(tourney);
            }
        }
        String s = competitionId != null && competitionId > 0 ? "?competition_id="+competitionId : "";
        return "redirect:/tourney" + s;
    }

    @PutMapping("/tourney/{id}")
    public String editTourney(@PathVariable("id") String id, @RequestParam(value = "competition_id", required = false) Long competitionId, @Valid @ModelAttribute("tourneyDto") TourneyDto tourneyDto,
                                  BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            if (tourneyDto.getId()!=null && !tourneyDto.getId().equals(0L) &&
                    id != null && id.equals(tourneyDto.getId().toString())) {
                tourneyRepository.save(tourneyDto.toDomainObject());
                String s = competitionId != null && competitionId > 0 ? "?competition_id="+competitionId : "";
                return "redirect:/tourney" + s;
            } else {
                List<CompetitionDto> competitionsDto = competitionRepository.findAll().stream().map(CompetitionDto::fromDomainObject).collect(Collectors.toList());
                tourneyDto.setCompetitions(competitionsDto);
                model.addAttribute("tourneyDto", tourneyDto);
                return "tourney_create";
            }
        } else {
            List<CompetitionDto> competitionsDto = competitionRepository.findAll().stream().map(CompetitionDto::fromDomainObject).collect(Collectors.toList());
            tourneyDto.setCompetitions(competitionsDto);
            model.addAttribute("tourneyDto", tourneyDto);
            return "tourney_edit";
        }
    }

    @PostMapping("/tourney")
    public String createTourney(@RequestParam(value = "competition_id", required = false) Long competitionId, @Valid @ModelAttribute("tourneyDto") TourneyDto tourneyDto,
                                    BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            if (tourneyDto.getId()==null || tourneyDto.getId().equals(0L)) {
                tourneyRepository.save(tourneyDto.toDomainObject());
            }
            String s = competitionId != null && competitionId > 0 ? "?competition_id="+competitionId : "";
            return "redirect:/tourney" + s;
        } else {
            List<CompetitionDto> competitionsDto = competitionRepository.findAll().stream().map(CompetitionDto::fromDomainObject).collect(Collectors.toList());
            tourneyDto.setCompetitions(competitionsDto);
            model.addAttribute("tourneyDto", tourneyDto);
            return "tourney_create";
        }
    }
}
