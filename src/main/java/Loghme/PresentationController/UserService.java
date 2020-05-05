package Loghme.PresentationController;

import Loghme.DataSource.UserDAO;
import Loghme.Exceptions.Error400;
import Loghme.Exceptions.Error403;
import Loghme.Exceptions.Error404;
import Loghme.Domain.Logic.CommandHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@RestController
public class UserService {

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable(value = "userId") int id) {
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().doGetUser(), HttpStatus.OK);
        } catch (IOException| SQLException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Error404 error404) {
            return new ResponseEntity<>(error404.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(value = "/users/cart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCart(@RequestParam(value = "userId", required = true) int id) {
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().doGetCart(), HttpStatus.OK);
        } catch (IOException|SQLException error){
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Error400 error400) {
            return new ResponseEntity<>(error400.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/users/finalize", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> finalizeCart(@RequestParam(value = "userId", required = true) int id) {
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().finalizeOrder(), HttpStatus.OK);
        } catch (IOException|SQLException error){
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Error400 error400) {
            return new ResponseEntity<>(error400.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Error403 error403) {
            return new ResponseEntity<>(error403.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Error404 error404) {
            return new ResponseEntity<>(error404.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> increaseCreadit(@PathVariable(value = "userId") int id,
                                             @RequestParam(value = "credit", required = true) int newCredit){
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().increaseCredit(newCredit), HttpStatus.OK);
        } catch (IOException|SQLException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    @RequestMapping(value = "/users/cart", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addToCart(
            @RequestParam(value = "userId", required = true) int id,
            @RequestParam(value = "id", required = true) String restaurantId,
            @RequestParam(value = "name", required = true) String foodName,
            @RequestParam(value = "action", required = true) String action,
            @RequestParam(value = "count", required = true) int count){
        try {
            switch (action) {
                case "add":
                    for(int i = 0; i < count-1; i++){
                        CommandHandler.getInstance().addToCart(restaurantId, foodName, false);
                    }
                    return new ResponseEntity<>(CommandHandler.getInstance().addToCart(restaurantId, foodName, false), HttpStatus.OK);
                case "delete":
                    for(int i = 0; i < count-1; i++){
                        CommandHandler.getInstance().deleteFromCart(restaurantId, foodName, false);
                    }
                    return new ResponseEntity<>(CommandHandler.getInstance().deleteFromCart(restaurantId, foodName, false), HttpStatus.OK);
                default:
                    throw new Error400("Error: You can just add or delete a food.");
            }
        } catch (Error404 error404) {
            return new ResponseEntity<>(error404.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Error403 error403) {
            return new ResponseEntity<>(error403.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Error400 error400) {
            return new ResponseEntity<>(error400.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException|SQLException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    @RequestMapping(value = "/users/signup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signup(@RequestParam(value = "firstName", required = true) String firstName,
                                    @RequestParam(value = "lastName", required = true) String lastName,
                                    @RequestParam(value = "phone", required = true) String phoneNumber,
                                    @RequestParam(value = "email", required = true) String email,
                                    @RequestParam(value = "password", required = true) String password){
        try {

            UserDAO newUser = new UserDAO(firstName, lastName, phoneNumber, email);
            int userId = CommandHandler.getInstance().signup(newUser, password);
            return new ResponseEntity<>(JWTmanager.getInstance().createJWT(userId, email), HttpStatus.OK);

        } catch (IOException|SQLException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Error403 error403) {
            return new ResponseEntity<>(error403.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
    @RequestMapping(value = "/users/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login( @RequestParam(value = "email", required = true) String email,
                                    @RequestParam(value = "password", required = true) String password){
        try {

            int userId = CommandHandler.getInstance().login(email, password);
            return new ResponseEntity<>(JWTmanager.getInstance().createJWT(userId, email), HttpStatus.OK);

        } catch (IOException|SQLException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Error403 error403) {
            return new ResponseEntity<>(error403.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}