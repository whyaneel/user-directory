package com.prototype.userdirectory.security

import com.google.common.base.Strings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import com.prototype.userdirectory.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FirebaseAuthenticationFilter(
    private val fireBaseAuth: FirebaseAuth,
    authManager: AuthenticationManager
) : BasicAuthenticationFilter(authManager) {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val idToken: String? = request.getBearerToken()
            if (Strings.isNullOrEmpty(idToken)) throw NotFoundException("No JWT token provided")
            SecurityContextHolder.getContext().authentication = buildAuthentication(fireBaseAuth.verifyIdToken(idToken))
            filterChain.doFilter(request, response)
        } catch (ex: FirebaseAuthException) {
            response.setCorsHeaders()
            response.sendError(HttpStatus.FORBIDDEN.value(), ex.message)
        } catch (ex: NotFoundException) {
            response.setCorsHeaders()
            response.sendError(HttpStatus.NOT_FOUND.value(), ex.message)
        }
    }

    private fun HttpServletResponse.setCorsHeaders() {
        addHeader("Access-Control-Expose-Headers", "Authorization")
        addHeader("Access-Control-Allow-Headers", "Authorization")
        addHeader("Access-Control-Allow-Origin", "*")
    }

    private fun HttpServletRequest.getBearerToken(): String? {
        val bearerToken: String? = getHeader("Authorization")
        if (bearerToken is String && bearerToken.length > "Bearer ".length && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring("Bearer ".length, bearerToken.length)
        }
        return null
    }

    private fun buildAuthentication(firebaseToken: FirebaseToken): Authentication =
        UsernamePasswordAuthenticationToken(firebaseToken.email, "", AuthorityUtils.createAuthorityList("ADMIN"))
}
