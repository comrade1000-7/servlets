package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private static final String GET = "GET";
  private static final String POST = "POST";
  private static final String DELETE = "DELETE";
  private static final String POSTS_ENDPOINT = "/api/posts";
  private static final String ID_POSTS_ENDPOINT = "/api/posts/\\d+";
  private static final String PATH_DELIMITER = "/";


  @Override
  public void init() {
    /*final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);*/

    // отдаём список пакетов, в которых нужно искать аннотированные классы
    final var context = new AnnotationConfigApplicationContext("ru.netology");

    // получаем по имени бина
    final var controller = context.getBean("postController");

    // получаем по имени бина
    final var repository = context.getBean("postRepository");

    // получаем по классу бина
    final var service = context.getBean(PostService.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals(GET) && path.equals(POSTS_ENDPOINT)) {
        controller.all(resp);
        return;
      }
      if (method.equals(GET) && path.matches(ID_POSTS_ENDPOINT)) {
        // easy way
        final var id = Long.parseLong(path.substring(path.lastIndexOf(PATH_DELIMITER)));
        controller.getById(id, resp);
        return;
      }
      if (method.equals(POST) && path.equals(POSTS_ENDPOINT)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(DELETE) && path.matches(ID_POSTS_ENDPOINT)) {
        // easy way
        final var id = Long.parseLong(path.substring(path.lastIndexOf(PATH_DELIMITER)));
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}
