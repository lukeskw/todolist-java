package com.porfiriodev.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.porfiriodev.todolist.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        var serverletPath = request.getServletPath();

        if (!serverletPath.startsWith("/tasks/")){
            filterChain.doFilter(request, response);
            return;
        }

        var auth = request.getHeader("Authorization");

        var authEncoded = auth.substring("Basic".length()).trim();

        byte[] authDecode = Base64.getDecoder().decode(authEncoded);

        var authString = new String(authDecode);

        String[] credentials = authString.split(":");
        String username = credentials[0];
        String password = credentials[1];

        var user = this.userRepository.findByUsername(username);

        if(user == null){
            response.sendError(401);
            return;
        }

        var passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray());

        if(passwordVerified.verified){
            request.setAttribute("idUser", user.getId());
            filterChain.doFilter(request, response);
            return;
        }
        response.sendError(401);
    }
}
