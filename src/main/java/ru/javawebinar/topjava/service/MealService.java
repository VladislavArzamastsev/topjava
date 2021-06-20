package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MealService {

    private final MealRepository repository;
    private final UserService userService;

    @Autowired
    public MealService(@Qualifier("jdbcMealRepository") MealRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public Collection<MealTo> getAll(Integer userId) {
        User u = userService.get(userId);
        if(u == null){
            return new ArrayList<>();
        }
        return MealsUtil.getTos(repository.getAll(userId), u.getCaloriesPerDay());
    }

    public Collection<MealTo> getAll(Integer userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        User u = userService.get(userId);
        if(u == null){
            return new ArrayList<>();
        }
        LocalDateTime startOfDay = LocalDateTime.of(startDateTime.toLocalDate(), LocalTime.MIDNIGHT);
        LocalDateTime startOfNextDayToEnd = LocalDateTime.of(endDateTime.toLocalDate(), LocalTime.MIDNIGHT).plusDays(1);
        List<MealTo> tos = MealsUtil.getTos(repository.getAll(userId, startOfDay, startOfNextDayToEnd), u.getCaloriesPerDay());
        return tos.stream().filter(to -> Util.isBetweenHalfOpen(to.getDateTime(), startDateTime, endDateTime)).collect(Collectors.toList());

    }

    public MealTo get(int id, Integer userId) {
        Optional<MealTo> meal = getAll(userId).stream().filter(m -> Objects.equals(m.getId(), id)).findFirst();
        return meal.orElseThrow(() -> new NotFoundException("No such meal"));
    }

    public MealTo create(Meal meal, Integer userId) {
        Meal saved = repository.save(meal, userId);
        return get(saved.getId(), userId);
    }

    public void delete(int id, Integer userId) {
        if (repository.delete(id, userId)) {
            throw new NotFoundException("No such meal");
        }
    }

    public void update(Meal meal, int id, Integer userId) {
        meal.setId(id);
        if (repository.save(meal, userId) == null) {
            throw new NotFoundException("Meal does not belong to this user");
        }
    }

}