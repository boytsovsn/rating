package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.entities.Player;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlayerRepositoryImpl implements PlayerRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Player> findAll() {
        var query = em.createQuery("select distinct b from Player b left join fetch b.rating order by b.id", Player.class);
        return query.getResultList();
    }

    @Override
    public Optional<Player> findById(Long Id) {
        var query = em.createQuery("select distinct b from Player b left join fetch b.rating where b.id = :id", Player.class);
        query.setParameter("id", Id);
        try {
            Player res = query.getSingleResult();
            return Optional.of(res);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void resetSequence() {
        String queryStr = "ALTER SEQUENCE Players_id_seq RESTART WITH 4";
        var query = em.createNativeQuery(queryStr).executeUpdate();
    }

}
