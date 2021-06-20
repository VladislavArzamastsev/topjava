package ru.javawebinar.topjava.testData;

import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.testData.UserTestData.USER_ID;
import static ru.javawebinar.topjava.testData.UserTestData.ADMIN_ID;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class MealTestData {

    public static int MEAL_ID = START_SEQ;

    public static final List<Meal> allRecords = new ArrayList<Meal>(16) {{
        add(new Meal(MEAL_ID++, USER_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 467));
        add(new Meal(MEAL_ID++, USER_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 611));
        add(new Meal(MEAL_ID++, USER_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        add(new Meal(MEAL_ID++, USER_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        add(new Meal(MEAL_ID++, USER_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 520));
        add(new Meal(MEAL_ID++, USER_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 1000));
        add(new Meal(MEAL_ID++, USER_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 500));
        add(new Meal(MEAL_ID++, ADMIN_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 400));
        add(new Meal(MEAL_ID++, ADMIN_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 520));
        add(new Meal(MEAL_ID++, ADMIN_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 930));
    }};

    public static final List<Meal> userRecords = new ArrayList<>();
    public static final List<Meal> adminRecords = new ArrayList<>();
    public static final int NOT_PRESENT_ID = 0;

    static {
        for (Meal meal : allRecords) {
            if (meal.getUserId().equals(USER_ID)) {
                userRecords.add(meal);
            } else {
                adminRecords.add(meal);
            }
        }
    }


}
