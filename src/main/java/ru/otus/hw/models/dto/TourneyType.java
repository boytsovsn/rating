package ru.otus.hw.models.dto;

public enum TourneyType {
    Circle,
    Net;

    public static TourneyType getValueById(int id) throws EnumConstantNotPresentException {
        if (id>=0 && id < values().length) {
            return values()[id];
        } else {
            throw new EnumConstantNotPresentException(TourneyType.class, "id = %d".formatted(id));
        }
    }
}
