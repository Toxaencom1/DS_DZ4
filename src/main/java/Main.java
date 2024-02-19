import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String TOKEN = "PUT TOKEN HERE";
    private static final String URL = "https://chirpstack-api.iotserv.ru/api/devices";
    private static final String applicationId = "6bb2cce7-abe9-4e98-980e-f5ada088d177";
    private static final String deviceProfileId = "29d26ba4-2bf0-452e-9341-2671f442c7da";
    private static final int LIMIT = 20;
    private static final int MILLIS = 400;

    public static void main(String[] args) {
        List<String> nameNumbers = new ArrayList<>(List.of(
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
        ));
        List<String> devUiList = new ArrayList<>(List.of(
                "FFAA824613004450",
                "FFAA824613004451",
                "FFAA824613004452",
                "FFAA824613004453",
                "FFAA824613004454",
                "FFAA824613004455",
                "FFAA824613004456",
                "FFAA824613004457",
                "FFAA824613004458",
                "FFAA824613004459"
        ));

        try {
            System.out.println("Create... ");
            for (int i = 0; i < 10; i++) {
                Thread.sleep(MILLIS);
                postDevices(nameNumbers.get(i), devUiList.get(i));
            }
            System.out.println("Get: ");
            System.out.println(getDevices());
            System.out.println("Update... ");
            for (int i = 0; i < 10; i++) {
                Thread.sleep(MILLIS);
                putDevices(nameNumbers.get(i), devUiList.get(i));
            }
            System.out.println("Get: ");
            System.out.println(getDevices());
            System.out.println("Delete... ");
            for (int i = 0; i < 10; i++) {
                Thread.sleep(MILLIS);
                deleteDevices(devUiList.get(i));
            }
            System.out.println("Get: ");
            System.out.println(getDevices());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getDevices() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(URL + "?limit=" + LIMIT + "&applicationId=" + applicationId)
                .method("GET", null)
                .addHeader("Authorization", TOKEN)
                .build();
        Response response = client.newCall(request).execute();
        return response+"\n"+response.body().string();
    }

    public static void postDevices(String nameNumber, String devEui) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"device\": {\r\n    \"applicationId\": \"" + applicationId + "\",\r\n    \"description\": \"Test Device from TAY-5471 " + nameNumber + "  \",\r\n    \"devEui\": \"" + devEui + "\",\r\n    \"deviceProfileId\": \"" + deviceProfileId + "\",\r\n    \"isDisabled\": false,\r\n    \"joinEui\": \"0000000000000000\",\r\n    \"name\": \"Test Device from TAY-5471 " + nameNumber + "\",\r\n    \"skipFcntCheck\": false\r\n  }\r\n}");
        Request request = new Request.Builder()
                .url(URL)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", TOKEN)
                .build();
        Response response = client.newCall(request).execute();
    }

    public static void putDevices(String nameNumber, String devEui) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"device\": {\r\n    \"applicationId\": \"" + applicationId + "\",\r\n    \"deviceProfileId\": \"" + deviceProfileId + "\",\r\n    \"name\": \"Test Device from TAY-5471 " + nameNumber + " Updated" + "\"\r\n  }\r\n}");
        Request request = new Request.Builder()
                .url(URL + "/" + devEui)
                .method("PUT", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", TOKEN)
                .build();
        Response response = client.newCall(request).execute();
    }

    public static void deleteDevices(String devEui) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(URL + "/" + devEui)
                .method("DELETE", body)
                .addHeader("Authorization", TOKEN)
                .build();
        Response response = client.newCall(request).execute();
    }
}
