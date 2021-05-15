package ca.sheridancollege.waamande.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ca.sheridancollege.waamande.beans.Posts;
import ca.sheridancollege.waamande.beans.Threads;
import ca.sheridancollege.waamande.beans.UserRegistration;
import ca.sheridancollege.waamande.database.DatabaseAccess;

@Controller
public class HomeController {
	
	@Autowired
	JdbcUserDetailsManager jdbcUserDetailsManager;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired 
	private DatabaseAccess da;
	
	@GetMapping("/")
	public String getIndex() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.getAuthorities().toString().equals("[ROLE_EMPLOYEE]")) {
			return "redirect:/discussionForum";
		}
		else if(auth.getAuthorities().toString().equals("[ROLE_MANAGER]")) {
			return "index";
		}
		return null;
	}
	
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	
	@GetMapping("/discussionForum")
	public String getDiscussionForum(Model model) {
		model.addAttribute("threads", da.getAllThreads());
		return "discussion forum";
	}
	
	@GetMapping("/threadInput")
	public String getThreadInput(Model model, Threads thread) {
		model.addAttribute("thread", thread);
		return "thread input";
	}
	
	@PostMapping("/threadInput")
	public String processThreadInput(@ModelAttribute Threads thread) {
		Authentication auth =SecurityContextHolder.getContext().getAuthentication();
		thread.setUsername(auth.getName());
		
		LocalDate date = LocalDate.now();
		thread.setThreadDate(date);
		
		LocalTime time = LocalTime.now();
		thread.setThreadTime(time);
		
		da.insertThread(thread);
		return "redirect:/discussionForum";
	}
	
	@GetMapping("/postView/{id}")
	public String getPosts(@PathVariable int id, Model model, Posts post) {
		model.addAttribute("th", da.getThreadById(id));
		model.addAttribute("posts", da.getAllPosts(id));
		return "post view";
	}
	
	@GetMapping("/postInput/{id}")
	public String getPostInput(@PathVariable int id, Model model, Posts post) {
		model.addAttribute("th", da.getThreadById(id));
		
		post.setThreadId(id);
		model.addAttribute("post", post);
		return "post input";
	}
	
	@PostMapping("/postInput")
	public String postInput(@ModelAttribute Posts post, Model model) {
		Authentication auth =SecurityContextHolder.getContext().getAuthentication();
		post.setUsername(auth.getName());
		
		LocalDate date = LocalDate.now();
		post.setPostDate(date);
		
		LocalTime time = LocalTime.now();
		post.setPostTime(time);
		
		da.insertPost(post);
		
		model.addAttribute("th", da.getThreadById((int)post.getThreadId()));
		model.addAttribute("posts", da.getAllPosts((int)post.getThreadId()));
		return "post view";
	}
	
	@GetMapping("/register")
	private String getRegister(Model model, UserRegistration user) {
		model.addAttribute("user", user);
		return "register";
	}
	
	@PostMapping("/register")
	public String processRegister(@ModelAttribute UserRegistration user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		User newuser = new User(user.getUsername(), encodedPassword, authorities);
		jdbcUserDetailsManager.createUser(newuser);
		return "redirect:/";
	}
}
