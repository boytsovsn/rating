package ru.otus.hw.models.dto;

public enum Stage {
    SubGroups,
    Finals;

    public static Stage getValueById(int id) throws EnumConstantNotPresentException {
        if (id>=0 && id < values().length) {
            return values()[id];
        } else {
            throw new EnumConstantNotPresentException(Stage.class, "id = %d".formatted(id));
        }
    }
}
