package com.IamMusavaRibica.ScammerChecker;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.EnumChatFormatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class httputils {
    public static httputils instance;
    public static class ThreadedHttp implements Runnable {
        private String playerName;
        public ThreadedHttp(String playerName) {
            this.playerName = playerName;
        }

        public httpresult get(String url_) throws IOException {
            URL url = new URL(url_);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            StringBuilder response = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            return new httpresult(connection.getResponseCode(), response.toString());
        }
        @Override
        public void run() {
            try {
                // This is the whole function of requests and checks

                MainFile.sendMessage(EnumChatFormatting.AQUA + "Checking player " + this.playerName + "...");

                httpresult res = this.get("https://api.mojang.com/users/profiles/minecraft/" + this.playerName);
                int code = res.getResponseCode();
                String response = res.getResponse();

                if (code == 204) {
                    MainFile.sendErrorMessage("No content returned. Have you entered a correct username?");
                } else if (code == 403) {
                    MainFile.sendErrorMessage("403 Forbidden. Please wait before trying again.");
                } else if (code == 200) {
                    JsonObject response_json = new Gson().fromJson(response, JsonObject.class);
                    String uuid = response_json.get("id").getAsString();

                    // Now that we have the UUID , check for scams
                    String response2 = this.get("https://raw.githubusercontent.com/skyblockz/pricecheckbot/master/scammer.json").getResponse();

                    JsonObject scammers = new Gson().fromJson(response2, JsonObject.class);
                    JsonElement scammer_info = scammers.get(uuid);
                    if (scammer_info != null) {
                        System.out.println("This is the scammer info: " + scammer_info.getAsJsonObject());
                        MainFile.sendMessage(EnumChatFormatting.RED + "Player " + response_json.get("name").getAsString() + " is a known scammer. You might want to /ignore add him.");

                        String reason = scammer_info.getAsJsonObject().get("reason").getAsString();
                        MainFile.sendMessage(EnumChatFormatting.RED + "Reason: " + reason);
                    }
                    else {
                        MainFile.sendMessage(EnumChatFormatting.GREEN + "Player " + response_json.get("name").getAsString() + " is not a known scammer.");
                        MainFile.sendMessage(EnumChatFormatting.YELLOW + "However, you should still be careful when trading with this player.");
                    }
                }


            }
            catch(Exception e) {  // If an error occurs with the checking
                System.out.println("Catched exception while checking for scammer: " + e);
                MainFile.sendErrorMessage("An error occured while trying to check for scams done by this player.");
            }
        }
    }

    public httputils() {
        instance = this;
    }

    public void check(String playerName) {
        ThreadedHttp req = new ThreadedHttp(playerName);
        Thread thread = new Thread(req);
        thread.start();
    }
}
