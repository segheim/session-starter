package com.example.session.service;

import com.example.session.dto.SessionDto;
import com.example.session.model.Session;
import com.example.session.repository.SessionRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
public class SessionService {

    private final SessionRepository repository;

    public Long save(Session session) {
        Session savedSession = repository.save(session);
        return savedSession.getId();
    }

    public SessionDto findByLoginOrCreate(String login) {
        Optional<Session> optionalSession = repository.findByLogin(login);
        if (optionalSession.isEmpty()) {
            Session savedSession = repository.save(new Session(null, login, null));
            return new SessionDto(savedSession.getId(), savedSession.getLogin(), savedSession.getCreateAt());
        }
        Session session = optionalSession.get();
        return new SessionDto(session.getId(), session.getLogin(), session.getCreateAt());
    }

}
