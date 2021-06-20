package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.testData.MealToTestData;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.testData.UserTestData.USER_ID;
import static ru.javawebinar.topjava.testData.UserTestData.ADMIN_ID;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;

import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void getAll() {
        Collection<MealTo> userRecords = mealService.getAll(USER_ID);
        assertThat(userRecords).usingFieldByFieldElementComparator().isEqualTo(MealToTestData.userRecords);
        Collection<MealTo> adminRecords = mealService.getAll(ADMIN_ID);
        assertThat(adminRecords).usingFieldByFieldElementComparator().isEqualTo(MealToTestData.adminRecords);
    }

    @Test
    public void getAllBetweenHalfOpen() {
        LocalDateTime start = LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0);
        LocalDateTime end = LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0);
        Collection<MealTo> expected = MealToTestData.getBetweenHalfOpenUser(start, end);
        Collection<MealTo> actual = mealService.getAll(USER_ID, start, end);
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test
    public void get() {
        MealTo lastMealTo = mealService.get(100008, USER_ID);
        assertThat(lastMealTo).usingRecursiveComparison().isEqualTo(MealToTestData.lastUserMeal);
    }

    @Test(expected = NotFoundException.class)
    public void getNotExistingFood() {
        MealTo lastMealTo = mealService.get(MealToTestData.NOT_PRESENT_ID, USER_ID);
        assertThat(lastMealTo).usingRecursiveComparison().isEqualTo(MealToTestData.lastUserMeal);
    }

    @Test(expected = NotFoundException.class)
    public void getSomeoneElsesFood() {
        MealTo lastMealTo = mealService.get(100011, USER_ID);
        assertThat(lastMealTo).usingRecursiveComparison().isEqualTo(MealToTestData.lastUserMeal);
    }

    @Test
    public void create() {
        Meal meal = MealToTestData.newUserMeal;
        meal.setId(null);
        meal.setUserId(null);
        mealService.create(meal, USER_ID);
        Collection<MealTo> actual = mealService.getAll(USER_ID);
        Collection<MealTo> expected = MealToTestData.userRecordsAfterCreatingMeal;
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test(expected = DuplicateKeyException.class)
    public void createWithExistingDatetime() {
        Meal meal = MealToTestData.newUserMeal;
        meal.setId(null);
        meal.setUserId(null);
        MealTo lastUserMeal = MealToTestData.lastUserMeal;
        meal.setDateTime(lastUserMeal.getDateTime());
        mealService.create(meal, USER_ID);
        Collection<MealTo> actual = mealService.getAll(USER_ID);
        Collection<MealTo> expected = MealToTestData.userRecordsAfterCreatingMeal;
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test
    public void delete() {
        MealTo mealToDelete = MealToTestData.lastUserMeal;
        mealService.delete(mealToDelete.getId(), USER_ID);
        Collection<MealTo> actual = mealService.getAll(USER_ID);
        Collection<MealTo> expected = MealToTestData.userRecordsAfterDeletingLastMeal;
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test(expected = NotFoundException.class)
    public void deleteSomeoneElsesFood() {
        MealTo mealToDelete = MealToTestData.lastUserMeal;
        mealService.delete(mealToDelete.getId(), ADMIN_ID);
    }

    @Test
    public void update() {
        Meal meal = MealToTestData.updatedLastUserMeal;
        mealService.update(
                meal,
                meal.getId(),
                USER_ID);
        Collection<MealTo> actual = mealService.getAll(USER_ID);
        Collection<MealTo> expected = MealToTestData.userRecordsAfterUpdatingLastMeal;
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test(expected = NotFoundException.class)
    public void updateSomeoneElsesFood() {
        Meal meal = MealToTestData.updatedLastUserMeal;
        mealService.update(
                meal,
                meal.getId(),
                ADMIN_ID);
    }

}