package com.polikt.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ApiApplication {

	@GetMapping("/")
	String index() {
		return """
				<h1>Polikt API</h1>
				<p>An endpoint is a specific URL where an API receives requests and returns a response.</p>
				<p>HTTP methods describe the action: GET reads data, POST creates data, and DELETE removes data.</p>

				<h2>System</h2>
				<ul><li>GET /</li></ul>

				<h2>Users</h2>
				<ul>
					<li>GET /users</li>
					<li>GET /users/{id}</li>
					<li>POST /users</li>
					<li>DELETE /users/{id}</li>
				</ul>

				<h2>Agencies</h2>
				<ul>
					<li>GET /agencies</li>
					<li>GET /agencies/{id}</li>
					<li>POST /agencies</li>
					<li>DELETE /agencies/{id}</li>
				</ul>

				<h2>News</h2>
				<ul>
					<li>GET /news</li>
					<li>GET /news/{id}</li>
					<li>POST /news</li>
					<li>DELETE /news/{id}</li>
				</ul>

				<h2>Guides</h2>
				<ul>
					<li>GET /guides</li>
					<li>GET /guides/{id}</li>
					<li>POST /guides</li>
					<li>DELETE /guides/{id}</li>
				</ul>

				<h2>Guide Steps</h2>
				<ul>
					<li>GET /guides/{guideId}/steps</li>
					<li>GET /guides/{guideId}/steps/{id}</li>
					<li>POST /guides/{guideId}/steps</li>
					<li>DELETE /guides/{guideId}/steps/{id}</li>
				</ul>
				""";
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}
