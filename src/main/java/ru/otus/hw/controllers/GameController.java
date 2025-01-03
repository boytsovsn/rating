package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.calculation.RatingCalculator;
import ru.otus.hw.exceptions.RatingCalculationException;
import ru.otus.hw.models.dto.GameDto;
import ru.otus.hw.models.dto.GameResult;
import ru.otus.hw.models.dto.GameResultDto;
import ru.otus.hw.models.dto.GameResultsDto;
import ru.otus.hw.models.entities.*;
import ru.otus.hw.repositories.CompetitionRepository;
import ru.otus.hw.repositories.GameRepository;
import ru.otus.hw.repositories.RatingRepository;
import ru.otus.hw.repositories.TourneyPlayerRepository;
import ru.otus.hw.services.GameService;
import ru.otus.hw.services.RatingService;
import ru.otus.hw.services.TourneyPlayerService;
import ru.otus.hw.services.TourneyService;

import java.sql.Date;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final CompetitionRepository competitionRepository;

    private final TourneyPlayerService tourneyPlayerService;

    private final TourneyPlayerRepository tourneyPlayerRepository;

    private final TourneyService tourneyService;

    private final GameService gameService;

    private final GameRepository gameRepository;

    private final RatingService ratingService;

    private final RatingRepository ratingRepository;

    private List<Game> generateAllGames(List<TourneyPlayer> tourneyPlayers) {

        List<Game> games = new ArrayList<>();
        for (int tour=1; gameService.checkTourRange(tour, tourneyPlayers.size()); tour++) {
            List<Game> tourGames = gameService.generateByTourneyPlayers(tourneyPlayers, tour, null);
            games.addAll(tourGames);
        }
        return games;
    }

    private Float meanTourneyRating(List<TourneyPlayer> tourneyPlayers) {
        Float mean = 0F;
        for (TourneyPlayer tourneyPlayer: tourneyPlayers) {
            mean += tourneyPlayer.getRatingCurrent();
        }
        return tourneyPlayers.size() > 0 ? mean / tourneyPlayers.size() : 0F;
    }

    private Boolean foundSavedRating(List<TourneyPlayer> tourneyPlayers) {
        Boolean foundSavedRating = false;
        for (TourneyPlayer tourneyPlayer: tourneyPlayers) {
            if (tourneyPlayer.getRatingOut()>0F) {
                foundSavedRating = true;
                break;
            }
        }
        return foundSavedRating;
    }

    @GetMapping("/game")
    public String listPage(@RequestParam(value = "competition_id", required = false) Long competitionId
            , @RequestParam(value = "tourney_id", required = false) Long tourneyId
            , Model model) {
        model.addAttribute("tourney_id", tourneyId);
        model.addAttribute("competition_id", competitionId);
        Competition competition = competitionRepository.findById(competitionId).get();
        Tourney tourney = tourneyService.findById(tourneyId).get();
        model.addAttribute("competition_title", competition.getTitle());
        model.addAttribute("tourney_title", tourney.getTitle());
        List<TourneyPlayer> tourneyPlayers = tourneyPlayerService.findPlayersByTourney(tourney.getId());
        List<Game> games = generateAllGames(tourneyPlayers);
        List<GameDto> gamesDto = games.stream().map(GameDto::fromDomainObject).toList();
        model.addAttribute("games", gamesDto);
        List<String> gameResults = gamesDto.stream().map(x->{return /*GameResult.NO_RESULT.name()*/"";}).toList();
        Map<Long, Set<GameResultDto>> gameResultSets = new LinkedHashMap<Long, Set<GameResultDto>>();
        Long i = 0L;
        for (GameDto gameDto: gamesDto) {
            Set<GameResultDto> gameResultSet = new LinkedHashSet<GameResultDto>();
            for (GameResult gameResult : GameResult.values()) {
                GameResultDto gameResultDto = new GameResultDto(gameDto.getId(), gameResult);
                gameResultSet.add(gameResultDto);
            }
            gameResultSets.put(i, gameResultSet);
            i++;
        }
        GameResultsDto gameResultsDto = new GameResultsDto(gameResults, gameResultSets);
        model.addAttribute("games_result", gameResultsDto);
        return "games";
    }

    @PostMapping("/game")
    public String saveGameRatings(
            @RequestParam(value = "competition_id", required = false) Long competitionId
            , @RequestParam(value = "tourney_id", required = false) Long tourneyId
            , Model model) {
        Tourney tourney = tourneyService.findById(tourneyId).get();
        List<TourneyPlayer> tourneyPlayers = tourneyPlayerService.findPlayersByTourney(tourney.getId());
        if (foundSavedRating(tourneyPlayers)) {
            throw new RatingCalculationException("The tournament rating has already been saved!  Contact the administrator!");
        }
        List<Game> games = generateAllGames(tourneyPlayers);
        for (Game game:games) {
            if (game.getResult() == GameResult.NO_RESULT.ordinal()) {
                throw new RatingCalculationException("Not all game results are given!");
            }
        }
        tourneyPlayers = tourneyPlayerService.findPlayersByTourney(tourney.getId());
        for (TourneyPlayer tourneyPlayer: tourneyPlayers) {
            tourneyPlayer.setRatingOut(tourneyPlayer.getRatingCurrent());
        }
        tourneyPlayerRepository.saveAll(tourneyPlayers);
        // TODO: Далее код под вопросом, рейтинг сохраняется на текущую дату.
        // Но поскольку для расчёта мы брали последний рейтинг, то и заносить
        // новый рейтинг нужно туда же, хотя правильней ориентироваться на дату соревнования.
        for (TourneyPlayer tourneyPlayer: tourneyPlayers) {
            Float newRating = tourneyPlayer.getRatingOut();
            Calendar calendar = GregorianCalendar.getInstance();
            Date date = new Date(calendar.getTimeInMillis());
            Rating lastRating = ratingService.findLastByPlayerId(tourneyPlayer.getPlayer().getId());
            if (date.getYear()+1900 == lastRating.getYear()
                    && date.getMonth()+1 == lastRating.getMonth()
                    && calendar.get(Calendar.WEEK_OF_MONTH) == lastRating.getWeek()) {
                lastRating.setRatingCurrent(newRating);
                ratingRepository.save(lastRating);
            } else {
                ratingService.save(0L, date.getYear()+1900, date.getMonth()+1, calendar.get(Calendar.WEEK_OF_MONTH), lastRating.getRatingOut(), newRating, 0F, tourneyPlayer.getPlayer().getId());
            }
        }
        String s = tourneyId != null && tourneyId > 0 ? "?tourney_id="+tourneyId : "";
        String razdelitel = s == null || s.isEmpty() ? "?" : "&";
        s = s + (competitionId != null && competitionId > 0 ? razdelitel +"competition_id="+competitionId : "");
        return "redirect:/tourneyplayer"+s;
    }


    boolean resultInTwoStrings(String gameResultString1, String gameResultString2) {
        return (gameResultString2 != null
                && gameResultString1.startsWith("GameResultDto(gameId=")
                && gameResultString2.startsWith("selResult="));
    }

    GameResultDto parseGameResultString(String gameResultString1, String gameResultString2)
    {
        String gameResultString = gameResultString1;
        if (resultInTwoStrings(gameResultString1, gameResultString2)) {
            // Случай для одной игры: строка с результатом игры распадается на две строки, поэтому соединяем их
            gameResultString = gameResultString1+","+gameResultString2;
        }
        String[] params = gameResultString.replace("(", "").replace(")", "").split(",");
        List<String> values = new ArrayList<String>();
        for (String param : params) {
            String[] svalues = param.split("=");
            if (svalues.length > 1 && svalues[1] != null) {
                values.add(svalues[1].trim());
            }
        }
        if (values.size() != 2)
            throw new RatingCalculationException("Incorrect game result:  %s".formatted(gameResultString));
        Long id = Long.valueOf(values.get(0));
        GameResult gameResult = GameResult.getValueByName(values.get(1));
        GameResultDto gameResultDto = new GameResultDto(id, gameResult);
        return gameResultDto;
    }

    @PutMapping("/game")
    public String addResultGames(
              @RequestParam(value = "competition_id", required = false) Long competitionId
            , @RequestParam(value = "tourney_id", required = false) Long tourneyId
            , @ModelAttribute("games_result") GameResultsDto gameResultsDto
            , Model model) {
        Tourney tourney = tourneyService.findById(tourneyId).get();
        List<TourneyPlayer> tourneyPlayers = tourneyPlayerService.findPlayersByTourney(tourney.getId());
        List<Game> games = generateAllGames(tourneyPlayers);
        Float meanRating = meanTourneyRating(tourneyPlayers);
        Float coeff = RatingCalculator.tourneyCoefficient (meanRating);
        if (foundSavedRating(tourneyPlayers)) {
            throw new RatingCalculationException("The tournament rating has already been saved!  Contact the administrator!");
        }
        if (gameResultsDto.getSelectedResults() == null) {
            throw new RatingCalculationException("Nothing to count on!");
        }

        for (int k=0; k<gameResultsDto.getSelectedResults().size(); k++) {
            String gameResultString = gameResultsDto.getSelectedResults().get(k);
            String nextString = k+1<gameResultsDto.getSelectedResults().size()?gameResultsDto.getSelectedResults().get(k+1):null;
            GameResultDto gameResultDto = parseGameResultString(gameResultString, nextString);
            if (resultInTwoStrings(gameResultString, nextString)) {
                k++;
            }
            final Long i = gameResultDto.getGameId();
            if (games != null && games.stream().filter(x -> x.getId() == i).findFirst().isPresent()) {
                Game game = games.stream().filter(x -> x.getId() == i).findFirst().get();
                game.getTourneyPlayer1().setRatingCurrent(0F);
                game.getTourneyPlayer2().setRatingCurrent(0F);
                tourneyPlayerRepository.save(game.getTourneyPlayer1());
                tourneyPlayerRepository.save(game.getTourneyPlayer2());
                gameRepository.save(game);
            }
        }
        for (int k=0; k<gameResultsDto.getSelectedResults().size(); k++) {
            String gameResultString = gameResultsDto.getSelectedResults().get(k);
            String nextString = k+1<gameResultsDto.getSelectedResults().size()?gameResultsDto.getSelectedResults().get(k+1):null;
            GameResultDto gameResultDto = parseGameResultString(gameResultString, nextString);
            if (resultInTwoStrings(gameResultString, nextString)) {
                k++;
            }
            if (!gameResultDto.getSelResult().equals(GameResult.NO_RESULT)) {
                final Long i = gameResultDto.getGameId();
                if (games != null && games.stream().filter(x->x.getId()==i).findFirst().isPresent()) {
                    Game game = games.stream().filter(x->x.getId()==i).findFirst().get();
                    game.setResult(gameResultDto.getSelResult().ordinal());
                    TourneyPlayer tourneyPlayer1 = tourneyPlayerRepository.findById(game.getTourneyPlayer1().getId()).orElseThrow(()->new RatingCalculationException("Not found the first player for game: %s".formatted(game.toString())));
                    TourneyPlayer tourneyPlayer2 = tourneyPlayerRepository.findById(game.getTourneyPlayer2().getId()).orElseThrow(()->new RatingCalculationException("Not found the second player for game: %s".formatted(game.toString())));
                    game.setTourneyPlayer1(tourneyPlayer1);
                    game.setTourneyPlayer2(tourneyPlayer2);
                    if (game.getTour()==1) {
                        game.setRating1(game.getTourneyPlayer1().getRatingIn());
                        game.setRating2(game.getTourneyPlayer2().getRatingIn());
                    } else {
                        if (game.getTourneyPlayer1().getRatingCurrent() != 0F) {
                            game.setRating1(game.getTourneyPlayer1().getRatingCurrent());
                        } else {
                            game.setRating1(game.getTourneyPlayer1().getRatingIn());
                        }
                        if (game.getTourneyPlayer2().getRatingCurrent() != 0F) {
                            game.setRating2(game.getTourneyPlayer2().getRatingCurrent());
                        } else {
                            game.setRating2(game.getTourneyPlayer2().getRatingIn());
                        }
                    }
                    if (gameResultDto.getSelResult().equals(GameResult.FIRST_WIN)) {
                        game.getTourneyPlayer1().setRatingCurrent(RatingCalculator.winnerRating(game.getRating1(), game.getRating2(),coeff));
                        game.getTourneyPlayer2().setRatingCurrent(RatingCalculator.loserRating(game.getRating1(), game.getRating2(),coeff));
                    } else if (gameResultDto.getSelResult().equals(GameResult.SECOND_WIN)) {
                        game.getTourneyPlayer2().setRatingCurrent(RatingCalculator.winnerRating(game.getRating2(), game.getRating1(),coeff));
                        game.getTourneyPlayer1().setRatingCurrent(RatingCalculator.loserRating(game.getRating2(), game.getRating1(),coeff));
                    }
                    tourneyPlayerRepository.save(game.getTourneyPlayer1());
                    tourneyPlayerRepository.save(game.getTourneyPlayer2());
                    gameRepository.save(game);
                } else {
                    throw new RatingCalculationException("Invalid number of games in the tournament!");
               }
           } else {
               throw new RatingCalculationException("Not all game results are given!");
           }
        }
        String s = tourneyId != null && tourneyId > 0 ? "?tourney_id="+tourneyId : "";
        String razdelitel = s == null || s.isEmpty() ? "?" : "&";
        s = s + (competitionId != null && competitionId > 0 ? razdelitel +"competition_id="+competitionId : "");
        return "redirect:/game"+s;
    }
}
