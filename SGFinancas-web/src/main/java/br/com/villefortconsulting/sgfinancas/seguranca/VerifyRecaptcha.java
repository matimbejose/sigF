package br.com.villefortconsulting.sgfinancas.seguranca;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VerifyRecaptcha {

    public static final String URL = "https://www.google.com/recaptcha/api/siteverify";

    public static final String SECRET = "6Lc8DQ4TAAAAANvOvL4JKce5iBveSaq1skq2qAY6";

    public static boolean verify(String gRecaptchaResponse) throws IOException {
        if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
            return false;
        }

        try {
            URL obj = new URL(URL);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            // add reuqest header
            con.setRequestMethod("POST");

            String postParams = "secret=" + SECRET + "&response=" + gRecaptchaResponse;

            // Send post request
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(postParams);
                wr.flush();
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            //parse JSON response and return 'success' value
            try (JsonReader jsonReader = Json.createReader(new StringReader(response.toString()))) {
                JsonObject jsonObject = jsonReader.readObject();
                return jsonObject.getBoolean("success");
            }
        } catch (IOException e) {
            return false;
        }
    }

}
