package fr._42.sockets.repositories;

import fr._42.sockets.models.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class MessagesRepositoryImpl implements MessagesRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired 
    public MessagesRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(Message entity) {
        jdbcTemplate.update(
            """
            INSERT INTO messages_m09_ex01
                (sender_id, text, sending_time)
            VALUES (?, ?, ?)
            """,
            entity.getSender().getIdentifier(),
            entity.getText(),
            entity.getSendingTime()
        );
    }
}