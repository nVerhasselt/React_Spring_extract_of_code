package com.itso.occupancy.occupancy.dto.model.project;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class JiraProjectListDto {
    // private String self;
    // private int maxResults;
    // private int startAt;
    // private int total;

    // @JsonProperty("isLast")
    // private boolean isLast;
    
    private List<JiraProjectDto> values;
}