package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;

@Controller
public class MealRestController {

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Collection<MealTo> getAll() {
        return service.getAll(SecurityUtil.authUserId());
    }

    public MealTo get(int id) {
        return service.get(id, SecurityUtil.authUserId());
    }

    public MealTo create(Meal meal) {
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        service.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        service.update(meal, id, SecurityUtil.authUserId());
    }
}