package ru.otus.hw.calculation;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.exceptions.TourGamesGenerationException;
import ru.otus.hw.models.entities.Game;
import ru.otus.hw.models.entities.TourneyPlayer;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class TourGameGeneratorCircle implements TourGameGenerator {

    //Туры рекомендованные ETTU
    static private int [][][] tours_1_2 = {{{1, 2}}};

    static private int [][][] tours_3_4 = {{{1, 3}, {2, 4}},
                                           {{4, 1}, {3, 2}},
                                           {{1, 2}, {3, 4}}};

    static private int [][][] tours_5_6 = {{{2, 4}, {1, 5}, {3, 6}},
                                           {{4, 1}, {6, 2}, {5, 3}},
                                           {{1, 3}, {2, 5}, {4, 6}},
                                           {{3, 2}, {6, 1}, {5, 4}},
                                           {{1, 2}, {3, 4}, {5, 6}}};

    static private int [][][] tours_7_8 = {{{2, 6}, {3, 5}, {1, 7}, {4, 8}},
                                           {{5, 2}, {6, 1}, {8, 3}, {7, 4}},
                                           {{1, 5}, {2, 8}, {4, 6}, {3, 7}},
                                           {{8, 1}, {5, 4}, {7, 2}, {6, 3}},
                                           {{1, 3}, {2, 4}, {5, 7}, {6, 8}},
                                           {{4, 1}, {3, 2}, {7, 6}, {8, 5}},
                                           {{1, 2}, {3, 4}, {5, 6}, {7, 8}}};

    static private int [][][] tours_9_10 ={{{3,7},  {2,8},  {4,6},  {1,9},  {5,10}},
                                           {{7,2},  {6,3},  {8,1},  {10,4}, {9,5}},
                                           {{2,6},  {1,7},  {3,10}, {5,8},  {4,9}},
                                           {{6,1},  {10,2}, {7,5},  {9,3},  {8,4}},
                                           {{2,4},  {1,5},  {3,8},  {6,10}, {7,9}},
                                           {{4,1},  {5,3},  {9,2},  {8,6},  {10,7}},
                                           {{1,3},  {2,5},  {4,7},  {6,9},  {8,10}},
                                           {{3,2},  {5,4},  {10,1}, {7,6},  {9,8}},
                                           {{1,2},  {3,4},  {5,6},  {7,8},  {9,10}}};

    static private int [][][] tours_11_12={{{3,9},  {4,8},  {2,10}, {5,7},  {1,11},  {6,12}},
                                           {{8,3},  {9,2},  {7,4},  {10,1}, {12,5},  {11,6}},
                                           {{2,8},  {3,7},  {1,9},  {4,12}, {6,10},  {5,11}},
                                           {{7,2},  {8,1},  {12,3}, {9,6},  {11,4},  {10,5}},
                                           {{1,7},  {2,12}, {6,8},  {3,11}, {5,9},   {4,10}},
                                           {{12,1}, {7,6},  {11,2}, {8,5},  {10,3},  {9,4}},
                                           {{2,4},  {1,5},  {3,6},  {7,10}, {8,12},  {9,11}},
                                           {{1,4},  {2,6},  {3,5},  {8,10}, {7,11},  {9,12}},
                                           {{1,3},  {2,5},  {4,6},  {7,9},  {8,11},  {10,12}},
                                           {{2,3},  {1,6},  {4,5},  {8,9},  {7,12},  {10,11}},
                                           {{1,2},  {3,4},  {5,6},  {7,8},  {9,10},  {11,12}}};

    public List<Game> generateTourGames(List<TourneyPlayer> players, Integer tour, List<Game> previousTourGames) {
        List<Game> tourGames = new ArrayList<>();
        int [][][] tours;
        switch (players.size()) {
            case 1, 2:
                tours = tours_1_2;
                break;
            case 3, 4:
                tours = tours_3_4;
                break;
            case 5, 6:
                tours = tours_5_6;
                break;
            case 7, 8:
                tours = tours_7_8;
                break;
            case 9, 10:
                tours = tours_9_10;
                break;
            case 11, 12:
                tours = tours_11_12;
                break;
            default:
                throw new TourGamesGenerationException("%d of tourney players is not supported".formatted(players.size()));
        }
        if (tour > 0 && tour <= tours.length) {
            for (int iGame = 0; iGame < tours[tour-1].length; iGame++) {
                int player1_n = tours[tour-1][iGame][0];
                int player2_n = tours[tour-1][iGame][1];
                if (player1_n <= players.size() && player2_n <= players.size()) {
                    TourneyPlayer player1 = players.get(player1_n-1);
                    TourneyPlayer player2 = players.get(player2_n-1);
                    Game game = new Game(player1, player2, player1.getRatingCurrent(), player2.getRatingCurrent(), player1.getTourney(), tour);
                    tourGames.add(game);
                }
            }
        } else
            throw new TourGamesGenerationException("%d tour is out of range".formatted(tour));
        return tourGames;
    }
}
