package com.abhi.models;

import com.abhi.models.SessionDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {
	
	private String token;
	
	private String message;
}
