package com.tungstun.barapi.application.order.query;

import java.util.UUID;

public record ListOrdersOfSession(
        UUID barId,
        UUID sessionId){
}
