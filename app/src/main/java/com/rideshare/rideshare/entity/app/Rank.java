package com.rideshare.rideshare.entity.app;

public class Rank {

    private String id;
    private String name;
    private int rank;

    public Rank(){
        this.rank = 3;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }
}
