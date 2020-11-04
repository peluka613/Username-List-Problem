package com.tests.username.list.problem.util;

import com.tests.username.list.problem.db.entities.RestrictedWords;
import com.tests.username.list.problem.db.entities.UsersList;
import com.tests.username.list.problem.db.repositories.RestrictedWordsRepository;
import com.tests.username.list.problem.db.repositories.UserListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Carlos Andres Valencia Rojas
 */
@Component
public class Utilities {

    public static final String USERNAME = "username";
    public static final String RESTRICTED_WORD = "restrictedWord";

    @Autowired
    private UserListRepository userListRepository;

    @Autowired
    private RestrictedWordsRepository restrictedWordsRepository;

    /**
     * Returns tge list of usernames sorted by username
     *
     * @return List<UsersList>
     */
    public List<UsersList> findAllUsersList() {
        return userListRepository.findAll(sortAsc(USERNAME));
    }

    /**
     * Returns tge list of RestrictedWords sorted by restrictedWord
     *
     * @return List<RestrictedWords>
     */
    public List<RestrictedWords> findAllRestrictedWords() {
        return restrictedWordsRepository.findAll(sortAsc(RESTRICTED_WORD));
    }

    /**
     * Sort ascending by field
     *
     * @param field
     * @return Sort
     */
    public Sort sortAsc(String field) {
        return new Sort(Sort.Direction.ASC, field);
    }

    /**
     * Validates if does the current username already exist in database
     *
     * @param username
     * @return boolean
     */
    public boolean validateUsernameExists(String username) {
        List<UsersList> usersList = findAllUsersList();
        Optional<UsersList> user = usersList.stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findAny();

        return user.isPresent();
    }

    /**
     * Generates a list of sugested usernames based on the current username
     *
     * @param username
     * @return
     */
    public List<String> generateSugestedUsernames(String username) {
        List<String> sugestedUsernames = new ArrayList<>();

        for (int i = 0; i < 14; ++i) {
            sugestedUsernames.add(username + generateString());
        }

        return sugestedUsernames.stream().sorted().collect(Collectors.toList());
    }

    /**
     * Generates a random 4 length String based on UUID
     *
     * @return
     */
    public String generateString() {
        String uuid = UUID.randomUUID().toString();
        return "_" + uuid.substring(0, 4);
    }

}
