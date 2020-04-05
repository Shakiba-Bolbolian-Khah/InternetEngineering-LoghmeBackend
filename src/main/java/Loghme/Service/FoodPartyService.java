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
public class FoodPartyService {

    @RequestMapping(value = "/foodparty", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFoodParty() {
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().getFoodParty(), HttpStatus.OK);
        } catch (IOException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/foodparty", method = RequestMethod.POST)
    public ResponseEntity<?> buyPartyFood(
            @RequestParam(value = "userId", required = true) String userId,
            @RequestParam(value = "id", required = true) String restaurantId,
            @RequestParam(value = "name", required = true) String partyFoodName) {
        try {
            return new ResponseEntity<>(CommandHandler.getInstance().addPartyFoodToCart(restaurantId, partyFoodName), HttpStatus.OK);
        } catch (Error404 error404) {
            return new ResponseEntity<>(error404.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Error403 error403) {
            return new ResponseEntity<>(error403.getMessage(), HttpStatus.FORBIDDEN);
        } catch (IOException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}