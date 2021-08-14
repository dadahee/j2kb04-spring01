package hello.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet 서블릿 애노테이션
//name: 서블릿 이름
//urlPatterns: URL 매핑

//서블릿: 서버에서 웹페이지 등을 동적으로 생성하거나 데이터 처리를 수행하기 위해 자바(Java)로 작성된 프로그램을 말함.
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
        //HTTP 요청을 통해 매핑된 URL이 호출되면 서블릿 컨테이너는 아래의 메서드를 실행함.
        @Override
        protected void service(HttpServletRequest request, HttpServletResponse
                response)
                throws ServletException, IOException {
                System.out.println("HelloServlet.service");
                System.out.println("request = " + request);
                System.out.println("response = " + response);
                String username = request.getParameter("username");
                System.out.println("username = " + username);
                response.setContentType("text/plain");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write("hello " + username);
        }
}
