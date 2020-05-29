package com.catalog.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

	private String username;
	private List<String> roles;
}
