package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.dao.HardcodeMealsDao;
import ru.javawebinar.topjava.dao.MealsDao;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class MealServlet extends HttpServlet {

    private static final int CALORIES_PER_DAY = 2000;
    private final MealsDao mealsDao = HardcodeMealsDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<MealTo> mealToList = MealsUtil.filteredByStreams(mealsDao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
        req.setAttribute("meals", mealToList);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
