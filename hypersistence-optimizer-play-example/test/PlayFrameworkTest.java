import org.junit.Test;
import play.api.test.CSRFTokenHelper;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WSTestClient;
import play.test.WithServer;

import java.util.concurrent.CompletionStage;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static play.test.Helpers.*;

public class PlayFrameworkTest extends WithServer {

    @Test
    public void testSavePost() throws Exception {
        // Tests using a scoped WSClient to talk to the server through a port.
        try (WSClient ws = WSTestClient.newClient(this.testServer.getRunningHttpPort().getAsInt())) {
            CompletionStage<WSResponse> stage = ws.url("/").get();
            WSResponse response = stage.toCompletableFuture().get();
            String body = response.getBody();
            assertThat(body, containsString("Save Post"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
