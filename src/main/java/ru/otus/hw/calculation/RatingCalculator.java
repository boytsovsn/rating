package ru.otus.hw.calculation;

public class RatingCalculator {

    static private Float formula(Float winner, Float loser, Float turnirCoef)
    {
        Float ret;
        if (winner - loser < 100)
            ret = turnirCoef *(100 - (winner - loser));
        else
            ret = 0F;
        return ret;
    }

    static public Float winnerRating(Float winner, Float loser, Float turnirCoef) {
        Float prirost = 0F;
        prirost = formula(winner, loser, turnirCoef)/10F;
        return winner + prirost;
    }

    static public Float loserRating(Float winner, Float loser, Float turnirCoef) {
        Float prirost = 0F;
        prirost = formula(winner, loser, turnirCoef)/20F;
        return loser - prirost;
    }

    static public Float tourneyCoefficient(Float meanRating) {
        if (meanRating < 250F) {
            return 0.2F;
        } else if (meanRating < 350F) {
            return 0.25F;
        } else if (meanRating < 450F) {
            return 0.3F;
        } else if (meanRating < 550F) {
            return 0.35F;
        } else {
            return 0.4F;
        }
    }
}
