import apiclient.ApiIngredientsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class IngredientsTests {

    public ApiIngredientsClient apiIngredientsClient;

    @BeforeEach
    public void setUp() {
        apiIngredientsClient = new ApiIngredientsClient();
    }

    @Test
    public void shouldGetIngredientsList() {
        apiIngredientsClient.getIngredients()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data", notNullValue());
    }

}