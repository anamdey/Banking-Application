package com.capgemini.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.capgemini.exceptions.InsufficientBalanceException;
import com.capgemini.exceptions.InsufficientInitialBalanceException;
import com.capgemini.exceptions.InvalidAccountNumberException;
import com.capgemini.model.Account;
import com.capgemini.repository.AccountRepository;
import com.capgemini.service.AccountService;
import com.capgemini.service.AccountServiceImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
public class AccountTest {

	AccountService accountService ;
	Account account;
	@Mock
	AccountRepository accountRepository;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		accountService = new AccountServiceImpl(accountRepository);
		
		account = new Account();
		account.setAccountNumber(101);
		account.setAmount(5000);
	}

	/*  create account
	 *  1.when the amount is less than 500 system should throw exception
	 *  2.when the valid info is passed account should be created successfully
	 */
	
	
	@Test(expected = com.capgemini.exceptions.InsufficientInitialBalanceException.class)
	public void whenTheAmountIsLessThan500SystemShouldThrowException() throws InsufficientInitialBalanceException 
	{
		
		accountService.createAccount(101, 200);
	}
	
	
	
	@Test
	public void whenTheValidInfoIsPassedAccountShouldBeCreatedSuccessfully() throws InsufficientInitialBalanceException
	{
	
		
		when(accountRepository.save(account)).thenReturn(true);
		
		assertEquals(account,accountService.createAccount(101, 5000));
		
	}
	
	/*
	 * Search an account
	 * 1. When the account number is invalid system should throw exception
	 * 2. when the valid info is passed then the account balance will be showed
	 */
	
	@Test(expected = com.capgemini.exceptions.InvalidAccountNumberException.class)
	public void whenAccountNumberIsNullThenSystemShouldGiveException() throws InvalidAccountNumberException {
		
		when(accountRepository.searchAccount(0)).thenReturn(null);
		accountService.showBalance(200);
		
	}
	
	@Test
	public void whenAccountNumberIsSameThenSystemShouldReturnBalance() throws InvalidAccountNumberException {
		
		when(accountRepository.searchAccount(101)).thenReturn(account);
		assertEquals(account.getAmount(), accountService.showBalance(101));
		
	}
	
	/*
	 * Search an account
	 * 1. When the account number is invalid system should throw exception
	 * 2. when the valid info is passed then the amount will be deposited
	 */
	
	@Test(expected = com.capgemini.exceptions.InvalidAccountNumberException.class)
	public void whenAccountNumberIsInvalidForDepositThenThrowException() throws InvalidAccountNumberException {
		
		when(accountRepository.searchAccount(0)).thenReturn(null);
		accountService.depositAmount(200, 2000);
	}
	
	@Test
	public void depositAmount() throws InvalidAccountNumberException {
		
		when(accountRepository.searchAccount(101)).thenReturn(account);
		assertEquals(6000, accountService.depositAmount(101, 1000));
	}
	
	/*
	 * Search an account
	 * 1. When the account number is invalid system should throw exception
	 * 2. When the withdrawal amount is more than available balance system should throw exception
	 * 3. when the valid info is passed then the amount will be withdrawal
	 */
	
	@Test(expected = com.capgemini.exceptions.InvalidAccountNumberException.class)
	public void whenAccountNumberIsNullForWithdrawlThenSystemShouldGiveException() throws InvalidAccountNumberException, InsufficientBalanceException {
		
		when(accountRepository.searchAccount(200)).thenReturn(null);
		accountService.withdrawAmount(200, 1000);
		Mockito.verify(accountRepository).searchAccount(200);
		
	}
	
	@Test(expected = com.capgemini.exceptions.InsufficientBalanceException.class)
	public void whenWithdrawalAmountIsMoreThanAvailableBalanceThenSystemShoulGiveException() throws InvalidAccountNumberException, InsufficientBalanceException {
		
		when(accountRepository.searchAccount(101)).thenReturn(account);
		accountService.withdrawAmount(101, 10000);
		Mockito.verify(accountRepository).searchAccount(101);
	}
	
	@Test
	public void withdrawalAmount() throws InvalidAccountNumberException, InsufficientBalanceException {
		when(accountRepository.searchAccount(101)).thenReturn(account);
		assertEquals(4000, accountService.withdrawAmount(101, 1000));
	}
	
	
	
	
	
	
	
	
	

}
