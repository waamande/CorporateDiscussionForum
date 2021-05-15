package ca.sheridancollege.waamande.database;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.waamande.beans.Posts;
import ca.sheridancollege.waamande.beans.Threads;


@Repository
public class DatabaseAccess {
	
	@Autowired
	public NamedParameterJdbcTemplate jdbc;
	
	public void insertThread(Threads thread) {
		String query = "INSERT INTO threads (title, thread, username, threadDate, threadTime) VALUES (:title, :thread, :username, :threadDate, :threadTime)";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("title", thread.getTitle());
		map.put("thread", thread.getThread());
		map.put("username", thread.getUsername());
		map.put("threadDate", thread.getThreadDate());
		map.put("threadTime", thread.getThreadTime());
		jdbc.update(query, map);
	}
	
	public ArrayList<Threads> getAllThreads() {
		String query = "SELECT * FROM threads";
		ArrayList<Threads> threads = (ArrayList<Threads>) jdbc.query(query, new BeanPropertyRowMapper<Threads>(Threads.class));
		return threads;
	}
	
	public Threads getThreadById(int id) {
		String query = "SELECT * FROM threads WHERE threadId=:id";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		ArrayList<Threads> threads = (ArrayList<Threads>) jdbc.query(query, map, new BeanPropertyRowMapper<Threads>(Threads.class));
		if(threads.size()>0)
		{
			return threads.get(0);
		}
		return null;
	}
	
	public void insertPost(Posts post) {
		String query = "INSERT INTO posts (threadId, post, username, postDate, postTime) VALUES (:threadId, :post, :username, :postDate, :postTime)";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("threadId", post.getThreadId());
		map.put("post", post.getPost());
		map.put("username", post.getUsername());
		map.put("postDate", post.getPostDate());
		map.put("postTime", post.getPostTime());
		jdbc.update(query, map);
	}
	
	public ArrayList<Posts> getAllPosts(int id) {
		String query = "SELECT * FROM posts WHERE threadId=:id";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		ArrayList<Posts> posts = (ArrayList<Posts>) jdbc.query(query, map, new BeanPropertyRowMapper<Posts>(Posts.class));
		return posts;
	}
	
	public Posts getPostById(int id) {
		String query = "SELECT * FROM posts WHERE postId=:id";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		ArrayList<Posts> posts = (ArrayList<Posts>) jdbc.query(query, map, new BeanPropertyRowMapper<Posts>(Posts.class));
		if(posts.size()>0)
		{
			return posts.get(0);
		}
		return null;
	}

}
