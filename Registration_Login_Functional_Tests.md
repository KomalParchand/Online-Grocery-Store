# Functional Test Cases for Registration and Login Flow

## Overview
This document contains comprehensive functional test cases for the SlipKART e-commerce application's registration and login features, covering both customer and staff user types.

## Application Components Tested
- Customer Registration (`Custegistration.jsp` → `Register.jsp`)
- Customer Login (`customer_login.jsp` → `Clogin.jsp`)
- Staff Registration (`Staffregistration.jsp` → `SRegister.jsp`)

---

## 1. Customer Registration Test Cases

### Test Case CR-001: Successful Customer Registration with Complete Information
**Objective:** Verify that a new customer can successfully register with all required information
**Prerequisites:** Database is accessible and running
**Test Data:**
- First Name: "John"
- Last Name: "Doe"
- Username: "johndoe123"
- Password: "password123"
- Email: "john.doe@email.com"
- Permanent Address: Yes, Street: "123 Main St", City: "Chicago", State: "Illinois", Zip: "60601"
- Delivery Address: Yes, Street: "456 Oak Ave", City: "Houston", State: "Texas", Zip: "77001"
- Credit Card: "1234567890123456", CVV: "123", Expiry: "01/Jan/2025"

**Steps:**
1. Navigate to customer registration page (`Custegistration.jsp`)
2. Fill all required fields with test data
3. Select "Yes" for Permanent Address radio button
4. Fill permanent address details
5. Select "Yes" for Delivery Address radio button
6. Fill delivery address details
7. Enter credit card information
8. Click "Submit" button

**Expected Result:**
- User is redirected to `Customerhome.jsp`
- Session attribute "uname" is set to "johndoe123"
- Customer record is inserted into CUSTOMER table
- Address records are inserted into ADDRESS table for both permanent and delivery
- Credit card record is inserted into CREDITCARD_INFO table
- Page displays "Logged in as: johndoe123"

**Acceptance Criteria:**
- ✅ Registration completes without errors
- ✅ User session is established
- ✅ Database records are created correctly
- ✅ User is automatically logged in post-registration

---

### Test Case CR-002: Customer Registration with Only Permanent Address
**Objective:** Verify registration works when user selects only permanent address
**Test Data:**
- Same personal info as CR-001
- Permanent Address: Yes
- Delivery Address: No

**Steps:**
1. Navigate to customer registration page
2. Fill personal information
3. Select "Yes" for Permanent Address only
4. Select "No" for Delivery Address
5. Submit form

**Expected Result:**
- Registration succeeds
- Only one address record created with type "PERMANENT"
- User redirected to customer home page

---

### Test Case CR-003: Customer Registration with Missing Required Fields
**Objective:** Verify proper validation for required fields
**Test Data:** Leave one required field empty at a time

**Steps:**
1. Navigate to registration page
2. Leave "First Name" empty, fill other fields
3. Submit form
4. Repeat for each required field: Last Name, Username, Password, Email, Credit Card, CVV

**Expected Result:**
- HTML5 validation prevents form submission
- Appropriate error messages displayed
- User remains on registration page

---

### Test Case CR-004: Customer Registration with Duplicate Username
**Objective:** Verify system handles duplicate username registration
**Test Data:** Use existing username from previous successful registration

**Steps:**
1. Complete registration with username "johndoe123" (assuming CR-001 passed)
2. Attempt registration with same username but different email

**Expected Result:**
- Registration should fail gracefully
- Database constraint should prevent duplicate entry
- User should see appropriate error message

---

### Test Case CR-005: Customer Registration with Invalid Email Format
**Objective:** Verify email validation
**Test Data:** 
- Invalid emails: "invalid-email", "@domain.com", "user@", "user.domain"

**Steps:**
1. Fill registration form with invalid email formats
2. Submit form

**Expected Result:**
- HTML5 email validation prevents submission
- Error message indicates invalid email format

---

### Test Case CR-006: Customer Registration with Invalid Credit Card Information
**Objective:** Verify credit card validation
**Test Data:**
- Invalid card numbers: "123", "abcd1234", "0000000000000000"
- Invalid CVV: "12", "abcd"

**Steps:**
1. Fill form with invalid credit card data
2. Submit form

**Expected Result:**
- Form validation prevents submission
- Appropriate error messages displayed

---

## 2. Customer Login Test Cases

### Test Case CL-001: Successful Customer Login
**Objective:** Verify registered customer can log in successfully
**Prerequisites:** Customer "johndoe123" is registered (CR-001 completed)
**Test Data:**
- Username: "johndoe123"
- Password: "password123"

**Steps:**
1. Navigate to customer login page (`customer_login.jsp`)
2. Enter valid username and password
3. Click "Submit" button

**Expected Result:**
- User is redirected to `Customerhome.jsp`
- Session attribute "uname" is set to "johndoe123"
- Page displays "Logged in as: johndoe123"
- Navigation menu is visible with options: HomePage, View Profile, Orders, Edit Profile, Shopping Cart, Logout

**Acceptance Criteria:**
- ✅ Login authentication succeeds
- ✅ User session is established
- ✅ Correct user home page is displayed
- ✅ User-specific content is shown

---

### Test Case CL-002: Login with Invalid Username
**Objective:** Verify system handles non-existent username
**Test Data:**
- Username: "nonexistentuser"
- Password: "anypassword"

**Steps:**
1. Navigate to login page
2. Enter non-existent username with any password
3. Submit form

**Expected Result:**
- User is redirected to `invalidLogin.jsp`
- No session is established
- Login fails with appropriate error message

---

### Test Case CL-003: Login with Invalid Password
**Objective:** Verify system handles incorrect password for valid username
**Test Data:**
- Username: "johndoe123" (valid existing user)
- Password: "wrongpassword"

**Steps:**
1. Navigate to login page
2. Enter valid username with incorrect password
3. Submit form

**Expected Result:**
- User is redirected to `invalidLogin.jsp`
- No session is established
- Authentication fails

---

### Test Case CL-004: Login with Empty Credentials
**Objective:** Verify required field validation on login form
**Test Data:** Empty username and/or password

**Steps:**
1. Navigate to login page
2. Leave username field empty, enter password
3. Submit form
4. Repeat with password field empty
5. Submit with both fields empty

**Expected Result:**
- HTML5 validation prevents form submission
- "required" attribute displays appropriate browser messages
- Form does not submit until both fields are filled

---

### Test Case CL-005: SQL Injection Prevention in Login
**Objective:** Verify login is protected against SQL injection attacks
**Test Data:**
- Username: "admin' OR '1'='1"
- Password: "password"

**Steps:**
1. Navigate to login page
2. Enter SQL injection payload in username field
3. Submit form

**Expected Result:**
- Login fails (redirected to invalidLogin.jsp)
- No unauthorized access granted
- Application remains secure

---

## 3. Staff Registration Test Cases

### Test Case SR-001: Successful Staff Registration
**Objective:** Verify staff member can register successfully
**Test Data:**
- First Name: "Jane"
- Last Name: "Smith"
- Username: "janesmith"
- Password: "staffpass123"
- Job Title: "Store Manager"
- Salary: "50000"
- Street: "789 Business Blvd"
- City: "Chicago"
- State: "Illinois"
- Zipcode: "60602"

**Steps:**
1. Navigate to staff registration page (`Staffregistration.jsp`)
2. Fill all required fields
3. Submit form

**Expected Result:**
- Staff record inserted into STAFF table
- User redirected to `Staffhomepage.jsp`
- Registration completes successfully

---

### Test Case SR-002: Staff Registration with Missing Required Fields
**Objective:** Verify validation for staff registration form
**Test Data:** Missing required fields one at a time

**Steps:**
1. Leave each required field empty individually
2. Submit form

**Expected Result:**
- HTML5 validation prevents submission
- Appropriate error messages displayed

---

### Test Case SR-003: Staff Registration with Invalid Salary
**Objective:** Verify salary field validation
**Test Data:**
- Salary: "invalid", "-1000", "abc123"

**Steps:**
1. Fill form with invalid salary values
2. Submit form

**Expected Result:**
- Validation should prevent submission of non-numeric salary values

---

## 4. Session Management Test Cases

### Test Case SM-001: Session Persistence After Login
**Objective:** Verify user session persists across page navigation
**Prerequisites:** User successfully logged in

**Steps:**
1. Log in as customer
2. Navigate to different pages within the application
3. Verify session information is maintained

**Expected Result:**
- Username appears on all pages as "Logged in as: [username]"
- Session remains active across page navigation

---

### Test Case SM-002: Session Termination on Logout
**Objective:** Verify user session is properly terminated
**Prerequisites:** User is logged in

**Steps:**
1. Log in as customer
2. Click "Logout" link
3. Attempt to access protected pages directly via URL

**Expected Result:**
- User is logged out
- Session is invalidated
- Accessing protected pages requires re-authentication

---

## 5. Database Integration Test Cases

### Test Case DB-001: Database Connection Failure Handling
**Objective:** Verify application handles database connection failures gracefully
**Prerequisites:** Ability to simulate database connection failure

**Steps:**
1. Simulate database connection failure
2. Attempt registration
3. Attempt login

**Expected Result:**
- Application doesn't crash
- Appropriate error handling is implemented
- User sees meaningful error message

---

### Test Case DB-002: Data Integrity After Registration
**Objective:** Verify all related records are created correctly
**Prerequisites:** Access to database for verification

**Steps:**
1. Complete customer registration
2. Query database tables: CUSTOMER, ADDRESS, CREDITCARD_INFO
3. Verify data consistency and foreign key relationships

**Expected Result:**
- Customer ID is properly generated using sequence
- Address records reference correct customer ID
- Credit card record references correct customer ID
- No orphaned records created

---

## 6. Cross-Browser Compatibility Test Cases

### Test Case CB-001: Registration and Login in Different Browsers
**Objective:** Verify functionality works across major browsers
**Test Browsers:** Chrome, Firefox, Safari, Edge

**Steps:**
1. Test complete registration flow in each browser
2. Test login flow in each browser
3. Verify form validation works consistently

**Expected Result:**
- Functionality works consistently across all browsers
- Form validation behaves similarly
- Redirects work properly in all browsers

---

## 7. Performance Test Cases

### Test Case PF-001: Registration Performance Under Load
**Objective:** Verify registration can handle multiple concurrent users
**Test Data:** 10-50 concurrent registration attempts

**Steps:**
1. Simulate multiple users registering simultaneously
2. Monitor database performance
3. Check for any failures or timeouts

**Expected Result:**
- All registrations complete successfully
- Reasonable response times maintained
- No database deadlocks or conflicts

---

## Test Execution Checklist

### Pre-Execution Setup
- [ ] Database server is running and accessible
- [ ] Web server is running
- [ ] Test data is prepared
- [ ] Database tables are created with proper sequences
- [ ] Test environment matches production configuration

### Post-Execution Cleanup
- [ ] Remove test data from database
- [ ] Clear browser cache and cookies
- [ ] Reset database sequences if needed
- [ ] Document any defects found

---

## Automation Recommendations

### High Priority for Automation
1. **Smoke Tests**: Basic registration and login flows (CR-001, CL-001)
2. **Regression Tests**: Core functionality after code changes
3. **Data Validation**: Required field validations (CR-003, CL-004)

### Manual Testing Priority
1. **Security Tests**: SQL injection, authentication bypass
2. **Usability Tests**: User experience and error messaging
3. **Cross-browser Tests**: Browser compatibility verification

---

## Risk Analysis

### High Risk Areas
1. **SQL Injection Vulnerabilities**: Direct SQL queries in JSP
2. **Session Management**: Improper session handling
3. **Data Validation**: Client-side only validation
4. **Error Handling**: Database connection failures

### Mitigation Strategies
1. Implement parameterized queries
2. Add server-side validation
3. Implement proper error handling
4. Regular security testing

---

## Success Metrics

### Functional Requirements
- ✅ 100% of positive test cases pass
- ✅ All negative test cases fail gracefully
- ✅ Data integrity maintained in all scenarios

### Non-Functional Requirements
- ✅ Registration completes within 5 seconds
- ✅ Login completes within 2 seconds
- ✅ Application handles 50 concurrent users
- ✅ Zero data corruption incidents

---

*This test plan should be executed in a dedicated test environment with proper test data management and should be updated as the application evolves.*