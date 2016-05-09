package com.sunil.markit.test.dao;

import com.sunil.markit.test.entities.Tribonnaci;
import com.sunil.markit.test.enums.Type;

import java.util.List;
import java.util.Map;

public interface TribonnaciDao {

    List<Tribonnaci> getTribonnaciByType(Type type);

    void saveTribonnaci(Map<Integer, Integer> callCount, Type type);

}
