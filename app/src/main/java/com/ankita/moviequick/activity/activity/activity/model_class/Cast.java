package com.ankita.moviequick.activity.activity.activity.model_class;

import android.app.Person;

public class Cast extends People {
    private String character;

    public Cast() {
    }

    /**
     * Initialize a cast member.
     *
     * @param name the cast name
     * @param character the cast character
     * @param profilePath the cast poster url
     */
    public Cast(String name, String character, String profilePath) {
        this.name = name;
        this.character = character;
        this.profilePath = profilePath;
        setProfileUrl(profilePath);
    }

    public String getCharacter() {
        return character;
    }
}
