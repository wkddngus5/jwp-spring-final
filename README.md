# 자바 웹 프로그래밍 최종 점검
## 최종 점검 문서 
* JWP Basic 최종 점검.pdf 문서를 참고해 최종 점검 진행

## 질문에 대한 답변
#### 2. Tomcat 서버를 시작 과정을 설명하시오.
Servlet Container인  Tomcat은 
AppConfig.java에 있는 ComponentScan을 통해 class들을 읽어서 
이들 중 servlet의 requestmapping 애노테이션이 있는 method를 찾아
map에 각 요청 url에 따라 method를 저장해놓고, DB연결 등 초기화를 마친 후
클라이언트의 요청을 기다린다.

#### 3. http://localhost:8080 으로 요청했을 때의 과정을 설명하시오.
Servlet filter에서 인코딩 등의 기본적인 과정을 거치고 DisptcherServlet에서 
RequestMapping에 요청 'http://localhost:8080' url을 통해 
HomeController를 찾는다.
HomeController에서는 index.jsp파일과 DB에 있는 Questions를 찾아서 
modelAndView에 담아 다시 필터를 거친 후 응답해준다.  

#### 6. QuestionController가 multi thread에서 문제가 되는 이유를 설명하시오.
