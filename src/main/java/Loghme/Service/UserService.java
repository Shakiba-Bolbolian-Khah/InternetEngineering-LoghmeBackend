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
public class UserService {

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable(value = "userId") String id) {
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().getUser().getEmail(), HttpStatus.OK);
        } catch (IOException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/users/cart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCart(@RequestParam(value = "userId", required = true) String id) {
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().getCart(), HttpStatus.OK);
        } catch (Error404 error404) {
            return new ResponseEntity<>(error404.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException error){
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> increaseCreadit(@PathVariable(value = "userId") String id,
                                             @RequestParam(value = "credit", required = true) int newCredit){
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().increaseCredit(newCredit), HttpStatus.OK);
        } catch (IOException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/users/cart", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addToCart(@RequestParam(value = "userId", required = true) String id,
                                       @RequestParam(value = "id", required = true) String restaurantId,
                                       @RequestParam(value = "name", required = true) String foodName){
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().addToCart(restaurantId, foodName), HttpStatus.OK);
        } catch (Error404 error404) {
            return new ResponseEntity<>(error404.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Error403 error403) {
            return new ResponseEntity<>(error403.getMessage(), HttpStatus.FORBIDDEN);
        } catch (IOException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/users/cart", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFromCart(@RequestParam(value = "userId", required = true) String id,
                                       @RequestParam(value = "id", required = true) String restaurantId,
                                       @RequestParam(value = "name", required = true) String foodName){
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().deleteFromCart(restaurantId, foodName), HttpStatus.OK);
        } catch (Error404 error404) {
            return new ResponseEntity<>(error404.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Error403 error403) {
            return new ResponseEntity<>(error403.getMessage(), HttpStatus.FORBIDDEN);
        } catch (IOException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}