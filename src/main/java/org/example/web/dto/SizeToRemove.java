package org.example.web.dto;

import javax.validation.constraints.NotNull;

public class SizeToRemove {

    @NotNull
    private Integer size;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
