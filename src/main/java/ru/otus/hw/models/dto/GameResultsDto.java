package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameResultsDto {

    List<GameResultDto> selectedResults;

    private Map<Long, Set<GameResultDto>> resultSets;
}
