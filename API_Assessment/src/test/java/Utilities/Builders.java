package Utilities;

import java.util.HashMap;
import java.util.Map;

public class Builders {

    public static class PayloadBuilder {

        public static Map<String, Object> loginUserPayload(String email, String password) {
            Map<String, Object> loginUser = new HashMap<>();
            loginUser.put("email", email);
            loginUser.put("password", password);
            return loginUser;
        }

        public static Map<String, Object> registerUserPayload(String firstName, String lastName, String email, String password, String groupId) {
            Map<String, Object> registerUser = new HashMap<>();
            registerUser.put("firstName", firstName);
            registerUser.put("lastName", lastName);
            registerUser.put("email", email);
            registerUser.put("password", password);
            registerUser.put("confirmPassword", password);
            registerUser.put("groupId", groupId);
            return registerUser;
        }
        public static Map<String, Object> updateUserRolePayload(String role) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("role", role);
            return payload;
        }
    }


}
