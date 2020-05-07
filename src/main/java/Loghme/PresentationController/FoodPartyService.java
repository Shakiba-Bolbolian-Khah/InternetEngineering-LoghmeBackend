package Loghme.PresentationController;

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
public class FoodPartyService {

    @RequestMapping(value = "/foodparty", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFoodParty() {
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().getFoodParty(), HttpStatus.OK);
        } catch (IOException|SQLException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/foodparty", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addPartyFoodToCart(
            @RequestParam(value = "userId", required = true) int userId,
            @RequestParam(value = "id", required = true) String restaurantId,
            @RequestParam(value = "name", required = true) String partyFoodName,
            @RequestParam(value = "action", required = true) String action,
            @RequestParam(value = "count", required = true) int count){
        try {
            switch (action) {
                case "add":
                    for(int i = 0; i < count-1; i++){
                        CommandHandler.getInstance().addToCart(restaurantId, partyFoodName, true, userId);
                    }
                    return new ResponseEntity<>(CommandHandler.getInstance().addToCart(restaurantId, partyFoodName, true, userId), HttpStatus.OK);
                case "delete":
                    for(int i = 0; i < count-1; i++){
                        CommandHandler.getInstance().deleteFromCart(restaurantId, partyFoodName, true, userId);
                    }
                    return new ResponseEntity<>(CommandHandler.getInstance().deleteFromCart(restaurantId, partyFoodName, true, userId), HttpStatus.OK);
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
}