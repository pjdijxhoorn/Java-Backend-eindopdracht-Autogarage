package com.example.garage.Controllers;

import com.example.garage.Payload.AuthenticationRequest;
import com.example.garage.Payload.AuthenticationResponse;
import com.example.garage.Services.CustomUserDetailsService;
import com.example.garage.Utilities.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;

    final
    JwtUtil jwtUtl;

    public AuthenticationController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtil jwtUtl) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtl = jwtUtl;
    }

    @GetMapping(value = "/authenticated")
    public ResponseEntity<Object> authenticated(Authentication authentication, Principal principal) {
        return ResponseEntity.ok().body(principal);
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException ex) {
            throw new Exception("Incorrect username or password", ex);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(username);

        final String jwt = jwtUtl.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @GetMapping( "/info")
    public ResponseEntity<String> info() {
        String info = """
                Here follow all the possible commands for this application:
                
                ----------Cars!----------
                localhost:8080/cars
                localhost:8080/cars/{id}
                localhost:8080/cars/licenseplate/{licenseplate}
                localhost:8080/cars/user
                localhost:8080/cars/user/status
                localhost:8080/cars   * POST
                localhost:8080/cars/{id} * PUT
                localhost:8080/cars/statusdesk/{carstatus}/{licenseplate}
                localhost:8080/cars/statusmechanic/{carstatus}/{licenseplate}
                localhost:8080/cars/{id} *DELETE

                ----------Invoices!----------
                localhost:8080/invoices
                localhost:8080/invoices/{id}
                localhost:8080/invoices/user
                localhost:8080/invoices/{id}/getpdfinvoice
                localhost:8080/invoices/{id} * POST
                localhost:8080/invoices/{id}/payed
                localhost:8080/invoices/{id}/generateInvoicePdf
                localhost:8080/invoices/{id}/sendinvoice
                localhost:8080/invoices/{id}

                ----------Maintenances!----------
                localhost:8080/maintenances
                localhost:8080/maintenances/{id}
                localhost:8080/maintenances/user
                localhost:8080/maintenances/{id} *POST\s
                localhost:8080/maintenances/{id}/mechanicdone
                localhost:8080/maintenances/{id}/approvaluser
                localhost:8080/maintenances/{id} *DELETE\s

                ----------Repairs!----------
                localhost:8080/repairs/{licenseplate}
                localhost:8080/repairs/{carpartname}/{car-id}
                localhost:8080/repairs/{car-id}/setrepaired
                localhost:8080/repairs/{repair-id}

                ----------Users!----------
                localhost:8080/users
                localhost:8080/users/{username}
                localhost:8080/users/{username}/authorities
                localhost:8080/users *POST
                localhost:8080/users/{username}/authorities
                localhost:8080/users/{username} *PUT
                localhost:8080/users/{username} *DELETE
                localhost:8080/users/{username}/authorities/{USER_ROLE}

                ----------Carparts!----------
                localhost:8080/carparts/{licenseplate}
                localhost:8080/carparts/{licenseplate}/inspection/{CARPART}

                ----------Carpapers!----------
                localhost:8080/carpapers/getpdfcarpapers/{licenseplate}
                localhost:8080/carpapers/upload/{username}

                ----------Auth!----------
                localhost:8080/authenticated
                localhost:8080/authenticate
                
                {} = fill in with certain code.
                * = this is double methode however a differant type of request.
                """;

        return ResponseEntity.ok().body(info);
    }

}