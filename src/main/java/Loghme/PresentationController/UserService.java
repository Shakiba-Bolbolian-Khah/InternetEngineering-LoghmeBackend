package Loghme.PresentationController;

import Loghme.DataSource.UserDAO;
import Loghme.Exceptions.Error400;
import Loghme.Exceptions.Error403;
import Loghme.Exceptions.Error404;
import Loghme.Domain.Logic.CommandHandler;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@RestController
public class UserService {

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@RequestParam(value = "userId", required = true) int id) {
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().doGetUser(id), HttpStatus.OK);
        } catch (IOException| SQLException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Error404 error404) {
            return new ResponseEntity<>(error404.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(value = "/users/cart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCart(@RequestParam(value = "userId", required = true) int id) {
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().doGetCart(id), HttpStatus.OK);
        } catch (IOException|SQLException error){
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Error400 error400) {
            return new ResponseEntity<>(error400.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/users/finalize", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> finalizeCart(@RequestParam(value = "userId", required = true) int id) {
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().finalizeOrder(id), HttpStatus.OK);
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
    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> increaseCreadit(@RequestParam(value = "userId", required = true) int id,
                                             @RequestParam(value = "credit", required = true) int newCredit){
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().increaseCredit(newCredit, id), HttpStatus.OK);
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
                        CommandHandler.getInstance().addToCart(restaurantId, foodName, false, id);
                    }
                    return new ResponseEntity<>(CommandHandler.getInstance().addToCart(restaurantId, foodName, false, id), HttpStatus.OK);
                case "delete":
                    for(int i = 0; i < count-1; i++){
                        CommandHandler.getInstance().deleteFromCart(restaurantId, foodName, false, id);
                    }
                    return new ResponseEntity<>(CommandHandler.getInstance().deleteFromCart(restaurantId, foodName, false, id), HttpStatus.OK);
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
    @RequestMapping(value = "/authentication/signup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signup(@RequestParam(value = "firstName", required = true) String firstName,
                                    @RequestParam(value = "lastName", required = true) String lastName,
                                    @RequestParam(value = "phone", required = true) String phoneNumber,
                                    @RequestParam(value = "email", required = true) String email,
                                    @RequestParam(value = "password", required = true) String password){
        try {

            UserDAO newUser = new UserDAO(firstName, lastName, phoneNumber, email);
            int userId = CommandHandler.getInstance().signup(newUser, password);
            return new ResponseEntity<>(JWTmanager.getInstance().createJWT(userId, email), HttpStatus.OK);

        } catch (IOException|SQLException | JWTCreationException error) {
            return new ResponseEntity<>(new Gson().toJson(error.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Error403 error403) {
            return new ResponseEntity<>(new Gson().toJson(error403.getMessage()), HttpStatus.FORBIDDEN);
        }
    }
    @RequestMapping(value = "/authentication/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login( @RequestParam(value = "email", required = true) String email,
                                    @RequestParam(value = "password", required = true) String password){
        try {
            System.out.println(email);
            System.out.println(password);
            int userId = CommandHandler.getInstance().login(email, password);
            System.out.println(userId);
            return new ResponseEntity<>(JWTmanager.getInstance().createJWT(userId, email), HttpStatus.OK);

        } catch (IOException | JWTCreationException | SQLException error) {
            return new ResponseEntity<>(new Gson().toJson(error.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Error403 error403) {
            return new ResponseEntity<>(new Gson().toJson(error403.getMessage()), HttpStatus.FORBIDDEN);
        }
    }
    @RequestMapping(value = "/authentication/googleLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> googleLogin( @RequestParam(value = "email", required = true) String email){
        try {
            System.out.println(email);
            int userId = CommandHandler.getInstance().googleLogin(email);
            System.out.println(userId);
            return new ResponseEntity<>(JWTmanager.getInstance().createJWT(userId, email), HttpStatus.OK);

        } catch (IOException | JWTCreationException | SQLException error) {
            return new ResponseEntity<>(new Gson().toJson(error.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Error403 error403) {
            return new ResponseEntity<>(new Gson().toJson(error403.getMessage()), HttpStatus.FORBIDDEN);
        }
    }
}