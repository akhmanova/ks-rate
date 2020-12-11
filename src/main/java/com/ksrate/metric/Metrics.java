package com.ksrate.metric;

import com.ksrate.data.Statistic;
import com.ksrate.dbacess.DatabaseAccess;

public class Metrics {
    private final DatabaseAccess db;
    public Metrics() {
        this.db = new DatabaseAccess();
    }

    public void push(Statistic statistic) {
        String state = statistic.getState().toLowerCase();
        if (state.equals("successful")) {
            recalculateSuccessProjects(statistic);
            recalculateTopSuccessCountries(statistic);
        } else if (state.equals("failed")) {
            recalculateFailedProjects(statistic);
            recalculateTopFailedCountries(statistic);
        }
    }

    private void recalculateSuccessProjects(Statistic statistic) {
        db.increaseSuccessCount();
        String country = statistic.getCountry();
        db.increaseCountrySuccess(country);
    }

    private void recalculateFailedProjects(Statistic statistic) {
        db.increaseFailCount();
        String country = statistic.getCountry();
        db.increaseCountryFailed(country);
    }

    private void recalculateTopSuccessCountries(Statistic statistic) {
        db.refillTop10Success();
    }

    private void recalculateTopFailedCountries(Statistic statistic) {
        db.refillTop10Failed();
    }
}
