package ru.otus.hw.models.dto;

public enum Rank {
    Final_I,
    Final_II,
    Final_III;

    public static Rank getValueById(int id) throws EnumConstantNotPresentException {
        if (id>=0 && id < values().length) {
            return values()[id];
        } else {
            throw new EnumConstantNotPresentException(Rank.class, "id = %d".formatted(id));
        }
    }
}
