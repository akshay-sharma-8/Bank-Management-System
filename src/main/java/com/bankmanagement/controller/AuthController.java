package com.bankmanagement.controller;

import com.bankmanagement.dto.ApiResponse;
import com.bankmanagement.dto.LoginRequest;
import com.bankmanagement.dto.UserDTO;
import com.bankmanagement.model.User;
import com.bankmanagement.service.AuthService;
import com.bankmanagement.service.UserService;
import com.bankmanagement.util.JwtUtil;
import com.bankmanagement.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // *** THIS METHOD IS UPDATED ***
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String email = request.get("email");
            String password = request.get("password");
            String phone = request.get("phone");

            if (!ValidationUtil.isNotEmpty(name) || !ValidationUtil.isValidEmail(email) ||
                    !ValidationUtil.isValidPassword(password) || !ValidationUtil.isValidPhone(phone)) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid input data"));
            }

            UserDTO userDTO = new UserDTO();
            userDTO.setName(name);
            userDTO.setEmail(email);
            userDTO.setPhone(phone);
            userDTO.setAddress(request.get("address"));
            userDTO.setPanNumber(request.get("panNumber"));

            User user = userService.createUser(userDTO, password);
            String token = jwtUtil.generateToken(email, "USER");

            // Convert the new user to a DTO for the response
            UserDTO userDTOForResponse = userService.convertToDTO(user);

            Map<String, Object> response = new HashMap<>();
            response.put("user", userDTOForResponse); // Put the DTO in the response
            response.put("token", token);

            return ResponseEntity.ok(ApiResponse.success("User registered successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Map<String, Object> userInfo = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
            String token = jwtUtil.generateToken(loginRequest.getEmail(), "USER");

            Map<String, Object> response = new HashMap<>();
            response.put("user", userInfo);
            response.put("token", token);

            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> adminLogin(@RequestBody LoginRequest loginRequest) {
        try {
            Map<String, Object> adminInfo = authService.authenticateAdmin(loginRequest.getEmail(), loginRequest.getPassword());
            String token = jwtUtil.generateToken(loginRequest.getEmail(), "ADMIN");

            Map<String, Object> response = new HashMap<>();
            response.put("admin", adminInfo);
            response.put("token", token);

            return ResponseEntity.ok(ApiResponse.success("Admin login successful", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}