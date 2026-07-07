package com.hollowknight.model;

import com.hollowknight.model.enums.Achievement;

public interface EventListener {
    void onAchievementUnlocked(Achievement achievement);
}
