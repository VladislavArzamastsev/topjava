package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class MealService {

    private final MealRepository repository;
    private final UserService userService;

    @Autowired
    public MealService(MealRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public Collection<MealTo> getAll(Integer userId) {
        return MealsUtil.getTos(repository.getAll(userId), userService.get(userId).getCaloriesPerDay());
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