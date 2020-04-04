package Loghme.Service;

import Loghme.Exceptions.Error403;
import Loghme.Exceptions.Error404;
import Loghme.Model.CommandHandler;
import Loghme.Model.Restaurant;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class RestaurantService {

    @RequestMapping(value = "/restaurant/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant getRestaurant(@PathVariable(value = "id") String id) throws IOException, Error404, Error403 {
        return CommandHandler.getInstance().getRestaurant(id);
    }
}