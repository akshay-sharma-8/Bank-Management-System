package com.bankmanagement.controller;

import com.bankmanagement.dto.ApiResponse;
import com.bankmanagement.dto.DepositWithdrawRequest;
import com.bankmanagement.dto.TransactionDTO;
import com.bankmanagement.dto.TransferRequest;
import com.bankmanagement.model.Transaction;
import com.bankmanagement.service.TransactionService;
import com.bankmanagement.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<Transaction>> deposit(@RequestBody DepositWithdrawRequest request) {
        try {
            if (!ValidationUtil.isValidAmount(request.getAmount())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid amount"));
            }

            Transaction transaction = transactionService.deposit(
                    request.getAccountNumber(),
                    request.getAmount(),
                    request.getDescription());

            return ResponseEntity.ok(ApiResponse.success("Deposit successful", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<Transaction>> withdraw(@RequestBody DepositWithdrawRequest request) {
        try {
            if (!ValidationUtil.isValidAmount(request.getAmount())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid amount"));
            }

            Transaction transaction = transactionService.withdraw(
                    request.getAccountNumber(),
                    request.getAmount(),
                    request.getDescription());

            return ResponseEntity.ok(ApiResponse.success("Withdrawal successful", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<Transaction>> transfer(@RequestBody TransferRequest request) {
        try {
            if (!ValidationUtil.isValidAmount(request.getAmount())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid amount"));
            }

            Transaction transaction = transactionService.transfer(
                    request.getFromAccountNumber(),
                    request.getToAccountNumber(),
                    request.getAmount(),
                    request.getDescription());

            return ResponseEntity.ok(ApiResponse.success("Transfer successful", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/history/{accountId}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionHistory(@PathVariable Long accountId) {
        try {
            List<TransactionDTO> transactions = transactionService.getAccountTransactions(accountId);
            return ResponseEntity.ok(ApiResponse.success("Transaction history retrieved successfully", transactions));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getAllTransactions() {
        try {
            List<TransactionDTO> transactions = transactionService.getAllTransactions();
            return ResponseEntity.ok(ApiResponse.success("All transactions retrieved successfully", transactions));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}