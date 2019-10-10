package com.prototype.userdirectory.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import com.google.firebase.FirebaseApp
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseOptions
import org.springframework.core.io.ClassPathResource
import java.io.FileInputStream
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

@EnableWebSecurity
@Configuration
class UserDirectoryAppWebSecurityConfigurerAdapter : WebSecurityConfigurerAdapter() {
    override fun configure(web: WebSecurity) {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS)
            .antMatchers("/assets/**")
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .antMatcher("/api/**")
            .addFilter(FirebaseAuthenticationFilter(firebaseAuth(), authenticationManager()))
    }

    @Bean
    fun firebaseAuth(): FirebaseAuth {
        val serviceAccount = ClassPathResource("userdirectory-service_account.json").inputStream
        val defaultOptions = FirebaseOptions.builder().apply {
            setCredentials(GoogleCredentials.fromStream(serviceAccount))
        }.build()
        val initializeApp = FirebaseApp.initializeApp(defaultOptions)
        return FirebaseAuth.getInstance(initializeApp)
    }
}