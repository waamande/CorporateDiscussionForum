package ca.sheridancollege.waamande.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Threads implements Serializable{
	private static final long serialVersionUID = 1L;
	private long threadId;
	private String title;
	private String thread;
	private String username;
	@DateTimeFormat(pattern="yyyy-MM-dd") private LocalDate threadDate;
	@DateTimeFormat(pattern="HH:mm") private LocalTime threadTime;
}
