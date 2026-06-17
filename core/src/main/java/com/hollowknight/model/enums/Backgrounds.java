package com.hollowknight.model.enums;

public enum Backgrounds {
    ECHOES("ui/bg/bg_ready.ogv"),
    HORNET_WATERFALL("ui/bg/hornet-waterfall-hollow-knight.ogv"),
    SILENT_HERO("ui/bg/silent-hero-of-hollownest.ogv"),
    THE_KNIGHT("ui/bg/the-knight-hollow-knight-moewalls-com (video-converter.com).mp4");


    private final String path;
    Backgrounds(String path){
        this.path = path;
    }

    public String getPath(){
        return this.path;
    }
}
