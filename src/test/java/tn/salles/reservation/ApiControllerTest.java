package tn.salles.reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class ApiControllerTest {
 @Autowired MockMvc mvc;
 @Test void listeSallesRenvoie200() throws Exception { mvc.perform(get("/api/v1/salles")).andExpect(status().isOk()); }
 @Test void idInexistantRenvoie404() throws Exception { mvc.perform(get("/api/v1/reservations/9999999")).andExpect(status().isNotFound()); }
 @Test void jsonInvalideRenvoie400() throws Exception {
    mvc.perform(post("/api/v1/reservations").contentType("application/json").content("{}"))
    .andExpect(status().isBadRequest());
 }
}
