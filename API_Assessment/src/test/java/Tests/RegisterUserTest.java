package Tests;

import Base.BaseTest.BaseURIs;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import Utilities.Builders.PayloadBuilder;

public class RegisterUserTest {

    static String authToken;
    static String userId;
    static String registeredEmail;
    static String baseURL = "https://ndosiautomation.co.za";

    @Test(priority = 1)
    public void registerUser() {

        String apiPath = "/APIDEV/register";
        registeredEmail = Faker.instance().internet().emailAddress();

        String payload = "{\n" +
                "    \"firstName\": \"Zuzu\",\n" +
                "    \"lastName\": \"Kino\",\n" +
                "    \"email\": \"Kino18000@gmail.com\",\n" +
                "    \"password\": \"12345678!\",\n" +
                "    \"confirmPassword\": \"12345678!\",\n" +
                "    \"phone\": \"\",\n" +
                "    \"groupId\": \"1deae17a-c67a-4bb0-bdeb-df0fc9e2e526\"\n" +
                "}";

        Response response = RestAssured.given()
                .baseUri(BaseURIs.BASE_URL)
                .basePath(apiPath)
                .header("Content-Type", "application/json")
                .body(payload)
                .log().all()
                .post()
                .prettyPeek();

        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, 201, "Status code should be 201");

        userId = response.jsonPath().getString("data.id");
        String approvalStatus = response.jsonPath().getString("data.approvalStatus");

        Assert.assertNotNull(userId, "User ID should not be null");
        Assert.assertEquals(approvalStatus, "pending", "Approval status should be pending");

        System.out.println("Registered User ID: " + userId);
        System.out.println("Registered Email: " + registeredEmail);
    }

    @Test(priority = 2)
    public void adminLoginTest() {

        String apiPath = "/APIDEV/login";
        String payload = "{\n" +
                "    \"email\": \"admin@gmail.com\",\n" +
                "    \"password\": \"@12345678\"\n" +
                "}";

        Response response = RestAssured.given()
                .baseUri(baseURL)
                .basePath(apiPath)
                .header("Content-Type", "application/json")
                .body(payload)
                .log().all()
                .post()
                .prettyPeek();

        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, 200, "Status code should be 200");

        authToken = response.jsonPath().getString("data.token");
        Assert.assertNotNull(authToken, "Auth token should not be null");
        System.out.println("Admin Token: " + authToken);
    }

    @Test(priority = 3, dependsOnMethods = {"registerUser", "adminLoginTest"})
    public void approveUserRegistration() {

        String apiPath = "/APIDEV/admin/users/" + userId + "/approve";

        Response response = RestAssured.given()
                .baseUri(baseURL)
                .basePath(apiPath)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .log().all()
                .put()
                .prettyPeek();

        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, 200, "Status code should be 200");
    }

    @Test(priority = 4, dependsOnMethods = "approveUserRegistration")
    public void makeUserAdmin() {

        Assert.assertNotNull(userId, "User ID should not be null before making user admin");
        Assert.assertNotNull(authToken, "Auth token should not be null before making user admin");

        String apiPath = "/APIDEV/admin/users/" + userId + "/role";

        Response response = RestAssured.given()
                .baseUri(baseURL)
                .basePath(apiPath)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .body(PayloadBuilder.updateUserRolePayload("admin"))
                .log().all()
                .put()
                .prettyPeek();

        int actualStatusCode = response.getStatusCode();
        System.out.println("Status code: " + actualStatusCode);
        System.out.println("Response body: " + response.asPrettyString());

        Assert.assertEquals(actualStatusCode, 200, "Status code should be 200");
    }

    @Test(priority = 5, dependsOnMethods = "makeUserAdmin")
    public void loginWithNewUser() {

        String apiPath = "/APIDEV/login";
        String payload = "{\n" +
                "    \"email\": \"Kino11@gmail.com\",\n" +
                "    \"password\": \"12345678!\"\n" +
                "}";

        Response response = RestAssured.given()
                .baseUri(baseURL)
                .basePath(apiPath)
                .header("Content-Type", "application/json")
                .body(payload)
                .log().all()
                .post()
                .prettyPeek();

        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, 200, "Status code should be 200");

        String role = response.jsonPath().getString("data.user.role");
        Assert.assertEquals(role, "admin", "User role should be admin");

        System.out.println("Logged in as new user: " + registeredEmail);
    }
    @Test(priority = 6, dependsOnMethods = "loginWithNewUser")
    public void deleteUser() {

        Assert.assertNotNull(userId, "User ID should not be null before deleting user");
        Assert.assertNotNull(authToken, "Auth token should not be null before deleting user");

        String apiPath = "/APIDEV/admin/users/" + userId;

        Response response = RestAssured.given()
                .baseUri(BaseURIs.BASE_URL)
                .basePath(apiPath)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .log().all()
                .delete()
                .prettyPeek();

        int actualStatusCode = response.getStatusCode();

        System.out.println("Delete response: " + response.asPrettyString());

        Assert.assertEquals(actualStatusCode, 200, "Status code should be 200");

        System.out.println("User deleted successfully: " + userId);
    }
}
