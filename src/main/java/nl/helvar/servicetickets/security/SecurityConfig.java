package nl.helvar.servicetickets.security;

import nl.helvar.servicetickets.roles.RoleRepository;
import nl.helvar.servicetickets.users.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtService service,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          UserDetailsService userDetailsService)
    {
        this.jwtService = service;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService(this.userRepository, this.roleRepository);
    }


    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {
        return http
                //.securityContext((securityContext) -> securityContext.requireExplicitSave(true))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/privileges/**").hasAuthority("CAN_MODIFY_USERS_PRIVILEGE")
                                .requestMatchers("/roles/**").hasAuthority("CAN_MODIFY_USERS_PRIVILEGE")
                                //.requestMatchers(HttpMethod.POST, "/users/**").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/users/**").hasAuthority("READ_PRIVILEGE")
//                                .requestMatchers(HttpMethod.PUT, "/roles/**").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/roles/**").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/roles/**").permitAll()

//                        .requestMatchers(HttpMethod.GET, "/projects/**", "/serviceContracts/**")
//                        .hasRole("USER")
//
//                        .requestMatchers(HttpMethod.GET, "/serviceTickets/**")
//                        .hasRole("USER")
//
//                        .requestMatchers(HttpMethod.POST, "/serviceTickets/**")
//                        .hasRole("USER")
//
//                        .requestMatchers(HttpMethod.POST, "/projects/**")
//                        .hasRole("ENGINEER")
//
//                        .requestMatchers(HttpMethod.POST, "/serviceContracts/**")
//                        .hasRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.PUT, "/serviceTickets/**")
//                        .hasRole("ENGINEER")
//
//                        .requestMatchers(HttpMethod.PUT, "/projects/**", "/serviceContracts/**")
//                        .hasRole("ADMIN")
//
//                        .requestMatchers(HttpMethod.DELETE, "/serviceTickets/**")
//                        .hasRole("USER")
//
//                        .requestMatchers(HttpMethod.DELETE, "/projects/**", "/serviceContracts/**")
//                        .hasRole("ADMIN")
//
//                        .requestMatchers("/ticketResponses/**")
//                        .hasRole("USER")
//
//                        .requestMatchers("/users/**")
//                        .permitAll()

                        .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtRequestFilter(jwtService, userDetailsService()), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_ENGINEER \n ROLE_ENGINEER > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

//    @Bean
//    public AuthenticationManager authenticationManager(UserDetailsService udService, PasswordEncoder passwordEncoder) {
//        var auth = new DaoAuthenticationProvider();
//        auth.setPasswordEncoder(passwordEncoder);
//        auth.setUserDetailsService(udService);
//        return new ProviderManager(auth);
//    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider())
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//
//        authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource)
//                .usersByUsernameQuery("SELECT username, password, enabled" +
//                        " FROM users" +
//                        " WHERE username=?")
//                .authoritiesByUsernameQuery("SELECT username, authority" +
//                        " FROM authorities " +
//                        " WHERE username=?");
//        return authenticationManagerBuilder.build();
//    }
}
