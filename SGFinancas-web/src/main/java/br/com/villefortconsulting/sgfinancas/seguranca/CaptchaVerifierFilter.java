/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.seguranca;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author Christopher //http://krams915.blogspot.com.br/2011/02/spring-security-3-integrating-recaptcha.html
 * http://www.journaldev.com/7133/how-to-integrate-google-recaptcha-in-java-web-application
 */
@Component
public class CaptchaVerifierFilter extends OncePerRequestFilter {

    private boolean isCaptchaOK = false;

    private String recaptchaResponse;

    @Override
    public void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        recaptchaResponse = req.getParameter("g-recaptcha-response");

        if (recaptchaResponse != null) {

            setIsCaptchaOK(VerifyRecaptcha.verify(recaptchaResponse));

            resetCaptchaFields();
        }
        chain.doFilter(req, res);
    }

    /**
     * Reset Captcha fields
     */
    public void resetCaptchaFields() {
        recaptchaResponse = null;
    }

    public boolean isIsCaptchaOK() {
        return isCaptchaOK;
    }

    public void setIsCaptchaOK(boolean isCaptchaOK) {
        this.isCaptchaOK = isCaptchaOK;
    }

}
