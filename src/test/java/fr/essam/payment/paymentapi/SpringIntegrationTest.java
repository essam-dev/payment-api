package fr.essam.payment.paymentapi;

import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@Getter
public class SpringIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
}
