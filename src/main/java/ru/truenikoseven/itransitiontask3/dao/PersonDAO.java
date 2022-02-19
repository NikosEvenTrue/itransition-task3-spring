package ru.truenikoseven.itransitiontask3.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.truenikoseven.itransitiontask3.models.LoginData;
import ru.truenikoseven.itransitiontask3.models.PasswordlessPerson;
import ru.truenikoseven.itransitiontask3.models.Person;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class PersonDAO {

    private final String TABLE_NAME = "persons";
    private final String CRYPTHO_METHOD = "SHA-256";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private BeanPropertyRowMapper rowMapper = new BeanPropertyRowMapper(Person.class);
    private MessageDigest msgDigest = MessageDigest.getInstance(CRYPTHO_METHOD);

    public PersonDAO() throws NoSuchAlgorithmException {
    }

    public List<PasswordlessPerson> getPeople() {
        return jdbcTemplate.query("SELECT * FROM " + TABLE_NAME, new BeanPropertyRowMapper<>(PasswordlessPerson.class));
    }

    public List<LoginData> tryLoginAndGetLoginData(LoginData loginData) {
        loginData.setPass_word(getHashPassword(loginData.getPass_word(), loginData.getEmail(), msgDigest));
        return jdbcTemplate.query("SELECT email, pass_word FROM " + TABLE_NAME +
                " WHERE email=\"" + loginData.getEmail() + "\" and pass_word=\"" + loginData.getPass_word() + "\"" +
                        " and isBlocked=false" ,
                new BeanPropertyRowMapper<>(LoginData.class));
    }

    public LoginData insertPerson(Person person) {
        person.setPass_word(getHashPassword(person.getPass_word(), person.getEmail(), msgDigest));
        jdbcTemplate.update("INSERT INTO " + TABLE_NAME + " (name, email, registeredDate, lastActivity, isBlocked, pass_word)" +
                        "VALUES (?, ?, ?, ?, ?, ?)", person.getName(), person.getEmail(), person.getRegisteredDate(),
                person.getLastActivity(), person.isBlocked(), person.getPass_word());
        return new LoginData(person.getEmail(), person.getPass_word());
    }

    public int deletePeople(int[] ids) {
        return jdbcTemplate.update("DELETE FROM " + TABLE_NAME + " WHERE id IN " + intArrayToStringSqlSet(ids));
    }

    public int setIsBlockedToPeople(boolean isBlocked, int[] ids) {
        return jdbcTemplate.update("UPDATE " + TABLE_NAME + " SET isBlocked=" + isBlocked + " WHERE id IN " + intArrayToStringSqlSet(ids));
    }

    public boolean isAutorized(LoginData loginData) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(id) FROM " + TABLE_NAME +
                " WHERE email=\"" + loginData.getEmail() + "\" and pass_word=\"" + loginData.getPass_word() + "\"" +
                " and isBlocked=false", Integer.class);
        return count == 1;
    }

    public void setLastActivity(Date date, String email) {
        jdbcTemplate.update("UPDATE " + TABLE_NAME + " SET lastActivity=? WHERE email=?", date, email);
    }

    private String getHashPassword(String password, String salt, MessageDigest msgDigest) {
        byte[] hash_bytes = msgDigest.digest((password+salt).getBytes(StandardCharsets.UTF_8));
        BigInteger hash_bitint = new BigInteger(1, hash_bytes);
        String hash_string = hash_bitint.toString(16);
        return hash_string;
    }

    private String intArrayToStringSqlSet(int[] arr) {
        return Arrays.toString(arr).replace('[', '(').replace(']', ')');
    }
}