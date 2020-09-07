package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.dao.DmeInfoDao;
import com.dmeplugin.dmestore.services.DmeAccessServiceImpl;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

public class DmeAccessControllerTest {
    private Gson gson = new Gson();

    @Test
    public void controller_accessTest() throws Exception {
        DmeAccessController controller = new DmeAccessController();
        Map<String, Object> params = new HashMap<>();
        params.put("hostIp", "192.168.3.203");
        params.put("hostPort", 26335);
        params.put("userName", "testadmin001");
        params.put("password", "Pbu421234");
        System.out.println(gson.toJson(params));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/accessdme/access").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(params)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void controller_refreshaccessTest() throws Exception {
        DmeAccessController controller = new DmeAccessController();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/accessdme/refreshaccess"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void service_accessDme_Test() throws Exception {
        DmeAccessServiceImpl dmeAccessService = new DmeAccessServiceImpl();
        DmeInfoDao dmeInfoDao = new DmeInfoDao();
        dmeAccessService.setDmeInfoDao(dmeInfoDao);
        Map<String, Object> params = new HashMap<>();
        params.put("hostIp", "192.168.3.203");
        params.put("hostPort", 26335);
        params.put("userName", "testadmin001");
        params.put("password", "Pbu421234");
        Map<String, Object> remap = dmeAccessService.accessDme(params);
        System.out.println("remap==" + remap);
    }

    @Test
    public void service_refreshDme_Test() throws Exception {
        DmeAccessServiceImpl dmeAccessService = new DmeAccessServiceImpl();
        DmeInfoDao dmeInfoDao = new DmeInfoDao();
        dmeAccessService.setDmeInfoDao(dmeInfoDao);
        Map<String, Object> remap = dmeAccessService.refreshDme();
        System.out.println("remap==" + remap);
    }
}