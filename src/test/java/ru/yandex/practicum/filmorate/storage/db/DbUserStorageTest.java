package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DbUserStorageTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private DbUserStorage userStorage;

    @BeforeEach
    protected void set() throws IOException {
        userStorage = new DbUserStorage(namedParameterJdbcTemplate);
    }

    @Test
    public void testFindUserByIdInvalidFindByIdAndCreation() {
        User newUser = new User();
        newUser.setEmail("user@email.ru");
        newUser.setLogin("vanya123");
        newUser.setName("Ivan Petrov");
        newUser.setBirthday(LocalDate.of(1990, 1, 1));
        assertNotNull(newUser);
        assertThrows(NotFoundException.class, () -> {
            userStorage.getById(9999L).orElseThrow(() -> new NotFoundException("Data not found"));
        });
        userStorage.create(newUser);
        newUser.setId(1L);
        User savedUser = userStorage.getById(1L).orElseThrow(() -> new NotFoundException("Data not found"));
        assertNotNull(savedUser);
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testCreaeteInvalide() {
        User newUser = new User();
        newUser.setEmail(null);
        newUser.setLogin("vanya123");
        newUser.setName("Ivan Petrov");
        newUser.setBirthday(LocalDate.of(1000, 1, 1));
        assertNotNull(newUser);
        assertThrows(DataIntegrityViolationException.class, () -> {userStorage.create(newUser);});
    }

    @Test
    public void testUpdate() {
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
                LocalDate.of(1990, 1, 1));
        User secondUser = new User(1L, "secondUser@email.ru", "viktop111", "viktor",
                LocalDate.of(1980, 1, 1));
        assertNotNull(newUser, "Null");
        assertNotNull(secondUser, "Null");
        userStorage.create(newUser);
        User savedUser = userStorage.getById(1L).orElseThrow(() -> new NotFoundException("Data not found"));
        assertNotNull(savedUser);
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);

        userStorage.update(secondUser);
        User secondSavedUser = userStorage.getById(1L).orElseThrow(() -> new NotFoundException("Data not found"));
        assertNotNull(secondSavedUser, "Null");
        assertThat(secondSavedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(secondUser)
                .isNotEqualTo(newUser);
    }

    @Test
    public void testNameChange() {
        User newUser = new User(1L, "user@email.ru", "vanya123", "",
                LocalDate.of(1990, 1, 1));
        assertNotNull(newUser, "Null");
        userStorage.create(newUser);
        User savedUser = userStorage.getById(1L).orElseThrow(() -> new NotFoundException("Data not found"));
        assertNotNull(newUser.getLogin(), savedUser.getName());
    }

    @Test
    public void testGetAll() {
        DbUserStorage userStorage = new DbUserStorage(namedParameterJdbcTemplate);
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
                LocalDate.of(1990, 1, 1));
        User secondUser = new User(2L, "secondUser@email.ru", "viktop111", "viktor",
                LocalDate.of(1980, 1, 1));
        assertNotNull(newUser, "Null");
        assertNotNull(secondUser, "Null");
        userStorage.create(newUser);
        userStorage.create(secondUser);
        List<User> users = userStorage.getAll();
        assertNotNull(users, "List is null");
        assertEquals(2, users.size(), "The list should contain exactly 2 elements");
        assertEquals(newUser, users.get(0), "The list should contain exactly 2 elements");
        assertEquals(secondUser, users.get(1), "The list should contain exactly 2 elements");
    }

    @Test
    public void testAllFriendThings() {
        User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
                LocalDate.of(1990, 1, 1));
        User secondUser = new User(2L, "secondUser@email.ru", "viktop111", "viktor",
                LocalDate.of(1980, 1, 1));
        User trirdUser = new User(3L, "trirdUser@email.ru", "pi111t", "pit",
                LocalDate.of(1985, 1, 1));
        User fourthUser = new User(4L, "fourthUser@email.ru", "Ole111g", "Oleg",
                LocalDate.of(1988, 1, 1));
        assertNotNull(newUser, "Null");
        assertNotNull(secondUser, "Null");
        assertNotNull(trirdUser, "Null");
        assertNotNull(fourthUser, "Null");
        userStorage.create(newUser);
        userStorage.create(secondUser);
        userStorage.create(trirdUser);
        userStorage.create(fourthUser);
        userStorage.addFriend(newUser.getId(), secondUser.getId());
        userStorage.addFriend(secondUser.getId(), newUser.getId());
        userStorage.addFriend(secondUser.getId(), trirdUser.getId());
        userStorage.addFriend(secondUser.getId(), fourthUser.getId());
        List<User> firstUserFriends = userStorage.getFriends(newUser.getId());
        assertNotNull(firstUserFriends);
        assertEquals(1, firstUserFriends.size());
        assertEquals(secondUser, firstUserFriends.get(0));
        List<User> secondUserFriends = userStorage.getFriends(secondUser.getId());
        assertNotNull(firstUserFriends);
        assertEquals(3, secondUserFriends.size());
        assertEquals(newUser, secondUserFriends.get(0));
        assertEquals(trirdUser, secondUserFriends.get(1));
        assertEquals(fourthUser, secondUserFriends.get(2));
        userStorage.addFriend(newUser.getId(), trirdUser.getId());
        userStorage.addFriend(newUser.getId(), fourthUser.getId());

        List<User> commonFriends = userStorage.getCommonFriends(newUser.getId(), secondUser.getId());
        assertNotNull(commonFriends);
        assertEquals(2, commonFriends.size());
        assertEquals(trirdUser, commonFriends.get(0));
        assertEquals(fourthUser, commonFriends.get(1));
    }
}