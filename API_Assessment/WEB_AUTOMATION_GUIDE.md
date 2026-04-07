# How to Automate Web Login Testing

## Two Approaches for Your Project

### Option 1: REST API Testing (Your Current Approach) ✅ RECOMMENDED
Your current `RegisterUser.java` is **already optimal** for API testing. It:
- Calls API endpoints directly
- Is faster and more reliable
- Doesn't require a UI
- Perfect for backend/API testing

**No changes needed** - your API tests are ready to go!

---

### Option 2: Web UI Automation (If You Need Browser Testing)
Use the new files created:
- `BrowserAutomation.java` - Reusable browser utility
- `LoginUITest.java` - Example web UI login test

## Step-by-Step: Customize LoginUITest.java

### Step 1: Open Your Login Page in a Browser
Navigate to: `https://ndosiautomation.co.za/login`

### Step 2: Find the Element Selectors
Use **Chrome DevTools** (Press F12):
1. Right-click on the **Email input field** → Select "Inspect"
2. Look for the element's ID, class, or name
3. Note the selector

### Step 3: Update the Locators in LoginUITest.java

For example, if your page HTML looks like:
```html
<input type="email" id="emailAddress" placeholder="Email">
<input type="password" name="pwd" placeholder="Password">
<button class="btn btn-primary">Sign In</button>
```

Update your locators in `LoginUITest.java`:
```java
private static final By EMAIL_INPUT = By.id("emailAddress");
private static final By PASSWORD_INPUT = By.name("pwd");
private static final By LOGIN_BUTTON = By.className("btn-primary");
```

### Step 4: Find the Success Indicator
After login, what shows you're logged in?
- A "Dashboard" heading?
- A "Welcome" message?
- A redirect to a specific URL?

Update the SUCCESS_MESSAGE selector to match.

### Different Ways to Find Elements

```java
// By ID
By.id("emailInput")

// By Name
By.name("email")

// By Class
By.className("form-control")

// By CSS Selector
By.cssSelector("input[type='email']")

// By XPath
By.xpath("//input[@type='email']")

// By Link Text
By.linkText("Login")

// By Partial Link Text
By.partialLinkText("Log")
```

## Running Your Tests

### Run All API Tests
```
mvn test -Dtest=RegisterUser
```

### Run UI Login Tests
```
mvn test -Dtest=LoginUITest
```

### Run Everything
```
mvn test
```

## Important Notes

⚠️ **Before Running Tests:**
1. Update the `LOGIN_PAGE_URL` with your actual login page URL
2. Inspect your login form and update the element locators
3. Make sure you have the correct credentials
4. Ensure your login page is accessible

## Helpful Utilities in BrowserAutomation.java

```java
BrowserAutomation.initializeDriver();      // Start browser
BrowserAutomation.navigateTo(url);         // Go to URL
BrowserAutomation.typeIntoElement(locator, text);  // Type in field
BrowserAutomation.clickElement(locator);   // Click button
BrowserAutomation.getElementText(locator); // Get text
BrowserAutomation.waitForElement(locator); // Wait for element
BrowserAutomation.getCurrentUrl();         // Get current URL
BrowserAutomation.getPageTitle();          // Get page title
BrowserAutomation.closeBrowser();          // Close browser
```

## Debugging Tips

If tests fail:
1. **Check locators** - Use F12 to verify selectors are correct
2. **Add wait times** - Elements may take time to load
3. **Run in non-headless mode** - You'll see what the browser is doing:
   ```java
   // Already enabled by default in BrowserAutomation.java
   // Remove the // from the headless line to hide browser
   ```
4. **Check console output** - We added System.out.println() statements

## Combining Both Approaches

You can use both in your tests:

```java
// UI: Login via web interface
BrowserAutomation.navigateTo(LOGIN_PAGE_URL);
BrowserAutomation.typeIntoElement(EMAIL_INPUT, email);
BrowserAutomation.typeIntoElement(PASSWORD_INPUT, password);
BrowserAutomation.clickElement(LOGIN_BUTTON);

// Get token/info from page
String token = BrowserAutomation.getElementText(By.id("userToken"));

// API: Use token for API calls
RestAssured.given()
    .header("Authorization", "Bearer " + token)
    .baseUri("https://api.example.com")
    .get("/data")
    .prettyPeek();
```

## Questions?

Run: `mvn clean install` first to download all dependencies
Then: `mvn test` to run tests

Good luck! 🚀

