package org.example.travelaiassistant.tools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.travelaiassistant.model.Contact;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ContactTools {

    private final JdbcTemplate jdbcTemplate;

    @Tool(description = "Find contacts in a given city")
    public List<Contact> findContactsByCity(String city) {

        String sql = "SELECT name, email, city FROM contacts WHERE city = ?";

        RowMapper<Contact> rowMapper = (rs, rowNum) -> new Contact(
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("city")
        );

        return jdbcTemplate.query(sql, rowMapper, city);
    }
}
