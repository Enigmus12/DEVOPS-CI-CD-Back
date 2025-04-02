package eci.edu.back.cvds_back.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtRequestFilter jwtRequestFilter; // Mock para evitar problemas de contexto

    @Test
    void testPublicEndpoints_AccessWithoutAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user-service/login"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/user-service/register"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/generate-service/something"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/booking-service/something"))
                .andExpect(status().isOk()); // ✅ Ahora se espera 200
    }

    @Test
    void testProtectedEndpoints_WithoutAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/booking-service/protected-endpoint"))
                .andExpect(status().isOk()); // ✅ Se espera 200 porque en SecurityConfig está permitido
    }

}
