package ru.javawebinar.topjava.testData;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.Util;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.testData.UserTestData.USER_ID;
import static ru.javawebinar.topjava.testData.UserTestData.ADMIN_ID;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class MealToTestData {

    public static int MEAL_ID = START_SEQ + 2;
    public static final int DEFAULT_CALORIES_LIMIT = 2000;

    public static final List<Meal> allRecords = new ArrayList<>(16);
    public static final List<MealTo> userRecords = new ArrayList<>();
    public static final List<MealTo> userRecordsAfterDeletingLastMeal = new ArrayList<>();
    public static final List<MealTo> userRecordsAfterUpdatingLastMeal = new ArrayList<>();
    public static final List<MealTo> userRecordsAfterCreatingMeal = new ArrayList<>();
    public static final List<MealTo> adminRecords = new ArrayList<>();
    public static MealTo lastUserMeal;
    public static Meal updatedLastUserMeal;
    public static Meal newUserMeal;
    public static final int NOT_PRESENT_ID = 0;

    static {
        setupAllRecords();
        setupUserRecords();
        setupLastUserMeal();
        setupUserRecordsAfterDeletingLastMeal();
        setupUpdatedLastUserMeal();
        setupUserRecordsAfterUpdatingLastMeal();
        setupNewUserMeal();
        setupUserRecordsAfterCreatingMeal();
        setupAdminRecords();
    }

    public static List<MealTo> getBetweenHalfOpenUser(LocalDateTime start, LocalDateTime end) {
        return MealsUtil.filterByPredicate(allRecords, DEFAULT_CALORIES_LIMIT,
                m -> m.getUserId().equals(USER_ID) && Util.isBetweenHalfOpen(m.getDateTime(), start, end));
    }

    private static void setupAllRecords() {
        allRecords.add(new Meal(MEAL_ID++, USER_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 467));
        allRecords.add(new Meal(MEAL_ID++, USER_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 611));
        allRecords.add(new Meal(MEAL_ID++, USER_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        allRecords.add(new Meal(MEAL_ID++, USER_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        allRecords.add(new Meal(MEAL_ID++, USER_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 520));
        allRecords.add(new Meal(MEAL_ID++, USER_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 1000));
        allRecords.add(new Meal(MEAL_ID++, USER_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 500));
        allRecords.add(new Meal(MEAL_ID++, ADMIN_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 400));
        allRecords.add(new Meal(MEAL_ID++, ADMIN_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 520));
        allRecords.add(new Meal(MEAL_ID++, ADMIN_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 930));
        allRecords.sort(((Comparator<Meal>) (o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime())).reversed());
    }

    private static void setupUserRecords() {
        List<Meal> userMeals = filterById(USER_ID);
        userRecords.addAll(MealsUtil.getTos(userMeals, DEFAULT_CALORIES_LIMIT));
    }

    private static void setupLastUserMeal() {
        lastUserMeal = userRecords.get(0);
    }

    private static void setupUserRecordsAfterDeletingLastMeal() {
        List<Meal> userMeals = filterById(USER_ID);
        List<Meal> userRecordsAfterDeletingLastMealList = new ArrayList<>(userMeals);
        userRecordsAfterDeletingLastMealList.remove(0);
        userRecordsAfterDeletingLastMeal
                .addAll(MealsUtil.getTos(userRecordsAfterDeletingLastMealList, DEFAULT_CALORIES_LIMIT));
    }

    private static void setupUpdatedLastUserMeal(){
        updatedLastUserMeal = new Meal(lastUserMeal.getId(),
                USER_ID, lastUserMeal.getDateTime(), lastUserMeal.getDescription(), 100);
    }

    private static void setupUserRecordsAfterUpdatingLastMeal() {
        List<Meal> userMeals = filterById(USER_ID);
        List<Meal> userRecordsAfterUpdatingLastMealList = new ArrayList<>(userMeals);
        userRecordsAfterUpdatingLastMealList.set(0, updatedLastUserMeal);
        userRecordsAfterUpdatingLastMeal
                .addAll(MealsUtil.getTos(userRecordsAfterUpdatingLastMealList, DEFAULT_CALORIES_LIMIT));
    }

    private static void setupNewUserMeal(){
        newUserMeal = new Meal(MEAL_ID,
                USER_ID, LocalDateTime.of(2021, Month.MARCH, 20, 14, 10), "New meal", 600);
    }

    private static void setupUserRecordsAfterCreatingMeal() {
        List<Meal> userMeals = filterById(USER_ID);
        List<Meal> userRecordsAfterCreatingMealList = new ArrayList<>(userMeals);
        userRecordsAfterCreatingMealList.add(0, newUserMeal);
        userRecordsAfterCreatingMeal
                .addAll(MealsUtil.getTos(userRecordsAfterCreatingMealList, DEFAULT_CALORIES_LIMIT));
    }


    private static void setupAdminRecords() {
        List<Meal> adminMeals = filterById(ADMIN_ID);
        adminRecords.addAll(MealsUtil.getTos(adminMeals, DEFAULT_CALORIES_LIMIT));
    }

    private static List<Meal> filterById(int id) {
        return allRecords.stream().filter(m -> Objects.equals(m.getUserId(), id)).collect(Collectors.toList());
    }

}
