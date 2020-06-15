package main.java.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * This controller handles the weather forcast for upcoming 5 days for the given latitude and longitude.
 *
 */
@RestController
@RequestMapping("weather")
public class WeatherForcastController {

    private final String GRID_URL = "https://api.weather.gov/points/";

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/{latitude}/{longitude}", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> weatherForcast(@PathVariable double latitude, @PathVariable double longitude) {

        try {
            String gridJSONData = restTemplate.getForObject(GRID_URL + latitude + "," + longitude, String.class);

            JSONObject gridMetaData = new JSONObject(gridJSONData);
            String foreCasturl = gridMetaData.getJSONObject("properties").getString("forecast");
            System.out.println(foreCasturl);

            String foreCastData = restTemplate.getForObject(foreCasturl, String.class);
            System.out.println("Forcast data = " + foreCastData);
            JSONObject forcastJSONData = new JSONObject(foreCastData);
            JSONArray fivedaysData = forcastJSONData.getJSONObject("properties").getJSONArray("periods");

            for (int i = 0; i < fivedaysData.length(); i++) {
                System.out.println("Detailed forcast" + fivedaysData.get(i));
            }
            return new ResponseEntity<String>("Succesfully printed the detailed value", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>("Error during retreiving the forcast data.", HttpStatus.BAD_REQUEST);
    }
}
