package Loghme.Service;

import Loghme.Exceptions.Error404;
import Loghme.Model.CommandHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class RestaurantsService {

    @RequestMapping(value = "/restaurants", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRestaurants() {
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().getRestaurants(), HttpStatus.OK);
        } catch (Error404 error404) {
            return new ResponseEntity<>(error404.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}