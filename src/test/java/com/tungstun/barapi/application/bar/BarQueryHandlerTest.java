package com.tungstun.barapi.application.bar;

import com.tungstun.barapi.application.bar.query.GetBar;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.bar.BarBuilder;
import com.tungstun.barapi.domain.bar.BarRepository;
import com.tungstun.security.application.user.UserQueryHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BarQueryHandlerTest {
    private static final BarRepository repository = mock(BarRepository.class);
    private static final UserQueryHandler userQueryHandler = mock(UserQueryHandler.class);
    private static final BarQueryHandler barQueryHandler = new BarQueryHandler(repository, userQueryHandler);

    @AfterEach
    void teardown() {
        clearInvocations(repository, userQueryHandler);
    }

    @Test
    @DisplayName("Get bar returns bar")
    void getBar_ReturnsBar() throws EntityNotFoundException {
        Bar expectedBar = new BarBuilder("bar").build();
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(expectedBar));

        Bar bar = barQueryHandler.handle(new GetBar(UUID.randomUUID()));

        assertEquals(expectedBar, bar);
        verify(repository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Get not existing bar throws")
    void getNotExistingBar_ThrowsNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> barQueryHandler.handle(new GetBar(UUID.randomUUID()))
        );

        verify(repository, times(1)).findById(any(UUID.class));
    }
}
