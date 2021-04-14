package org.elsys.ip.forms

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.*

@Configuration
@EnableWebSecurity
class AuthConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()?.antMatchers("/**")?.permitAll()
    }
}