package com.thinkin_service.provider.data.network.model;

public class Walkthrough {
    public String title, description;
    public int drawable;

    public Walkthrough(int drawable, String title, String description) {
        this.drawable = drawable;
        this.title = title;
        this.description = description;
    }

}
