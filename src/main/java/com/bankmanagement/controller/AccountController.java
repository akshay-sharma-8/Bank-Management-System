package com.bankmanagement.controller;

import com.bankmanagement.dto.AccountDTO;
import com.bankmanagement.dto.ApiResponse;
import com.bankmanagement.dto.CreateAccountRequest;
import com.bankmanagement.model.Account;
import com.bankmanagement.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AccountDTO>> createAccount(@RequestBody CreateAccountRequest request) {
        try {
            Account account = accountService.createAccount(request.getUserId(), request.getAccountType());
            AccountDTO accountDTO = accountService.convertToDTO(account); 
            return ResponseEntity.ok(ApiResponse.success("Account created successfully", accountDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountDTO>> getAccountById(@PathVariable Long accountId) {
        try {
            Account account = accountService.getAccountById(accountId);
            AccountDTO accountDTO = accountService.convertToDTO(account);
            return ResponseEntity.ok(ApiResponse.success("Account retrieved successfully", accountDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountDTO>> getAccountByNumber(@PathVariable String accountNumber) {
        try {
            Account account = accountService.getAccountByNumber(accountNumber);
            AccountDTO accountDTO = accountService.convertToDTO(account);
            return ResponseEntity.ok(ApiResponse.success("Account retrieved successfully", accountDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AccountDTO>>> getUserAccounts(@PathVariable Long userId) {
        try {
            List<AccountDTO> accounts = accountService.getUserAccounts(userId);
            return ResponseEntity.ok(ApiResponse.success("Accounts retrieved successfully", accounts));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{accountId}/status")
    public ResponseEntity<ApiResponse<String>> updateAccountStatus(@PathVariable Long accountId,
                                                                   @RequestBody Map<String, Boolean> request) {
        try {
            boolean active = request.get("active");
            accountService.updateAccountStatus(accountId, active);
            String status = active ? "activated" : "deactivated";
            return ResponseEntity.ok(ApiResponse.success("Account " + status + " successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<ApiResponse<AccountDTO>> checkBalance(@PathVariable Long accountId) {
        try {
            Account account = accountService.getAccountById(accountId);
            AccountDTO accountDTO = accountService.convertToDTO(account);
            return ResponseEntity.ok(ApiResponse.success("Balance retrieved successfully", accountDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<ApiResponse<String>> deleteAccount(@PathVariable Long accountId) {
        try {
            accountService.deleteAccount(accountId);
            return ResponseEntity.ok(ApiResponse.success("Account deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}