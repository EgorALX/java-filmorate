package ru.yandex.practicum.filmorate.storage.db.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipMapper implements RowMapper<Friendship> {

    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        Friendship friendship = new Friendship();
        friendship.setFirstUserId(rs.getLong("user_id"));
        friendship.setSecondUserId(rs.getLong("friend_id"));
        friendship.setIsFriendship(rs.getBoolean("is_friend"));
        return friendship;
    }
}