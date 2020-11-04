package com.tests.username.list.problem;

import com.tests.username.list.problem.controller.UserController;
import com.tests.username.list.problem.db.entities.RestrictedWords;
import com.tests.username.list.problem.db.entities.UsersList;
import com.tests.username.list.problem.db.repositories.RestrictedWordsRepository;
import com.tests.username.list.problem.db.repositories.UserListRepository;
import com.tests.username.list.problem.dto.ResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meanbean.test.BeanTester;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootTestApplicationTests {

    public static final String USER_TEST = "userTest";
    public static final String RESTRICTED_TEST = "xxxTest";

    @Autowired
    private UserListRepository userListRepository;

    @Autowired
    private RestrictedWordsRepository restrictedWordsRepository;

    @Autowired
    private UserController userController;

    /**
     * Tests the repository's coverage
     */
    @Test
    public void repositoryTest() {

        UsersList foundUser = userListRepository.findByUsername(USER_TEST);
        assertNull(foundUser);

        UsersList user = new UsersList(USER_TEST);
        userListRepository.save(user);

        foundUser = userListRepository.findByUsername(USER_TEST);
        assertNotNull(foundUser);
    }

    /**
     * Tests the entities and pojos' coverage
     */
    @Test
    public void testEntitiesAndPojos() {
        BeanTester beanTester = new BeanTester();
        beanTester.testBean(UsersList.class);
        beanTester.testBean(RestrictedWords.class);
        beanTester.testBean(ResponseDto.class);

        HashCodeMethodTester hashCodeMethodTester = new HashCodeMethodTester();
        hashCodeMethodTester.testHashCodeMethod(UsersList.class);
        hashCodeMethodTester.testHashCodeMethod(RestrictedWords.class);

        EqualsMethodTester equalsMethodTester = new EqualsMethodTester();
        equalsMethodTester.testEqualsMethod(UsersList.class);
        equalsMethodTester.testEqualsMethod(RestrictedWords.class);
    }

    @Test
    public void getAllUserListTest() {
        Collection<UsersList> userList = userController.getAllUserList();
        assertNotNull(userList);
    }

    @Test
    public void getAllRestrictedWordsTest() {
        Collection<RestrictedWords> restrictedWords = userController.getAllRestrictedWords();
        assertNotNull(restrictedWords);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUserByUsernameLessThanSixCharactersShouldFail() {
        userController.getUserByUsername("ASD");
    }

    @Test
    public void getUserByUsernameContainsRestrictedWordsShouldBeFalse() {
        restrictedWordsRepository.save(new RestrictedWords(RESTRICTED_TEST));
        ResponseEntity<ResponseDto> response = userController.getUserByUsername(USER_TEST + RESTRICTED_TEST);
        assertFalse(response.getBody().isValid());
    }

    @Test
    public void getUserByUsernameExistsShouldBeFalse() {
        UsersList user = new UsersList(USER_TEST);
        userListRepository.save(user);
        ResponseEntity<ResponseDto> response = userController.getUserByUsername(USER_TEST);
        assertFalse(response.getBody().isValid());
    }

    @Test
    public void getUserByValidUsernameShouldBeTrue() {
        ResponseEntity<ResponseDto> response = userController.getUserByUsername(USER_TEST + "1");
        assertTrue(response.getBody().isValid());
    }

    @Test
    public void SpringBootTestApplicationMain() {
        SpringBootTestApplication main = mock(SpringBootTestApplication.class);
        main.main(new String[]{});
    }

}
