package hello.servlet.web;

import hello.servlet.basic.domain.member.Member;
import hello.servlet.basic.domain.member.MemberRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "memberListServlet", urlPatterns = "/servlet/members")
public class MemberListServlet extends HttpServlet {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*
         */
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        List<Member> members = memberRepository.findAll();
        PrintWriter w = response.getWriter();
        w.write("<html>");
        w.write("<head>");
        w.write("    <meta charset=\"UTF-8\">");
        w.write("    <title>Title</title>");
        w.write("</head>");
        w.write("<body>");
        w.write("<a href=\"/index.html\">메인</a>");
        w.write("<table>");
        w.write("    <thead>");
        w.write("    <th>id</th>");
        w.write("    <th>username</th>");
        w.write("    <th>age</th>");
        w.write("    </thead>");
        w.write("    <tbody>");


        for (Member member : members) {
            w.write("    <tr>");
            w.write("<td>" + member.getId() + "</td>");
            w.write("<td>" + member.getUsername() + "</td>");
            w.write("<td>" + member.getAge() + "</td>");
            w.write("    </tr>");
        }

        w.write("</tbody>");
        w.write("</table>");
        w.write("</body>");
        w.write("</html>");
    }
}
//한 줄 한 줄 모두 치는 것이 어려우므로 html에 자바 코드를 끼우면 된다.
//그걸 하는 것이 jsp

//MemberListServlet 은 다음 순서로 동작한다.
//1. memberRepository.findAll() 을 통해 모든 회원을 조회한다.
//2. 회원 목록 HTML을 for 루프를 통해서 회원 수 만큼 동적으로 생성하고 응답한다.

    /*템플릿 엔진으로
    지금까지 서블릿과 자바 코드만으로 HTML을 만들어보았다.
    서블릿 덕분에 동적으로 원하는 HTML을 마음껏 만들 수 있다.
    정적인 HTML 문서라면 화면이 계속 달라지는 회원의 저장 결과라던가, 회원 목록 같은 동적인 HTML을 만드는 일은 불가능 할 것이다.
    그런데, 코드에서 보듯이 이것은 매우 복잡하고 비효율 적이다.
    자바 코드로 HTML을 만들어 내는 것 보다 차라리 HTML 문서에 동적으로 변경해야 하는 부분만 자바 코드를 넣을 수 있다면 더 편리할 것이다.
    이것이 바로 템플릿 엔진이 나온 이유이다.
    템플릿 엔진을 사용하면 HTML 문서에서 필요한 곳만 코드를 적용해서 동적으로 변경할 수 있다.
    템플릿 엔진에는 JSP, Thymeleaf, Freemarker, Velocity등이 있다. 다음 시간에는 JSP로 동일한 작업을 진행해보자.
        > 참고
        > JSP는 성능과 기능면에서 다른 템플릿 엔진과의 경쟁에서 밀리면서, 점점 사장되어 가는 추세이다. 템플릿
        엔진들은 각각 장단점이 있는데, 강의에서는 JSP는 앞부분에서 잠깐 다루고, 스프링과 잘 통합되는 Thymeleaf를 사용한다.

     */