package com.tests.username.list.problem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDto {

    private boolean valid;
    private List<String> sugestedUsernames;
}
