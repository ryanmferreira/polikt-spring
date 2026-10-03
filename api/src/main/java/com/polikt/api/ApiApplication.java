package com.polikt.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ApiApplication {

	@GetMapping(value = "/", produces = "text/html;charset=UTF-8")
	String index() {
		return """
				<!DOCTYPE html>
				<html lang="en">
				<head>
					<meta charset="UTF-8">
					<meta name="viewport" content="width=device-width, initial-scale=1">
					<meta name="color-scheme" content="dark">
					<meta name="theme-color" content="#0C0C0C">
					<title>Polikt API</title>

					<style>
						:root {
							--primary: #8D6DF0;
							--bg: #0C0C0C;
							--surface: #1C1C1C;
							--border: #393939;
							--text: #F5F5F5;
							--muted: #BDBDC7;
							--on-primary: #FFFFFF;
							--danger: #E32323;
							--success: #42b451;
							--warning: #E0A93B;
							--tag: #30264B;
							--tag-text: #E7DEFF;
							--radius: 12px;
							--pill: 128px;
							--gap: 24px;
						}

						* {
							box-sizing: border-box;
						}

						html {
							color-scheme: dark;
						}

						body {
							margin: 0;
							background: var(--bg);
							color: var(--text);
							font-family: system-ui, -apple-system, "Segoe UI",
								Roboto, sans-serif;
							line-height: 1.5;
						}

						a {
							color: inherit;
						}

						.topbar {
							border-bottom: 1px solid var(--primary);
							background: var(--bg);
							position: sticky;
							top: 0;
							z-index: 10;
						}

						.topbar div {
							max-width: 960px;
							margin: 0 auto;
							padding: 12px 24px;
							display: flex;
							justify-content: space-between;
							align-items: center;
							gap: 16px;
						}

						.topbar span,
						.topbar a {
							color: var(--primary);
							font-size: 13px;
							font-weight: 800;
							text-transform: uppercase;
							text-decoration: none;
						}

						.topbar nav {
							display: flex;
							gap: 20px;
						}

						.topbar a:hover {
							opacity: 0.8;
						}

						main {
							max-width: 960px;
							margin: 0 auto;
							padding: 48px 24px 80px;
						}

						.greeting {
							font-size: 24px;
							margin: 0;
							line-height: 1.25;
						}

						.greeting strong {
							display: block;
							font-weight: 800;
							color: var(--primary);
						}

						.subtitle {
							font-size: 16px;
							margin: 12px 0 16px;
							max-width: 620px;
						}

						.tag {
							display: inline-block;
							background: var(--tag);
							color: var(--tag-text);
							padding: 6px 12px;
							border-radius: var(--pill);
							font-size: 11px;
							font-weight: 800;
							text-transform: uppercase;
							text-decoration: none;
							white-space: nowrap;
						}

						a.tag:hover {
							opacity: 0.8;
						}

						.status {
							display: inline-flex;
							align-items: center;
							gap: 8px;
							margin-bottom: 20px;
						}

						.dot {
							width: 8px;
							height: 8px;
							border-radius: 50%;
							background: var(--success);
						}

						.button {
							display: inline-flex;
							align-items: center;
							justify-content: center;
							background: var(--primary);
							color: var(--on-primary);
							padding: 12px 16px;
							border-radius: var(--radius);
							font-size: 13px;
							font-weight: 800;
							text-transform: uppercase;
							text-decoration: none;
						}

						.button:hover {
							opacity: 0.8;
						}

						.section-title {
							font-size: 18px;
							font-weight: 800;
							color: var(--primary);
							text-transform: uppercase;
							margin: 48px 0 8px;
						}

						.section-divider {
							height: 1px;
							background: var(--primary);
							margin-bottom: var(--gap);
						}

						.card {
							background: var(--surface);
							border-style: solid;
							border-color: var(--border);
							border-width: 1px 0 0 1px;
							border-radius: var(--radius);
							padding: 16px;
						}

						.card h3 {
							margin: 0 0 8px;
							font-size: 16px;
							font-weight: 800;
						}

						.card-divider {
							height: 1px;
							background: var(--border);
							margin-bottom: 8px;
						}

						.how {
							display: grid;
							gap: 16px;
							grid-template-columns:
								repeat(auto-fit, minmax(220px, 1fr));
						}

						.nested {
							background: var(--bg);
							border-style: solid;
							border-color: var(--border);
							border-width: 0 1px 1px 0;
							border-radius: var(--radius);
							padding: 12px;
							font-size: 13px;
							line-height: 1.55;
							color: var(--muted);
						}

						.nested b {
							display: block;
							color: var(--text);
							font-size: 13px;
							font-weight: 800;
							text-transform: uppercase;
							margin-bottom: 4px;
						}

						.nested .tag {
							padding: 2px 8px;
						}

						/*
						 * GRID PRINCIPAL
						 *
						 * A grade possui duas colunas em telas maiores.
						 * Cada coluna ocupa metade do espaço disponível.
						 */
						.grid {
							display: grid;
							gap: var(--gap);
							grid-template-columns: repeat(2, minmax(0, 1fr));
							align-items: stretch;
						}

						/*
						 * Faz todos os cards ocuparem toda a altura
						 * disponível da linha da grid.
						 */
						.grid > .card {
							height: 100%;
						}

						/*
						 * Este card ocupa as duas colunas da grid.
						 */
						.module-content {
							grid-column: span 2;
						}

						.card ul {
							list-style: none;
							margin: 0;
							padding: 0;
							overflow-x: auto;
						}

						.card li {
							display: flex;
							align-items: center;
							gap: 12px;
							padding: 7px 0;
							white-space: nowrap;
						}

						.card li .tag {
							margin-left: auto;
							padding: 2px 8px;
						}

						.m {
							flex: none;
							width: 64px;
							text-align: center;
							padding: 2px 0;
							font-family: ui-monospace, SFMono-Regular, Menlo,
								Consolas, monospace;
							font-size: 11px;
							font-weight: 800;
							border: 1px solid currentColor;
							border-radius: 6px;
						}

						.GET {
							color: var(--success);
						}

						.POST {
							color: var(--primary);
						}

						.PATCH {
							color: var(--warning);
						}

						.DELETE {
							color: var(--danger);
						}

						code {
							font-family: ui-monospace, SFMono-Regular, Menlo,
								Consolas, monospace;
							font-size: 13px;
						}

						.nested code {
							color: var(--tag-text);
						}

						.tags {
							display: flex;
							flex-wrap: wrap;
							gap: 8px;
						}

						footer {
							margin-top: 48px;
							font-size: 12px;
							color: var(--muted);
						}

						footer a {
							color: var(--primary);
							text-decoration: none;
						}

						/*
						 * Em telas pequenas, a grid passa a ter apenas
						 * uma coluna.
						 */
						@media (max-width: 520px) {
							main {
								padding: 32px 16px 64px;
							}

							.topbar div {
								padding: 12px 16px;
							}

							.grid {
								grid-template-columns: 1fr;
							}

							/*
							 * Como agora existe somente uma coluna,
							 * o card volta a ocupar uma única coluna.
							 */
							.module-content {
								grid-column: auto;
							}
						}
					</style>
				</head>

				<body>
					<header class="topbar">
						<div>
							<span>Polikt API</span>

							<nav>
								<a href="https://polikt-spring.onrender.com/">
									API
								</a>

								<a href="https://polikt.vercel.app">
									App
								</a>
							</nav>
						</div>
					</header>

					<main>
						<span class="tag status">
							<span class="dot"></span>
							API online
						</span>

						<p class="greeting">
							WELCOME TO
							<strong>POLIKT API!</strong>
						</p>

						<p class="subtitle">
							REST API for political education and civic awareness.
							It powers news, guides, courses and agencies, plus user
							accounts and authentication.
						</p>

						<a
							class="button"
							href="https://polikt.vercel.app">
							Open the app
						</a>

						<h2 class="section-title">How it works</h2>
						<div class="section-divider"></div>

						<div class="card">
							<div class="how">
								<div class="nested">
									<b>Format</b>
									Every endpoint returns JSON, except this page.
								</div>

								<div class="nested">
									<b>Methods</b>
									GET reads, POST creates, PATCH updates
									partially and DELETE removes.
								</div>

								<div class="nested">
									<b>Authentication</b>
									Log in at
									<code>POST /users/auth</code>
									and send
									<code>
										Authorization: Bearer &lt;token&gt;
									</code>
									on routes marked
									<span class="tag">JWT</span>.
								</div>

								<div class="nested">
									<b>Authorship</b>
									On news, courses and guides, the author
									(<code>user</code>) comes from the token,
									so do not send it.
								</div>
							</div>
						</div>

						<h2 class="section-title">Endpoints</h2>
						<div class="section-divider"></div>

						<div class="grid">
							<div class="card">
								<h3>Users</h3>
								<div class="card-divider"></div>

								<ul>
									<li>
										<span class="m GET">GET</span>
										<code>/users</code>
									</li>

									<li>
										<span class="m GET">GET</span>
										<code>/users/{id}</code>
									</li>

									<li>
										<span class="m POST">POST</span>
										<code>/users</code>
									</li>

									<li>
										<span class="m PATCH">PATCH</span>
										<code>/users/{id}</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m DELETE">DELETE</span>
										<code>/users/{id}</code>
										<span class="tag">JWT</span>
									</li>
								</ul>
							</div>

							<div class="card">
								<h3>Authentication and profile</h3>
								<div class="card-divider"></div>

								<ul>
									<li>
										<span class="m POST">POST</span>
										<code>/users/auth</code>
									</li>

									<li>
										<span class="m GET">GET</span>
										<code>/users/me</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m PATCH">PATCH</span>
										<code>/users/me</code>
										<span class="tag">JWT</span>
									</li>
								</ul>
							</div>

							<div class="card">
								<h3>Agencies</h3>
								<div class="card-divider"></div>

								<ul>
									<li>
										<span class="m GET">GET</span>
										<code>/agencies</code>
									</li>

									<li>
										<span class="m GET">GET</span>
										<code>/agencies/{id}</code>
									</li>

									<li>
										<span class="m POST">POST</span>
										<code>/agencies</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m PATCH">PATCH</span>
										<code>/agencies/{id}</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m DELETE">DELETE</span>
										<code>/agencies/{id}</code>
										<span class="tag">JWT</span>
									</li>
								</ul>
							</div>

							<div class="card">
								<h3>News</h3>
								<div class="card-divider"></div>

								<ul>
									<li>
										<span class="m GET">GET</span>
										<code>/news</code>
									</li>

									<li>
										<span class="m GET">GET</span>
										<code>/news/{id}</code>
									</li>

									<li>
										<span class="m POST">POST</span>
										<code>/news</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m PATCH">PATCH</span>
										<code>/news/{id}</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m DELETE">DELETE</span>
										<code>/news/{id}</code>
										<span class="tag">JWT</span>
									</li>
								</ul>
							</div>

							<div class="card">
								<h3>Guides</h3>
								<div class="card-divider"></div>

								<ul>
									<li>
										<span class="m GET">GET</span>
										<code>/guides</code>
									</li>

									<li>
										<span class="m GET">GET</span>
										<code>/guides/{id}</code>
									</li>

									<li>
										<span class="m POST">POST</span>
										<code>/guides</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m PATCH">PATCH</span>
										<code>/guides/{id}</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m DELETE">DELETE</span>
										<code>/guides/{id}</code>
										<span class="tag">JWT</span>
									</li>
								</ul>
							</div>

							<div class="card">
								<h3>Guide steps</h3>
								<div class="card-divider"></div>

								<ul>
									<li>
										<span class="m GET">GET</span>
										<code>/guides/{guideId}/steps</code>
									</li>

									<li>
										<span class="m GET">GET</span>
										<code>/guides/{guideId}/steps/{id}</code>
									</li>

									<li>
										<span class="m POST">POST</span>
										<code>/guides/{guideId}/steps</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m PATCH">PATCH</span>
										<code>
											/guides/{guideId}/steps/{id}
										</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m DELETE">DELETE</span>
										<code>
											/guides/{guideId}/steps/{id}
										</code>
										<span class="tag">JWT</span>
									</li>
								</ul>
							</div>

							<div class="card">
								<h3>Courses</h3>
								<div class="card-divider"></div>

								<ul>
									<li>
										<span class="m GET">GET</span>
										<code>/courses</code>
									</li>

									<li>
										<span class="m GET">GET</span>
										<code>/courses/{id}</code>
									</li>

									<li>
										<span class="m POST">POST</span>
										<code>/courses</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m PATCH">PATCH</span>
										<code>/courses/{id}</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m DELETE">DELETE</span>
										<code>/courses/{id}</code>
										<span class="tag">JWT</span>
									</li>
								</ul>
							</div>

							<div class="card">
								<h3>Course modules</h3>
								<div class="card-divider"></div>

								<ul>
									<li>
										<span class="m GET">GET</span>
										<code>/courses/{courseId}/modules</code>
									</li>

									<li>
										<span class="m GET">GET</span>
										<code>
											/courses/{courseId}/modules/{id}
										</code>
									</li>

									<li>
										<span class="m POST">POST</span>
										<code>
											/courses/{courseId}/modules
										</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m PATCH">PATCH</span>
										<code>
											/courses/{courseId}/modules/{id}
										</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m DELETE">DELETE</span>
										<code>
											/courses/{courseId}/modules/{id}
										</code>
										<span class="tag">JWT</span>
									</li>
								</ul>
							</div>

							<!-- Este card ocupa as duas colunas -->
							<div class="card module-content">
								<h3>Module content</h3>
								<div class="card-divider"></div>

								<ul>
									<li>
										<span class="m GET">GET</span>
										<code>
											/courses/{courseId}/modules/{moduleId}/content
										</code>
									</li>

									<li>
										<span class="m GET">GET</span>
										<code>
											/courses/{courseId}/modules/{moduleId}/content/{id}
										</code>
									</li>

									<li>
										<span class="m POST">POST</span>
										<code>
											/courses/{courseId}/modules/{moduleId}/content
										</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m PATCH">PATCH</span>
										<code>
											/courses/{courseId}/modules/{moduleId}/content/{id}
										</code>
										<span class="tag">JWT</span>
									</li>

									<li>
										<span class="m DELETE">DELETE</span>
										<code>
											/courses/{courseId}/modules/{moduleId}/content/{id}
										</code>
										<span class="tag">JWT</span>
									</li>
								</ul>
							</div>
						</div>

						<h2 class="section-title">Explore</h2>
						<div class="section-divider"></div>

						<div class="tags">
							<a
								class="tag"
								href="https://polikt-spring.onrender.com/">
								API
							</a>

							<a
								class="tag"
								href="https://polikt.vercel.app">
								App
							</a>

							<a
								class="tag"
								href="https://github.com/ryanmferreira/polikt-spring">
								API repository
							</a>

							<a
								class="tag"
								href="https://github.com/ryanmferreira/polikt-expo">
								App repository
							</a>

							<a
								class="tag"
								href="https://github.com/ryanmferreira/polikt-docs">
								Documentation
							</a>
						</div>

						<footer>
							Polikt is open source under the MIT license.
						</footer>
					</main>
				</body>
				</html>
				""";
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}