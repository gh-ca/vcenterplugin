package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.dao.DmeInfoDao;
import com.dmeplugin.dmestore.services.DmeAccessService;
import com.dmeplugin.dmestore.services.DmeAccessServiceImpl;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DmeAccessControllerTest {

    @Test
    public void refreshaccessTest() throws Exception {
        DmeAccessController controller = new DmeAccessController();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/accessdme/refreshaccess"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void accessTest() throws Exception {
        DmeAccessController controller = new DmeAccessController();
        Map<String, Object> params = new HashMap<>();
        params.put("hostIp", "10.143.132.232");
        params.put("hostPort", 26335);
        params.put("userName", "testadmin001");
        params.put("password", "Pbu421234");
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/accessdme/access").contentType(MediaType.APPLICATION_JSON).content(params.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void dmeAccessServiceTest() throws Exception {
        DmeAccessServiceImpl dmeAccessService = new DmeAccessServiceImpl();
        DmeInfoDao dmeInfoDao = new DmeInfoDao();
        dmeAccessService.setDmeInfoDao(dmeInfoDao);
        Map<String, Object> params = new HashMap<>();
        params.put("hostIp", "10.143.132.232");
        params.put("hostPort", 26335);
        params.put("userName", "testadmin001");
        params.put("password", "Pbu421234");
        Map<String, Object> remap = dmeAccessService.accessDme(params);
        System.out.println("remap=="+remap);
    }
}