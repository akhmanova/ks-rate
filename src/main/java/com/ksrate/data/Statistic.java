package com.ksrate.data;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Statistic {

    private static final String SEPARATOR = ",";

    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private final String category;
    @Getter
    private final String main_category;
    @Getter
    private final String currency;
    @Getter
    private final String deadline;
    @Getter
    private final String goal;
    @Getter
    private final String launched;
    @Getter
    private final String pledged;
    @Getter
    private final String state;
    @Getter
    private final String backers;
    @Getter
    private final String country;
    @Getter
    private final String usd;

    public Statistic(String s) {
        final String[] split = s.split(SEPARATOR);
        id = split[0];
        name = split[1];
        category = split[2];
        main_category = split[3];
        currency = split[4];
        deadline = split[5];
        goal = split[6];
        launched = split[7];
        pledged = split[8];
        state = split[9];
        backers = split[10];
        country = split[11];
        usd = split[12];
    }

}
