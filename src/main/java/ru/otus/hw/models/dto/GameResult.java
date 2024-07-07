package ru.otus.hw.models.dto;

public enum GameResult {
    NO_RESULT,
    FIRST_WIN,
    SECOND_WIN,
    FIRST_WITHDRAW,
    SECOND_WITHDRAW,
    BOTH_WITHDRAW;

    public static GameResult getValueById(int id) throws EnumConstantNotPresentException {
        if (id>=0 && id < values().length) {
            return values()[id];
        } else {
            throw new EnumConstantNotPresentException(GameResult.class, "id = %d".formatted(id));
        }
    }

    public static GameResult getValueByName(String name) throws EnumConstantNotPresentException {
        for (GameResult value : values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        throw new EnumConstantNotPresentException(GameResult.class, "name = %s".formatted(name));
    }
}
