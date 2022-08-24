package com.tungstun.barapi.presentation.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

@ApiModel(description = "Response details about the category")
public class CategoryResponse {
    @ApiModelProperty(notes = "The category's categoryId")
    private UUID id;

    @ApiModelProperty(notes = "The category's name")
    private String name;

    public CategoryResponse() { }
    public CategoryResponse(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
