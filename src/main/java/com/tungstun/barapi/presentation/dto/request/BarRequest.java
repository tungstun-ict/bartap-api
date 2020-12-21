package com.tungstun.barapi.presentation.dto.request;

import javax.validation.constraints.NotBlank;

public class BarRequest {

    @NotBlank
    public String adres;

    @NotBlank
    public String name;

    @NotBlank
    public String mail;

    @NotBlank
    public String phoneNumber;

}
