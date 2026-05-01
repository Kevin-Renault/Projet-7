package com.openclassroom.devops.orion.microcrm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class RepositoryOperationLoggingHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldLogPersonCreateUpdateAndDeleteThroughRestEvents(CapturedOutput output) throws Exception {
        String email = "repo-handler-" + UUID.randomUUID() + "@example.net";

        String location = mockMvc.perform(post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "John",
                          "lastName": "Doe",
                          "email": "%s",
                          "phone": "+1 (555) 123-4567",
                          "bio": "Developer"
                        }
                        """.formatted(email)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assertThat(location).isNotBlank();

        String personPath = location.replace("http://localhost", "");

        mockMvc.perform(patch(personPath)
                .contentType("application/merge-patch+json")
                .content("""
                        {
                          "firstName": "Johnny"
                        }
                        """))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete(personPath))
                .andExpect(status().isNoContent());

        String logs = output.getOut() + output.getErr();

        assertThat(logs).contains("RepositoryOperationLoggingHandler#beforeCreate create requested")
                .contains("email='" + email + "'")
                .contains("RepositoryOperationLoggingHandler#afterCreate created person id=")
                .contains("RepositoryOperationLoggingHandler#beforeSave update requested")
                .contains("RepositoryOperationLoggingHandler#afterSave updated person id=")
                .contains("RepositoryOperationLoggingHandler#beforeDelete delete requested")
                .contains("RepositoryOperationLoggingHandler#afterDelete deleted person id=");
    }
}