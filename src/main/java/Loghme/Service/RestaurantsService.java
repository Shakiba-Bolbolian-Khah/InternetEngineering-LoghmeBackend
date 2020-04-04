package Loghme.Service;

import Loghme.Exceptions.Error404;
import Loghme.Model.CommandHandler;
import Loghme.Model.Restaurant;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class RestaurantsService {

    @RequestMapping(value = "/restaurants", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<Restaurant> getRestaurants() throws IOException, Error404 {
        return CommandHandler.getInstance().getRestaurants();
    }
}