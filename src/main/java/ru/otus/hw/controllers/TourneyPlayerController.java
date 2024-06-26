package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.exceptions.RatingCalculationException;
import ru.otus.hw.models.dto.CheckedPlayersDto;
import ru.otus.hw.models.dto.PlayerDto;
import ru.otus.hw.models.dto.TourneyPlayerDto;
import ru.otus.hw.models.dto.TourneyType;
import ru.otus.hw.models.entities.*;
import ru.otus.hw.repositories.CompetitionRepository;
import ru.otus.hw.repositories.TourneyPlayerRepository;
import ru.otus.hw.services.PlayerService;
import ru.otus.hw.services.RatingService;
import ru.otus.hw.services.TourneyPlayerService;
import ru.otus.hw.services.TourneyService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TourneyPlayerController {

    private final CompetitionRepository competitionRepository;
    private final TourneyPlayerRepository tourneyPlayerRepository;

    private final TourneyPlayerService tourneyPlayerService;

    private final RatingService ratingService;

    private final PlayerService playerService;

    private final TourneyService tourneyService;

    @GetMapping("/tourneyplayer")
    public String listPage(@RequestParam(value = "competition_id", required = false) Long competitionId
            , @RequestParam(value = "tourney_id", required = false) Long tourneyId
            , Model model) {
        model.addAttribute("tourney_id", tourneyId);
        model.addAttribute("competition_id", competitionId);
        Competition competition = competitionRepository.findById(competitionId).get();
        Tourney tourney = tourneyService.findById(tourneyId).get();
        model.addAttribute("competition_title", competition.getTitle());
        model.addAttribute("tourney_title", tourney.getTitle());
        model.addAttribute("tourney_type", TourneyType.getValueById(tourney.getType()));
        List<Player> players = playerService.findAll();
        List<PlayerDto> playersDto = players.stream().map(PlayerDto::fromDomainObject).toList();
        model.addAttribute("players", playersDto);
        CheckedPlayersDto checkedPlayersDto = new CheckedPlayersDto(new ArrayList<>(playersDto.size()));
        model.addAttribute("checkedPlayersDto", checkedPlayersDto);
        List<TourneyPlayer> tourneyPlayers = tourneyPlayerService.findPlayersByTourney(tourneyId);
        List<TourneyPlayerDto> tourneyPlayersDto = tourneyPlayers.stream().map(TourneyPlayerDto::fromDomainObject).toList();
        model.addAttribute("tourneyPlayers", tourneyPlayersDto);
        return "tourney_players";
    }

    @DeleteMapping("/tourneyplayer/{id}")
    public String deleteTourneyPlayer(@PathVariable("id") Long id
            , @RequestParam(value = "competition_id", required = false) Long competitionId
            , @RequestParam(value = "tourney_id", required = false) Long tourneyId
            , Model model) {
        if (id != null && !id.equals(0L)) {
            if (tourneyPlayerRepository.findById(id).isPresent()) {
                TourneyPlayer tourneyPlayer = tourneyPlayerRepository.findById(id).get();
                if (tourneyPlayer.getRatingOut()>0F) {
                    throw new RatingCalculationException("The player's rating has already been saved. He cannot be removed from the tournament. Contact your server administrator.");
                }
                tourneyPlayerRepository.delete(tourneyPlayer);
            }
        }
        String s = tourneyId != null && tourneyId > 0 ? "?tourney_id="+tourneyId : "";
        String razdelitel = s == null || s.isEmpty() ? "?" : "&";
        s = s + (competitionId != null && competitionId > 0 ? razdelitel +"competition_id="+competitionId : "");
        return "redirect:/tourneyplayer" + s;
    }

    @PostMapping("/tourneyplayer")
    public String addTourneyPlayer(@RequestParam(value = "competition_id", required = false) Long competitionId
            , @RequestParam(value = "tourney_id", required = false) Long tourneyId
            , @ModelAttribute("checkedPlayersDto") CheckedPlayersDto checkedPlayers
            , Model model) {
        if (checkedPlayers.getCheckedPlayers() != null && !checkedPlayers.getCheckedPlayers().isEmpty()) {
            Set<Long> checkedPlayerIds = new HashSet<>();
            for (String p : checkedPlayers.getCheckedPlayers()) {
//                log.info("addTourneyPlayer: {}", p);
                Long id = Long.valueOf(p);
                checkedPlayerIds.add(id);
            }
            List<TourneyPlayer> tourneyPlayers = tourneyPlayerService.findPlayersByTourney(tourneyId);
            Set<Long> tourneyPlayersIds = tourneyPlayers.stream().map(x->x.getPlayer().getId()).collect(Collectors.toSet());
            checkedPlayerIds.removeAll(tourneyPlayersIds);
            for (Long id: checkedPlayerIds) {
                Player player = playerService.findById(id);
                Rating rating = ratingService.findLastByPlayerId(id);
                Tourney tourney = tourneyService.findById(tourneyId).get();
                TourneyPlayer tourneyPlayer = new TourneyPlayer(0L, tourney, player, rating.getRatingCurrent(), rating.getRatingCurrent(), 0F, null, null);
                tourneyPlayerRepository.save(tourneyPlayer);
            }
        }
        String s = tourneyId != null && tourneyId > 0 ? "?tourney_id="+tourneyId : "";
        String razdelitel = s == null || s.isEmpty() ? "?" : "&";
        s = s + (competitionId != null && competitionId > 0 ? razdelitel +"competition_id="+competitionId : "");
        return "redirect:/tourneyplayer"+s;
    }
}
