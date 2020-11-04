package com.tests.username.list.problem.controller;

import com.tests.username.list.problem.db.entities.RestrictedWords;
import com.tests.username.list.problem.db.entities.UsersList;
import com.tests.username.list.problem.dto.ResponseDto;
import com.tests.username.list.problem.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Carlos Andres Valencia Rojas
 */
@RestController(value = "/users")
public class UserController {

    public static final String USERNAME_LENGTH_ERR = "Username should contain at least 6 characters";

    @Autowired
    private Utilities utilities;

    /**
     * Returns tge list of usernames sorted by username
     *
     * @return Collection<UsersList>
     */
    @GetMapping
    public Collection<UsersList> getAllUserList() {
        return utilities.findAllUsersList();
    }

    /**
     * Returns tge list of RestrictedWords sorted by restrictedWord
     *
     * @return List<RestrictedWords>
     */
    @GetMapping(value = "/restricted")
    public Collection<RestrictedWords> getAllRestrictedWords() {
        return utilities.findAllRestrictedWords();
    }

    /**
     * Validated all the business logic and do validations
     *
     * @param username
     * @return ResponseEntity<ResponseDto>
     */
    @GetMapping(value = "/username")
    public ResponseEntity<ResponseDto> getUserByUsername(@RequestParam String username) {

        validateUsernameLength(username);

        boolean isValidUsername = true;
        List<String> usersList = new ArrayList<>();

        Optional<String> restrictedWord = validateContainsRestrictedWords(username);

        if (restrictedWord.isPresent()) {
            isValidUsername = false;
            usersList = utilities.generateSugestedUsernames(username.replaceAll(restrictedWord.get(), ""));
        } else if (utilities.validateUsernameExists(username)) {
            isValidUsername = false;
            usersList = utilities.generateSugestedUsernames(username);
        }

        ResponseDto dto = new ResponseDto();
        dto.setValid(isValidUsername);
        dto.setSugestedUsernames(usersList);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Validates the length of the username should be at least six
     *
     * @param username
     */
    private void validateUsernameLength(String username) {
        if (username == null || username.length() < 6) {
            throw new IllegalArgumentException(USERNAME_LENGTH_ERR);
        }
    }

    /**
     * Finds if the username contains a restricted word
     *
     * @param username
     * @return Optional<String>
     */
    private Optional<String> validateContainsRestrictedWords(String username) {
        List<RestrictedWords> restrictedWords = utilities.findAllRestrictedWords();
        Optional<RestrictedWords> restricted = restrictedWords.stream().filter(u -> username.toUpperCase().contains(u.getRestrictedWord().toUpperCase())).findAny();

        Optional<String> restrictedWord = Optional.ofNullable(null);

        if (restricted.isPresent()) {
            restrictedWord = Optional.of(restricted.get().getRestrictedWord());
        }

        return restrictedWord;
    }


}
