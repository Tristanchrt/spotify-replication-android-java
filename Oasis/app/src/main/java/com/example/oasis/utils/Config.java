package com.example.oasis.utils;

import com.example.oasis.models.WebParameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
    public static String BaseUrl = "http://ws.audioscrobbler.com/2.0/?method=";
    public static List<WebParameters> webParameters = Arrays.asList(
            new WebParameters("api_key", "e642ea6f6cc871a4ce21462ac207b683"),
            new WebParameters("format", "json"));

}
