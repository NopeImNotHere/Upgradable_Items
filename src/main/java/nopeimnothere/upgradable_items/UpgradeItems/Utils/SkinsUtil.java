package nopeimnothere.upgradable_items.UpgradeItems.Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SkinsUtil {
    public static String getPlayerUUID(String Username) {
        String id = "404";
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + Username);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(conn.getInputStream());
            id = root.get("id").asText();
            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    };

    public static String getTexture(String UUID) {
        String value = "";
        try {
            String website = "https://sessionserver.mojang.com/session/minecraft/profile/" + UUID + "?unsigned=false";
            URL url = new URL(website);

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();
            String json = jsonBuilder.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);

            JsonNode propertiesNode = rootNode.get("properties");
            if (propertiesNode != null && propertiesNode.isArray()) {
                for (JsonNode property : propertiesNode) {
                    JsonNode valueNode = property.get("value");
                    if (valueNode != null) {
                        value = valueNode.asText();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getSignature(String UUID) {
        String signature = "";
        try {
            String website = "https://sessionserver.mojang.com/session/minecraft/profile/" + UUID + "?unsigned=false";
            URL url = new URL(website);

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();
            String json = jsonBuilder.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);

            JsonNode propertiesNode = rootNode.get("properties");
            if (propertiesNode != null && propertiesNode.isArray()) {
                for (JsonNode property : propertiesNode) {
                    JsonNode signatureNode = property.get("signature");
                    if (signatureNode != null) {
                        signature = signatureNode.asText();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signature;
    }
    

}
