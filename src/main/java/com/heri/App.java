package com.heri;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jooby.Jooby;
import io.jooby.hikari.HikariModule;
import io.jooby.json.JacksonModule;
import io.jooby.rocker.RockerModule;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import javax.sql.DataSource;

import static io.jooby.ExecutionMode.EVENT_LOOP;
import static io.jooby.MediaType.JSON;

public class App extends Jooby {

  private static final String UPDATE_USER = "UPDATE users SET firstName=? where id=?";
  private static final String SELECT_USERS = "SELECT * FROM users LIMIT 10";

  {
    /** JSON: */
    install(new JacksonModule());
    ObjectMapper mapper = require(ObjectMapper.class);

    /** Database: */
    install(new HikariModule());
    DataSource ds = require(DataSource.class);

    /** Template engine: */
    install(new RockerModule());

    /** Go blocking: */
    dispatch(() -> {

      get("/webhook", ctx -> {
        User result;

        final String query = (String)ctx.query("firstName").value();
        final String id = (String)ctx.query("id").value();

        try (Connection conn = ds.getConnection()) {
          try (final PreparedStatement statement = conn.prepareStatement(UPDATE_USER)) {
            statement.setString(1, query);
            statement.setString(1, id);
            try (ResultSet rs = statement.executeQuery()) {
              rs.next();
              result = new User(rs.getString("id"), rs.getString("firstName"), rs.getString("lastName"));
            }
          }
        }
        return ctx
            .setResponseType(JSON)
            .send(mapper.writeValueAsBytes(result));
      });

      get("/queries", ctx -> {
        User[] result = new User[10];
        try (Connection conn = ds.getConnection()) {
          for (int i = 0; i < result.length; i++) {
            try (final PreparedStatement statement = conn.prepareStatement(SELECT_USERS)) {
              try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                result[i] = new User(rs.getString("id"), rs.getString("firstName"), rs.getString("lastName"));
              }
            }
          }
        }
        return ctx
            .setResponseType(JSON)
            .send(mapper.writeValueAsBytes(result));
      });

    });
  }

  public static void main(final String[] args) {
    runApp(args, EVENT_LOOP, App::new);
  }
}
