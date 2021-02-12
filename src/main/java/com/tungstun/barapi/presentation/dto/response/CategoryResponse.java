package com.tungstun.barapi.presentation.dto.response;

public class CategoryResponse {
    private Long id;
    private String name;

    public CategoryResponse() { }
    public CategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
