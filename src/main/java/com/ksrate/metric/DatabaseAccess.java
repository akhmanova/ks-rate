package com.ksrate.metric;

import com.ksrate.Main;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class DatabaseAccess {
    private final Properties connectionProperties;
    private PreparedStatement checkCountsExist = null;
    private PreparedStatement checkCountry = null;
    private PreparedStatement insertCountry = null;
    private PreparedStatement increaseSuccessCount = null;
    private PreparedStatement increaseFailCount = null;
    private PreparedStatement increaseCountrySuccess = null;
    private PreparedStatement increaseCountryFailed = null;
    private PreparedStatement fillTop10Successful = null;
    private PreparedStatement fillTop10Failed = null;
    private Connection connection = null;

    public DatabaseAccess() {
        connectionProperties = getProperties();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/metrics" +
                            "?useUnicode=true" +
                            "&useJDBCCompliantTimezoneShift=true" +
                            "&useLegacyDatetimeCode=false" +
                            "&serverTimezone=UTC", connectionProperties);

            this.checkCountsExist = connection.prepareStatement(
                    "select successcount, failcount from projectStateCount where id = 1");

            this.checkCountry = connection.prepareStatement("select 1 from countryStats where country = ?");

            this.insertCountry = connection.prepareStatement(
                    "insert into countryStats (country, successcount, failcount) values (?, 0, 0)");

            this.increaseSuccessCount = connection.prepareStatement(
                    "update projectStateCount set successcount = successcount + 1 where id = 1 limit 1");

            this.increaseFailCount = connection.prepareStatement(
                    "update projectStateCount set failcount = failcount + 1 where id = 1 limit 1");

            this.increaseCountrySuccess = connection.prepareStatement(
                    "update countryStats set successCount = successCount + 1 where country = ? limit 1");

            this.increaseCountryFailed = connection.prepareStatement(
                    "update countryStats set failCount = failCount + 1 where country = ? limit 1");

            this.fillTop10Successful = connection.prepareStatement("insert into top10success (country, successcount) " +
                    "select country, successcount from countryStats order by successcount desc limit 10");

            this.fillTop10Failed = connection.prepareStatement("insert into top10failed (country, failcount) " +
                    "select country, failcount from countryStats order by failcount desc limit 10");

            if (!checkCountsExist.executeQuery().next()) {
                connection.createStatement().executeUpdate(
                        "insert into projectStateCount(id, successcount, failcount) values (1, 0, 0)");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private Properties getProperties() {
        final Properties connectionProperties = new Properties();
        connectionProperties.load(Files.newInputStream(Paths.get(Main.arguments.getDbPropertiesPath())));
        return connectionProperties;
    }

    public void increaseSuccessCount() {
        try {
            increaseSuccessCount.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void increaseFailCount() {
        try {
            increaseFailCount.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void increaseCountrySuccess(String country) {
        try {
            checkCountry.setString(1, country.toLowerCase());
            ResultSet res = checkCountry.executeQuery();
            if (!res.next()) {
                insertCountry.setString(1, country);
                insertCountry.executeUpdate();
            }
            increaseCountrySuccess.setString(1, country);
            increaseCountrySuccess.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void increaseCountryFailed(String country) {
        try {
            checkCountry.setString(1, country.toLowerCase());
            ResultSet res = checkCountry.executeQuery();
            if (!res.next()) {
                insertCountry.setString(1, country);
                insertCountry.executeUpdate();
            }
            increaseCountryFailed.setString(1, country);
            increaseCountryFailed.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refillTop10Success() {
        try {
            connection.createStatement().executeUpdate(
                    "delete from top10success");
            connection.createStatement().executeUpdate(
                    "alter table top10success AUTO_INCREMENT = 1");
            fillTop10Successful.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refillTop10Failed() {
        try {
            connection.createStatement().executeUpdate(
                    "delete from top10failed;");
            connection.createStatement().executeUpdate(
                    "alter table top10failed AUTO_INCREMENT = 1");
            fillTop10Failed.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

