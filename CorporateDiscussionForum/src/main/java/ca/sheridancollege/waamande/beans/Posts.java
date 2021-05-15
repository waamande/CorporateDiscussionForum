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
public class Posts implements Serializable{
	private static final long serialVersionUID = 1L;
	private long postId;
	private long threadId;
	private String post;
	public String username;
	@DateTimeFormat(pattern="yyyy-MM-dd") private LocalDate postDate;
	@DateTimeFormat(pattern="HH:mm") private LocalTime postTime;
}
