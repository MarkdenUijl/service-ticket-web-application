package nl.helvar.servicetickets.security;

import nl.helvar.servicetickets.roles.RoleRepository;
import nl.helvar.servicetickets.users.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtService service,
                          UserRepository userRepository,
                          UserDetailsService userDetailsService)
    {
        this.jwtService = service;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService(this.userRepository);
    }


    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/test").permitAll()

                                .requestMatchers("/privileges/**")
                                .hasAuthority("CAN_MODIFY_USERS_PRIVILEGE")

                                .requestMatchers("/roles/**")
                                .hasAuthority("CAN_MODIFY_USERS_PRIVILEGE")

                                .requestMatchers(HttpMethod.POST, "/users/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/users/**")
                                .hasAuthority("CAN_MODIFY_USERS_PRIVILEGE")
                                .requestMatchers(HttpMethod.GET, "/users")
                                .hasAuthority("CAN_ACCESS_USERS_PRIVILEGE")
                                .requestMatchers(HttpMethod.GET, "/users/**")
                                .hasAuthority("CAN_SEE_USERS_PRIVILEGE")
                                .requestMatchers(HttpMethod.PUT, "/users/**")
                                .authenticated()

                                .requestMatchers(HttpMethod.GET, "/serviceContracts/**")
                                .hasAuthority("CAN_SEE_CONTRACTS_PRIVILEGE")
                                .requestMatchers("/serviceContracts/**")
                                .hasAuthority("CAN_MODIFY_CONTRACTS_PRIVILEGE")

                                .requestMatchers(HttpMethod.GET, "/projects/**")
                                .authenticated()
                                .requestMatchers("/projects/**")
                                .hasAuthority("CAN_MODIFY_PROJECTS_PRIVILEGE")

                                .requestMatchers(HttpMethod.GET, "/serviceTickets")
                                .hasAuthority("CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE")
                                .requestMatchers(HttpMethod.DELETE, "/serviceTickets/*/files/**")
                                .hasAuthority("CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE")
                                .requestMatchers("/serviceTickets/**")
                                .authenticated()
                                .requestMatchers("/serviceTickets/*/files/**")
                                .authenticated()

                                .requestMatchers(HttpMethod.GET, "/ticketResponses")
                                .hasAuthority("CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE")
                                .requestMatchers("/ticketResponses/**")
                                .authenticated()

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
}
