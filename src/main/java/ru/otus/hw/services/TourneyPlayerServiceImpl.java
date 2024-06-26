package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.entities.TourneyPlayer;
import ru.otus.hw.repositories.TourneyPlayerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourneyPlayerServiceImpl implements TourneyPlayerService {

    @PersistenceContext
    private final EntityManager em;

    public List<TourneyPlayer> findPlayersByTourney(Long tourneyId) {
        List<TourneyPlayer> tourneyPlayers = em.createQuery("select tp from TourneyPlayer tp left join fetch tp.tourney t where t.id = :tid")
                .setParameter("tid", tourneyId).getResultList();
        return tourneyPlayers;
    }

}
