package com.ksrate.data;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Statistic {

    private static final String SEPARATOR = ",";

    private final String id;
    private final String name;
    private final String category;
    private final String main_category;
    private final String currency;
    private final String deadline;
    private final String goal;
    private final String launched;
    private final String pledged;
    private final String state;
    private final String backers;
    private final String country;
    private final String usd;
    private final String source;

    public Statistic(String s) {
        source = s;
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
