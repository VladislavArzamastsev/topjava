package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HardcodeMealsDao implements MealsDao{

    private static final HardcodeMealsDao INSTANCE = new HardcodeMealsDao();
    private final List<Meal> data = new CopyOnWriteArrayList<>(Arrays.asList(
            new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)));

    public static HardcodeMealsDao getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Meal> getAll() {
        return data;
    }
}
