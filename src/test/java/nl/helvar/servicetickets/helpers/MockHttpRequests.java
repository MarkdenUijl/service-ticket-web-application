package nl.helvar.servicetickets.helpers;

import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

public class MockHttpRequests {

    public static ResultActions performPostRequest(MockMvc mockMvc, String url, String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(MockMvcResultHandlers.print());
    }

    public static ResultActions performPostRequest(MockMvc mockMvc, String url, String content, UserDetails userDetails) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(MockMvcResultHandlers.print());
    }

    public static ResultActions performGetRequest(MockMvc mockMvc, String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    public static ResultActions performGetRequest(MockMvc mockMvc, String url, UserDetails userDetails) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    public static ResultActions performPutRequest(MockMvc mockMvc, String url, long id, String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(url + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(MockMvcResultHandlers.print());
    }
    public static ResultActions performPutRequest(MockMvc mockMvc, String url, long id, String content, UserDetails userDetails) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(url + "/{id}", id)
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(MockMvcResultHandlers.print());
    }

    public static ResultActions performDeleteRequest(MockMvc mockMvc, String url, long id) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }
}
