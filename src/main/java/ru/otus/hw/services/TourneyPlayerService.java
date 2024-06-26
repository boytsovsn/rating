package ru.otus.hw.services;

import ru.otus.hw.models.entities.TourneyPlayer;

import java.util.List;

public interface TourneyPlayerService {

    List<TourneyPlayer> findPlayersByTourney(Long tourneyId);
}
