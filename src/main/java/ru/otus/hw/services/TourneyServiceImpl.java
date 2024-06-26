package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.entities.Tourney;
import ru.otus.hw.repositories.TourneyRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TourneyServiceImpl implements TourneyService {
    private final TourneyRepository tourneyRepository;

    @Override
    public List<Tourney> findAll() {
        return tourneyRepository.findAll();
    }

    @Override
    public Optional<Tourney> findById(Long id) {
        return tourneyRepository.findById(id);
    }
}
