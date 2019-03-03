package com.bytebucket1111.progressmeter;

import android.os.AsyncTask;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NotificationTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... strings) {
        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic NjUxNjQ1MTctYzBhNS00NDFhLWE5YjItZDNhYzljODBjMWI5");
            con.setRequestMethod("POST");
            String strJsonBody = "{"
                    +   "\"app_id\": \"c5418327-5bbf-4776-9671-dcd514247c69\","
                    +   "\"included_segments\": [\"All\"],"
                    +	"\"filters\": [{\"field\": \"tag\", \"key\": \"key1\", \"relation\": \"=\", \"value\": \"" + strings[2] +"\"}],"
                    +   "\"data\": {\"foo\": \"bar\"},"
                    +   "\"headings\": {\"en\": \"" + strings[0] +"\"},"
                    +   "\"contents\": {\"en\": \"" + strings[1] +"\"},"
                    +   "\"small_icon\":  \"ic_stat_onesignal_logo\""
                    + "}";

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
}

