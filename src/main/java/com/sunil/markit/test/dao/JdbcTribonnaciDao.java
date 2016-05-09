package com.sunil.markit.test.dao;

import com.sunil.markit.test.entities.Tribonnaci;
import com.sunil.markit.test.enums.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTribonnaciDao implements TribonnaciDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTribonnaciDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Tribonnaci> getTribonnaciByType(Type type) {
        String sql = "SELECT * FROM TRIBONNACI WHERE TYPE = ?";
        return jdbcTemplate.query(sql, new Object[]{type.toString()}, new RowMapper<Tribonnaci>() {
            @Override
            public Tribonnaci mapRow(ResultSet rs, int i) throws SQLException {
                Tribonnaci tribonnaci = new Tribonnaci();
                tribonnaci.setNumber(rs.getInt("NUMBER"));
                tribonnaci.setCounter(rs.getInt("COUNTER"));
                return tribonnaci;
            }
        });
    }

    @Override
    public void saveTribonnaci(Map<Integer, Integer> callCount, final Type type) {
        String sql = "MERGE INTO TRIBONNACI USING (VALUES(CAST(? AS INT), CAST(? AS INT), CAST(? AS VARCHAR(9)))) AS params(NUMBER, COUNTER, TYPE)" +
                " ON TRIBONNACI.NUMBER = params.NUMBER AND TRIBONNACI.TYPE = params.TYPE" +
                " WHEN MATCHED THEN UPDATE SET TRIBONNACI.COUNTER = params.COUNTER" +
                " WHEN NOT MATCHED THEN INSERT (NUMBER, COUNTER, TYPE) VALUES(params.NUMBER, params.COUNTER, params.TYPE)";

        synchronized (callCount) {
            final List<Map.Entry<Integer, Integer>> listcallCount = new ArrayList<>(callCount.entrySet());
            try {
                jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, listcallCount.get(i).getKey());
                        ps.setInt(2, listcallCount.get(i).getValue());
                        ps.setString(3, type.toString());
                    }

                    @Override
                    public int getBatchSize() {
                        return listcallCount.size();
                    }
                });
            } catch (Exception e) {
                throw e;
            }
        }
    }
}
