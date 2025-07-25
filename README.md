# SlipKART Registration and Login Test Suite

This repository contains comprehensive functional test cases for the SlipKART e-commerce application's registration and login functionality.

## 📋 Test Coverage

The test suite covers:

- **Customer Registration Flow** - Complete registration with address and payment information
- **Customer Login Flow** - Authentication and session management  
- **Staff Registration Flow** - Staff member registration with job details
- **Input Validation** - Required fields, data format validation
- **Security Testing** - SQL injection prevention, authentication bypass protection
- **Session Management** - Login persistence, logout functionality
- **Error Handling** - Invalid credentials, database failures
- **Cross-browser Compatibility** - Chrome, Firefox, Edge support

## 📁 Repository Structure

```
├── Registration_Login_Functional_Tests.md    # Manual test cases documentation
├── RegistrationLoginAutomationTests.java     # Selenium WebDriver automation
├── pom.xml                                   # Maven project configuration  
├── src/test/resources/testng.xml             # TestNG test suite configuration
└── README.md                                 # This file
```

## 🚀 Quick Start

### Prerequisites

1. **Java 11+** installed
2. **Maven 3.6+** installed
3. **Chrome browser** installed (ChromeDriver will be auto-managed)
4. **SlipKART application** running at `http://localhost:8080/Online/WebContent/`
5. **Database** accessible and properly configured

### Running Manual Tests

1. Open `Registration_Login_Functional_Tests.md`
2. Follow the detailed test cases step by step
3. Use the provided test data for consistent results
4. Mark test results in the acceptance criteria checkboxes

### Running Automated Tests

#### Setup
```bash
# Clone/download the test files
# Navigate to project directory
cd slipkart-tests

# Install dependencies
mvn clean compile
```

#### Execute Tests

**Run all tests:**
```bash
mvn clean test
```

**Run specific test suites:**
```bash
# Smoke tests only
mvn test -Dtest=RegistrationLoginAutomationTests -Dgroups=smoke

# Registration tests only  
mvn test -DsuiteXmlFile=src/test/resources/testng.xml -Dtest.name="Registration Tests"

# Security tests only
mvn test -DsuiteXmlFile=src/test/resources/testng.xml -Dtest.name="Security Tests"
```

**Run in different browsers:**
```bash
# Firefox
mvn test -Pfirefox

# Edge  
mvn test -Pedge

# Headless mode
mvn test -Pheadless
```

**Parallel execution:**
```bash
mvn test -Pparallel
```

#### Generate Reports
```bash
# Generate Allure report
mvn allure:report

# Open report in browser
mvn allure:serve
```

## 🧪 Test Cases Overview

### Customer Registration Tests (CR-001 to CR-006)
- ✅ **CR-001**: Complete registration with full information
- ✅ **CR-002**: Registration with permanent address only
- ✅ **CR-003**: Validation for missing required fields
- ✅ **CR-004**: Duplicate username handling
- ✅ **CR-005**: Invalid email format validation
- ✅ **CR-006**: Credit card information validation

### Customer Login Tests (CL-001 to CL-005)
- ✅ **CL-001**: Successful login with valid credentials
- ✅ **CL-002**: Login with invalid username
- ✅ **CL-003**: Login with invalid password
- ✅ **CL-004**: Empty credentials validation
- ✅ **CL-005**: SQL injection prevention

### Staff Registration Tests (SR-001 to SR-003)
- ✅ **SR-001**: Complete staff registration
- ✅ **SR-002**: Missing required fields validation
- ✅ **SR-003**: Invalid salary validation

### Session Management Tests (SM-001 to SM-002)
- ✅ **SM-001**: Session persistence across pages
- ✅ **SM-002**: Proper session termination on logout

### Database Integration Tests (DB-001 to DB-002)
- ✅ **DB-001**: Database connection failure handling
- ✅ **DB-002**: Data integrity verification

## 🔧 Configuration

### Application Settings
Update these values in the test files if your setup differs:

```java
// RegistrationLoginAutomationTests.java
private static final String BASE_URL = "http://localhost:8080/Online/WebContent/";

// Database connection (if testing database integration)
String dbUrl = "jdbc:oracle:thin:@fourier.cs.iit.edu:1521:orcl";
String username = "kparchand";
String password = "Cognizant13";
```

### TestNG Configuration
Modify `src/test/resources/testng.xml` to:
- Change base URL
- Adjust timeout values  
- Select specific test suites
- Configure parallel execution

### Maven Profiles
Available profiles in `pom.xml`:
- `chrome` (default) - Run tests in Chrome
- `firefox` - Run tests in Firefox  
- `edge` - Run tests in Edge
- `headless` - Run tests without GUI
- `parallel` - Run tests in parallel

## 📊 Test Data Management

### Automated Test Data
- Tests use randomly generated usernames/emails to avoid conflicts
- UUID-based unique identifiers ensure test independence
- Database cleanup handled automatically

### Manual Test Data
Use these consistent test data sets:

**Customer Registration:**
```
First Name: John
Last Name: Doe  
Username: johndoe123
Password: password123
Email: john.doe@email.com
Credit Card: 1234567890123456
CVV: 123
```

**Staff Registration:**
```
First Name: Jane
Last Name: Smith
Username: janesmith
Password: staffpass123
Job Title: Store Manager
Salary: 50000
```

## 🐛 Troubleshooting

### Common Issues

**ChromeDriver Issues:**
```bash
# WebDriverManager handles driver automatically, but if issues persist:
mvn dependency:resolve
```

**Database Connection Issues:**
- Verify database is running and accessible
- Check connection string in JSP files
- Ensure Oracle JDBC driver is available

**Application Not Running:**
- Start your web server (Tomcat/etc.)
- Verify application is deployed correctly
- Check logs for startup errors

**Test Failures:**
- Check application logs for errors
- Verify database has proper test data
- Ensure proper cleanup between test runs

### Debug Mode
```bash
# Run tests with debug logging
mvn test -Dlogback.configurationFile=src/test/resources/logback-test.xml -X
```

## 📈 CI/CD Integration

### Jenkins Pipeline
```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
            post {
                always {
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/allure-report',
                        reportFiles: 'index.html',
                        reportName: 'Allure Report'
                    ])
                }
            }
        }
    }
}
```

### GitHub Actions
```yaml
name: Test Suite
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 11
      uses: actions/setup-java@v2
      with:
        java-version: '11'
        distribution: 'temurin'
    - name: Run tests
      run: mvn clean test -Pheadless
```

## 🎯 Best Practices

### Test Execution
1. **Environment Isolation** - Use dedicated test environment
2. **Data Cleanup** - Clean test data between runs
3. **Independent Tests** - Each test should be runnable in isolation
4. **Stable Locators** - Use reliable element identification strategies

### Test Maintenance  
1. **Page Object Model** - Consider implementing for larger test suites
2. **Test Data Externalization** - Move test data to external files
3. **Reporting** - Use Allure for comprehensive test reporting
4. **Version Control** - Track test changes alongside application changes

## 📞 Support

For issues or questions:
1. Check this README for common solutions
2. Review test execution logs
3. Verify application and database status
4. Check browser console for JavaScript errors

## 📝 Contributing

When adding new test cases:
1. Follow existing naming conventions (CR-XXX, CL-XXX, etc.)
2. Include both manual and automated versions
3. Add appropriate test data and cleanup
4. Update documentation and test counts
5. Ensure tests are independent and repeatable

---

**Test Suite Version:** 1.0.0  
**Last Updated:** December 2024  
**Compatible With:** SlipKART Application (JSP-based)