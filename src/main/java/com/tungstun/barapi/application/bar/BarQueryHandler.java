package com.tungstun.barapi.application.bar;

import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.bar.query.ListConnectedBars;
import com.tungstun.barapi.application.bar.query.ListOwnedBars;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.security.application.user.UserQueryHandler;
import com.tungstun.security.domain.user.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BarQueryHandler {
    private final BarRepository barRepository;
    private final UserQueryHandler userQueryHandler;

    public BarQueryHandler(BarRepository barRepository,UserQueryHandler userQueryHandler) {
        this.barRepository = barRepository;
        this.userQueryHandler = userQueryHandler;
    }

    public Bar handle(GetBar query) {
        return barRepository.findById(query.barId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Bar with id %s doesn't exist", query.barId())));
    }

    public List<Bar> handle(ListOwnedBars query) {
        Set<UUID> ownedBarIds = ((User) userQueryHandler.loadUserByUsername(query.username()))
                .getAuthorizations()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase("OWNER"))
                .map(Map.Entry::getKey)
                .collect(Collectors.toUnmodifiableSet());
        return barRepository.findAllById(ownedBarIds);
    }

    public List<Bar> handle(ListConnectedBars query) {
        Set<UUID> ownedBarIds = ((User) userQueryHandler.loadUserByUsername(query.username()))
                .getAuthorizations()
                .keySet();
        return barRepository.findAllById(ownedBarIds);
    }
}
