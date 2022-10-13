package jjfactory.webclient.global.ex;

import com.google.gson.Gson;
import jjfactory.webclient.global.dto.res.ErrorResponse;
import jjfactory.webclient.global.slack.SlackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.persistence.EntityNotFoundException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class ExceptionFilter extends OncePerRequestFilter {

    private final SlackService slackService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (EntityNotFoundException ex){
            log.error("exception handler filter");
            slackService.postSlackMessage("jwt관련 에러가 발생했습니다." + ex.getMessage());
            setErrorResponse(HttpStatus.FORBIDDEN,response,ex);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response,Throwable ex){
        response.setStatus(status.value());
        response.setContentType("application/json");
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.HANDLE_ACCESS_DENIED);
        try{
            Gson gson = new Gson();
            String json = gson.toJson(errorResponse);
            //String json = errorResponse.convertToJson();
            System.out.println(json);
            response.getWriter().write(json);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}