package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Autowired UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getFriends(Long userId) {
        List<User> friends = new ArrayList<>();
        for (Long id : userStorage.getUserById(userId).getFriends()) {
            friends.add(userStorage.getUserById(id));
        }
        return friends;
    }

    public Collection<User> getCommonFriends(Long userId1, Long userId2) {
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);
        if (user1 == null || user2 == null) {
            throw new NotFoundException("Пользователь не найден.");
        }
        return user1.getFriends()
                .stream()
                .filter(user2.getFriends()::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }
}
