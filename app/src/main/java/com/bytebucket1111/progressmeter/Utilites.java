package com.bytebucket1111.progressmeter;

public class Utilites {

    public static String getWeatherString(int code)
    {
        String weatherName = "";
        int x = code /100;
        switch(x){
            case 2 : weatherName = "Thunder Storm"; break;
            case 3 : weatherName = "Drizzle"; break;
            case 5 : weatherName = "Rain"; break;
            case 6 : weatherName = "Snow"; break;
            case 7 : weatherName = "Fog"; break;
            case 8 : weatherName = "Clear Sky"; break;
            default: weatherName = "Not Applied"; break;
        }
        return weatherName;
    }

}
