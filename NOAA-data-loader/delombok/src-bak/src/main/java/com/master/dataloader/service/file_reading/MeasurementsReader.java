package com.master.dataloader.service.file_reading;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class MeasurementsReader {
    private Integer ID = 1; //temp
    private final String path = "data/ghcnd_gsn/ghcnd_gsn/";
    private Map<String,Element> elements;

    private final Integer[][] invalidDates = new Integer[][]{
            {2,30},
            {2,31},
            {4,31},
            {6,31},
            {9,31},
            {11,31},
    };

    List<Integer> leapYears = new ArrayList<>();
//todo check stream reading
    public List<Measurement> readMeasurements(String filename, Map<String,Element> elements, Station station){
        this.elements = elements;
        List<Measurement> result = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path + filename))){
            String line;
            while((line = reader.readLine()) != null){
//                System.out.println(line);
                List<Measurement> monthlyMeasurements = readMonth(line, station);
                result.addAll(monthlyMeasurements);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        return result;
    }

    private List<Measurement> readMonth(String line, Station station){
        List<Measurement> result = new ArrayList<>();
        String stationId = line.substring(3,11);
        Integer year = Integer.parseInt(line.substring(11,15));
        Integer month = Integer.parseInt(line.substring(15,17));
        Element element = elements.get(line.substring(17,21));
        List<String> daysData = extractDays(line);

        Integer dayOfMonth = 1;
        for (String dayData : daysData){
            LocalDate date = parseDate(year,month,dayOfMonth++);
            if(date != null){
                Measurement measurement = Measurement.builder()
                        .id(ID)
                        .station(station)
                        .date(date)
                        .element(element)
                        .value(Integer.parseInt(dayData.substring(0,5).trim()))
                        .mflag(dayData.charAt(5))
                        .qflag(dayData.charAt(6))
                        .sflag(dayData.charAt(7))
                        .build();
                result.add(measurement);
            }
        }
        ID++;
        return result;
    }

    //good to handle
    private LocalDate parseDate(Integer year, Integer month, Integer dayOfMonth){
        List<Integer> lessThan31DaysMonths = Arrays.stream(invalidDates).map(date -> date[0]).toList();
        if (lessThan31DaysMonths.contains(month)) {

            if(month == 2 && dayOfMonth == 29 && !leapYears.contains(year)){
                return null;
            }

            List<Integer> invalidDays =
                    Arrays.stream(invalidDates)
                            .filter(date -> month.equals(date[0]))
                            .map(date -> date[1])
                            .toList();

            if (invalidDays.contains(dayOfMonth)){
                return null;
            }
        }
        return LocalDate.of(year,month,dayOfMonth);
    }

    List<String> extractDays(String line){
        List<String> result = new ArrayList<>();

        for(int i = 21 ; i<=261 ; i+=8){
            result.add(line.substring(i,i+8));
        }


        return result;
    }

}
