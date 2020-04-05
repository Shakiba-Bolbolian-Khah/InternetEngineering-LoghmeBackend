package Loghme.Service;

import Loghme.Exceptions.Error403;
import Loghme.Exceptions.Error404;
import Loghme.Model.CommandHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class RestaurantService {

    @RequestMapping(value = "/restaurant/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRestaurant(@PathVariable(value = "id") String restaurantId) {
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().getRestaurant(restaurantId), HttpStatus.OK);
        } catch (Error404 error404) {
            return new ResponseEntity<>(error404.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Error403 error403) {
            return new ResponseEntity<>(error403.getMessage(), HttpStatus.FORBIDDEN);
        } catch (IOException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}