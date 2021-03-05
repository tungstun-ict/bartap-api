package com.tungstun.barapi.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Response details about the person")
public class PersonResponse {

    @ApiModelProperty(notes = "The person's id")
    private Long id;

    @ApiModelProperty(notes = "The person's name")
    private String name;

    @ApiModelProperty(notes = "The person's phone number")
    private String phonenumber;

    public PersonResponse() { }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPhonenumber() { return phonenumber; }

    public void setPhonenumber(String phonenumber) { this.phonenumber = phonenumber; }
}
