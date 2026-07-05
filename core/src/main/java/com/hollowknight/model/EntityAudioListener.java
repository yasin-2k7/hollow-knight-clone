package com.hollowknight.model;

import com.hollowknight.model.enums.AudioAction;

public interface EntityAudioListener {
    void onAudioEvent(AudioAction action);
}
