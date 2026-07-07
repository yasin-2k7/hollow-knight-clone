package com.hollowknight.model.enums;

public enum Achievement {
    COMPLETION(Texts.COMPLETION, Texts.COMPLETION_DESC),
    DEFEAT_FALSE_KNIGHT(Texts.DEFEAT_FALSE_KNIGHT, Texts.DEFEAT_FALSE_KNIGHT_DESC),
    SPEEDRUN(Texts.SPEEDRUN, Texts.SPEEDRUN_DESC),
    TRUE_HUNTER(Texts.TRUE_HUNTER, Texts.TRUE_HUNTER_DESC),
    ZOTE(Texts.ZOTE, Texts.ZOTE_DESC);

    public final Texts title;
    public final Texts description;

    Achievement(Texts title, Texts description){
        this.title = title;
        this.description = description;
    }
}
