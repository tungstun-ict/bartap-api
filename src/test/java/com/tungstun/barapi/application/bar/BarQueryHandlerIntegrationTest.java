package com.tungstun.barapi.application.bar;

import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.application.bar.query.ListOwnedBars;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.port.persistence.bar.SpringBarRepository;
import com.tungstun.security.domain.user.User;
import com.tungstun.security.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class BarQueryHandlerIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpringBarRepository repository;
    @Autowired
    private BarQueryHandler barQueryHandler;

    @Test
    @DisplayName("Get all bars of bar owner returns bars")
    void getAllBarsOfBarOwner_ReturnsBars() {
        repository.save(new BarBuilder("bar").build()); //Not owned bar
        Bar bar = repository.save(new BarBuilder("bar2").build());
        User user = new User("user", "", "", "", "", "+31612345678", new ArrayList<>());
        user.newBarAuthorization(bar.getId());
        user = userRepository.save(user);

        List<Bar> resBars = barQueryHandler.handle(new ListOwnedBars(user.getUsername()));

        assertEquals(1, resBars.size());
        assertTrue(resBars.contains(bar));
    }

    @Test
    @DisplayName("Get bar returns bar")
    void getBar_ReturnsBar() throws EntityNotFoundException {
        Bar bar = repository.save(new BarBuilder("bar").build());

        Bar resBar = barQueryHandler.handle(new GetBar(bar.getId()));

        assertEquals(bar, resBar);
    }

    @Test
    @DisplayName("Get not existing bar throws")
    void getNotExistingBar_ThrowsNotFound() {
        assertThrows(
                EntityNotFoundException.class,
                () -> barQueryHandler.handle(new GetBar(UUID.randomUUID()))
        );
    }
}
