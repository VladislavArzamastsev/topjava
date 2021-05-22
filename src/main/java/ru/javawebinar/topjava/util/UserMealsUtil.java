package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        mealsTo = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, List<UserMeal>> dateToMealMap = new HashMap<>();
        for (UserMeal userMeal : meals) {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                LocalDate day = userMeal.getDateTime().toLocalDate();
                List<UserMeal> mealsAtDay = dateToMealMap.getOrDefault(day, new LinkedList<>());
                mealsAtDay.add(userMeal);
                dateToMealMap.put(day, mealsAtDay);
            }
        }
        List<UserMealWithExcess> out = new LinkedList<>();
        for (List<UserMeal> userMeals : dateToMealMap.values()) {
            int sum = 0;
            for (UserMeal meal : userMeals) {
                sum += meal.getCalories();
            }
            boolean excess = sum >= caloriesPerDay;
            for (UserMeal meal : userMeals) {
                out.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
            }
        }
        return out;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, List<UserMeal>> localDateUserMealMap = meals
                .stream()
                .filter(m -> TimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate()));
        Map<List<UserMeal>, Integer> mealsToCaloriesMap = localDateUserMealMap
                .values()
                .stream()
                .collect(Collectors.toMap(Function.identity(), list -> list.stream().mapToInt(UserMeal::getCalories).sum()));
        return mealsToCaloriesMap
                .keySet()
                .stream()
                .map(list -> {
                    List<UserMealWithExcess> userMealWithExcesses = new LinkedList<>();
                    list.forEach(e -> userMealWithExcesses
                            .add(new UserMealWithExcess(e.getDateTime(), e.getDescription(), e.getCalories(), mealsToCaloriesMap.get(list) >= caloriesPerDay)));
                    return userMealWithExcesses;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
