package com.master.dataloader.service.file_reading;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ElementReader {

    public Map<String,Element> readElements(){
        Map<Integer,String> groundCodes = new HashMap<>();
        Map<Integer,String> depthCodes = new HashMap<>();
        Map<String,String> weatherTypes = new HashMap<>();
        Map<String,String> vicinityWeatherTypes = new HashMap<>();
        Map<String,Element> result = new HashMap<>();

        readGroundAndDepthCodes(groundCodes,depthCodes);
        readWeatherAndVicinityWeatherTypes(weatherTypes,vicinityWeatherTypes);

        try(BufferedReader reader = Files.newBufferedReader(Paths.get("data/elements.txt"))){
            String line;

            while ((line = reader.readLine()) != null) {
                String[] elementData = line.split("=");
                String elementKey = elementData[0].trim();
                String elementDescription = elementData[1].trim();

                if(elementKey.equals("WT**")){
                    handleWeatherType(elementKey,result,weatherTypes);
                }
                else if(elementKey.equals("WV**")){
                    handleVicinityWeatherType(elementKey,result,vicinityWeatherTypes);
                }
                else if(elementKey.equals("SN*#")){
                    handleSoilMin(elementKey,result,groundCodes,depthCodes);
                }
                else if(elementKey.equals("SX*#")){
                    handleSoilMax(elementKey,result,groundCodes,depthCodes);
                }
                else{
                    result.put(elementKey,new Element(elementKey, elementDescription));
                }

            }

        }catch (IOException e){
            throw new RuntimeException(e);
        }

        return result;
    }

    private void readGroundAndDepthCodes(Map<Integer,String> groundCodes, Map<Integer,String> depthCodes){
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data/ground_depth_codes.txt"))) {
            String line;
            boolean readingDepthCodes = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("GROUND_COVER_CODES")) {
                    readingDepthCodes = false;
                    continue;
                } else if (line.equals("DEPTH_CODES")) {
                    readingDepthCodes = true;
                    continue;
                }

                String[] parts = line.split(" = ");
                if (parts.length == 2) {
                    try {
                        int key = Integer.parseInt(parts[0].trim());
                        String value = parts[1].trim();

                        if (readingDepthCodes) {
                            depthCodes.put(key, value);
                        } else {
                            groundCodes.put(key, value);
                        }
                    } catch (NumberFormatException e) {
                        // handle
                    }
                }
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void readWeatherAndVicinityWeatherTypes(Map<String, String> weatherTypes, Map<String, String> vicinityWeatherTypes) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data/weather_types.txt"))) {
            String line;
            boolean readingVicinityWeatherTypes = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("WEATHER_TYPE")) {
                    readingVicinityWeatherTypes = false;
                    continue;
                } else if (line.equals("VICINITY_WEATHER_TYPE")) {
                    readingVicinityWeatherTypes = true;
                    continue;
                }

                String[] parts = line.split(" = ");
                if (parts.length == 2) {
                    try {
                        String key = parts[0].trim();
                        String value = parts[1].trim();

                        if (readingVicinityWeatherTypes) {
                            vicinityWeatherTypes.put(key, value);
                        } else {
                            weatherTypes.put(key, value);
                        }
                    } catch (NumberFormatException e) {
                        //handle
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleWeatherType(String elementKey, Map<String,Element> result, Map<String, String> weatherTypes) {
        if (!elementKey.startsWith("WT")) return;

        for (Map.Entry<String, String> entry : weatherTypes.entrySet()) {
            String newKey = elementKey.substring(0,2) + entry.getKey();
            String description = "Weather Type: " + entry.getValue();
            result.put(newKey,new Element(newKey, description));
        }
    }

    private void handleVicinityWeatherType(String elementKey, Map<String,Element> result, Map<String, String> vicinityWeatherTypes) {
        if (!elementKey.startsWith("WV")) return;

        for (Map.Entry<String, String> entry : vicinityWeatherTypes.entrySet()) {
            String newKey = elementKey.substring(0,2) + entry.getKey();
            String description = "Vicinity weather type: " + entry.getValue();
            result.put(newKey,new Element(newKey, description));
        }
    }

    private void handleSoilMin(String elementKey, Map<String,Element> result, Map<Integer, String> groundCodes, Map<Integer, String> depthCodes){
        if(!elementKey.startsWith("SN")) return;
        for (Map.Entry<Integer, String> groundEntry : groundCodes.entrySet()) {
            for (Map.Entry<Integer, String> depthEntry : depthCodes.entrySet()) {
                String newKey = elementKey.substring(0,2) + groundEntry.getKey() + depthEntry.getKey();
                String description = "Minimum soil temperature (tenths of degrees C) (ground cover: " + groundEntry.getValue() + ") (soil depth: " + depthEntry.getValue() + ")";
                result.put(newKey,new Element(newKey, description));
            }
        }
    }

    private void handleSoilMax(String elementKey, Map<String,Element> result, Map<Integer, String> groundCodes, Map<Integer, String> depthCodes){
        if(!elementKey.startsWith("SX")) return;
        for (Map.Entry<Integer, String> groundEntry : groundCodes.entrySet()) {
            for (Map.Entry<Integer, String> depthEntry : depthCodes.entrySet()) {
                String newKey = elementKey.substring(0,2) + groundEntry.getKey() + depthEntry.getKey();
                String description = "Maximum soil temperature (tenths of degrees C) (ground cover: " + groundEntry.getValue() + ") (soil depth: " + depthEntry.getValue() + ")";
                result.put(newKey,new Element(newKey, description));
            }
        }
    }
}
