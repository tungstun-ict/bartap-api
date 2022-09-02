package com.tungstun.barapi.port.web.bar.response;

import java.util.UUID;

public record BarResponse(
        UUID id,
        String address,
        String name,
        String mail,
        String phoneNumber) {
}
