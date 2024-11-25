package org.example;
//import com.sun.net.httpserver.Request;
import netscape.javascript.JSObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;


import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Main {

    private static final String API_KEY = "a7b76e3c606d48effb4e257c3ff96b01";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args){
        String encoding = System.getProperty("console.encoding", "UTF-8");
        System.out.println(encoding);
        Scanner scanner = new Scanner(System.in, encoding);
        try {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new InternalError("VM does not support mandatory encoding UTF-8");
        }
        System.out.println("Введите название города");
        String city = scanner.nextLine();
        try {
            String weatherData = getWeatherData(city);
            if (weatherData != null) {
                parseAndDisplayWeather(weatherData);
            }else{
                System.out.println("Не удалось получить данные о погоде");
            }

        } catch (Exception e) {
            System.out.println("Error");;
        }

    }
    public static void parseAndDisplayWeather(String jsonData){
        JSONObject jsonObject = new JSONObject(jsonData);
        String cityName = jsonObject.getString("name");
        JSONObject main = jsonObject.getJSONObject("main");
        double temperature = main.getDouble("temp");
        double feelsLike = main.getDouble("feels_like");
        int humidity = main.getInt("humidity");

        JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");

        System.out.println("\n Погода в городе " + cityName);
        System.out.println("Температура " + temperature);
        System.out.println("Ощущается как " + feelsLike);
        System.out.println("Влажность " + humidity);
        System.out.println("Описание " + description);
    }

    private static String getWeatherData(String city) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric&lang=en";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try(Response response = client.newCall(request).execute()){
            if (response.isSuccessful() && response.body() != null){
                return response.body().string();
            }else{
                System.out.println("Error" + response.code() + "" + response.message());
                return null;
            }
        }
    }
}