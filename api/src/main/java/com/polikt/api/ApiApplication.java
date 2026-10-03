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
				<p>Essa API expõe endpoints para usuários, agências, notícias, cursos, módulos, conteúdos e guias.</p>
				<p>Os métodos HTTP principais são: GET para leitura, POST para criação, PATCH para atualização parcial e DELETE para remoção.</p>

				<h2>Sistema</h2>
				<ul>
					<li>GET /</li>
				</ul>

				<h2>Usuários</h2>
				<ul>
					<li>GET /users</li>
					<li>GET /users/{id}</li>
					<li>GET /users/me</li>
					<li>POST /users</li>
					<li>POST /users/auth</li>
					<li>PATCH /users/{id}</li>
					<li>DELETE /users/{id}</li>
				</ul>

				<h2>Agências</h2>
				<ul>
					<li>GET /agencies</li>
					<li>GET /agencies/{id}</li>
					<li>POST /agencies</li>
					<li>DELETE /agencies/{id}</li>
				</ul>

				<h2>Notícias</h2>
				<ul>
					<li>GET /news</li>
					<li>GET /news/{id}</li>
					<li>POST /news</li>
					<li>PATCH /news/{id}</li>
					<li>DELETE /news/{id}</li>
				</ul>

				<h2>Cursos</h2>
				<ul>
					<li>GET /courses</li>
					<li>GET /courses/{id}</li>
					<li>POST /courses</li>
					<li>PATCH /courses/{id}</li>
					<li>DELETE /courses/{id}</li>
				</ul>

				<h2>Módulos dos cursos</h2>
				<ul>
					<li>GET /courses/{courseId}/modules</li>
					<li>GET /courses/{courseId}/modules/{id}</li>
					<li>POST /courses/{courseId}/modules</li>
					<li>PATCH /courses/{courseId}/modules/{id}</li>
					<li>DELETE /courses/{courseId}/modules/{id}</li>
				</ul>

				<h2>Conteúdo dos módulos</h2>
				<ul>
					<li>GET /courses/{courseId}/modules/{moduleId}/content</li>
					<li>GET /courses/{courseId}/modules/{moduleId}/content/{id}</li>
					<li>POST /courses/{courseId}/modules/{moduleId}/content</li>
					<li>PATCH /courses/{courseId}/modules/{moduleId}/content/{id}</li>
					<li>DELETE /courses/{courseId}/modules/{moduleId}/content/{id}</li>
				</ul>

				<h2>Guias</h2>
				<ul>
					<li>GET /guides</li>
					<li>GET /guides/{id}</li>
					<li>POST /guides</li>
					<li>PATCH /guides/{id}</li>
					<li>DELETE /guides/{id}</li>
				</ul>

				<h2>Etapas dos guias</h2>
				<ul>
					<li>GET /guides/{guideId}/steps</li>
					<li>GET /guides/{guideId}/steps/{id}</li>
					<li>POST /guides/{guideId}/steps</li>
					<li>PATCH /guides/{guideId}/steps/{id}</li>
					<li>DELETE /guides/{guideId}/steps/{id}</li>
				</ul>

				<p><strong>Observação:</strong> as rotas GET são públicas; criação, atualização, exclusão e o perfil do usuário autenticado exigem JWT válido.</p>
				<p>Nas criações de notícias, cursos e guias, o autor (<code>user</code>) é obtido automaticamente pelo token JWT, dispensando o envio desse campo.</p>
				""";
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}
