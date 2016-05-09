package com.sunil.markit.test.dao;

import com.sunil.markit.test.entities.Tribonnaci;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sunil.markit.test.enums.Type.RECURSIVE;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@ContextConfiguration(locations = {"classpath:/system-test-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcTribonnaciDaoTest {

    @Autowired
    JdbcTribonnaciDao classToTest;

    @Test
    public void shouldGetTribonnaciByType() {

        // Given
        // Tribonnaci entries already exist in the database

        // When
        List<Tribonnaci> tribonnaci = classToTest.getTribonnaciByType(RECURSIVE);

        // Then
        assertThat(tribonnaci, is(notNullValue()));
        assertThat(tribonnaci.size(), equalTo(10));
    }

    @Test
    public void shouldSaveTribonnaci() {

        // Given
        List<Tribonnaci> tribonnacis = classToTest.getTribonnaciByType(RECURSIVE);
        Map<Integer, Integer> callCount = new HashMap<>();
        for (Tribonnaci tribonnaci : tribonnacis) {
            callCount.put(tribonnaci.getNumber(), tribonnaci.getCounter());
        }
        callCount.put(5, 1);
        callCount.put(6, 35);

        // When
        classToTest.saveTribonnaci(callCount, RECURSIVE);

        // Then
        List<Tribonnaci> savedtribonnacis = classToTest.getTribonnaciByType(RECURSIVE);
        assertThat(savedtribonnacis, is(notNullValue()));
        assertThat(savedtribonnacis.size(), equalTo(11));
        assertThat(savedtribonnacis.get(4).getNumber(), equalTo(5));
        assertThat(savedtribonnacis.get(4).getCounter(), equalTo(1));
        assertThat(savedtribonnacis.get(5).getNumber(), equalTo(6));
        assertThat(savedtribonnacis.get(5).getCounter(), equalTo(35));
    }
}
