package ro.ubb.tjfblooddonation.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

@Controller
public class DonorController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();
    @Autowired
    UsersService usersService;
}