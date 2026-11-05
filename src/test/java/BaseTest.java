import apiclient.ApiIngredientsClient;
import apiclient.ApiOrderClient;
import apiclient.ApiUserClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class BaseTest {

    protected ApiUserClient apiUserClient;
    protected String accessToken;
    protected ApiOrderClient apiOrderClient;
    protected ApiIngredientsClient apiIngredientsClient;

    @BeforeEach
    public void baseSetUp() {
        apiUserClient = new ApiUserClient();
        accessToken = null;
        apiOrderClient = new ApiOrderClient();
        apiIngredientsClient = new ApiIngredientsClient();
    }

    @AfterEach
    public void baseTearDown() {
        if (accessToken != null) {
            apiUserClient.deleteUser(accessToken)
                    .statusCode(202)
                    .body("success", equalTo(true));
        }
    }

    protected String getRandomString() {
        return UUID.randomUUID().toString();
    }

}
