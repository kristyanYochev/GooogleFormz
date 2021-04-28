package org.elsys.ip.forms.auth

import org.springframework.context.annotation.*
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.*
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class AuthConfig(
        private val usersService: UserDetailsService,
        private val authEntryPoint: AuthEntryPoint,
        private val authRequestFilter: AuthRequestFilter
) : WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(usersService).passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    override fun configure(http: HttpSecurity?) {
        http!!
                .authorizeRequests()
                .antMatchers("/login", "/register", "/", "/polls").permitAll()
                .antMatchers(HttpMethod.GET, "/polls/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/polls/{id}").permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.addFilterBefore(authRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}