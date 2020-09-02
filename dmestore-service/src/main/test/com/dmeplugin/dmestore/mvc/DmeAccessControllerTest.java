package com.dmeplugin.dmestore.mvc;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;

public class DmeAccessControllerTest {

    @Test
    public void courseListTest() throws Exception {
        DmeAccessController controller = new DmeAccessController();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/accessdme/refreshaccess"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}