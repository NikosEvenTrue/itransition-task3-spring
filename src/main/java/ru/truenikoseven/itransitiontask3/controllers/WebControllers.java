package ru.truenikoseven.itransitiontask3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import ru.truenikoseven.itransitiontask3.dao.PersonDAO;
import ru.truenikoseven.itransitiontask3.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/")
public class WebControllers {

    @Autowired
    PersonDAO personDAO;

    @GetMapping
    public List<PasswordlessPerson> getPeople(@RequestHeader("email") String email,
                                              @RequestHeader("pass_word") String pass_word) {
        if (personDAO.isAutorized(new LoginData(email, pass_word))) {
            personDAO.setLastActivity(new Date(), email);
            return personDAO.getPeople();
        }
        return getUnlogged();
    }

    @DeleteMapping("/")
    public LoginData deletePeople(@RequestBody int[] ids,
                                  @RequestHeader("email") String email,
                                  @RequestHeader("pass_word") String pass_word) {
        if (personDAO.isAutorized(new LoginData(email, pass_word))) {
            personDAO.setLastActivity(new Date(), email);
            personDAO.deletePeople(ids);
            return new LoginData(email, pass_word);
        }

        return new LoginData();
    }

    @PatchMapping("/")
    public LoginData changePeopleStatus(@RequestBody SetIsBlockedToPeopleData setIsBlockedToPeopleData,
                                        @RequestHeader("email") String email,
                                        @RequestHeader("pass_word") String pass_word) {
        if (personDAO.isAutorized(new LoginData(email, pass_word))) {
            personDAO.setLastActivity(new Date(), email);
            personDAO.setIsBlockedToPeople(setIsBlockedToPeopleData.isBlocked(), setIsBlockedToPeopleData.getIds());
            return new LoginData(email, pass_word);
        }
        return new LoginData();
    }

    @PostMapping("/login")
    public LoginData login(@RequestBody LoginData loginData) {
        List<LoginData> ld = personDAO.tryLoginAndGetLoginData(loginData);
        return ld.size() == 1 ? ld.get(0) : new LoginData();
    }

    @PostMapping("/signup")
    public LoginData signup(@RequestBody SignupData signupData) {
        Person person = new Person();
        person.setName(signupData.getName());
        person.setEmail(signupData.getEmail());
        person.setPass_word(signupData.getPass_word());
        person.setIsBlocked(false);
        person.setRegisteredDate(signupData.getLastActivity());
        person.setLastActivity(signupData.getLastActivity());

        return personDAO.insertPerson(person);
    }

    private List<PasswordlessPerson> getUnlogged() {
        List<PasswordlessPerson> pp = new ArrayList<>();
        Person person = new Person();
        person.setId(-1);
        pp.add(person);
        return pp;
    }
}


