package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.dao.DmeInfoDao;
import com.dmeplugin.dmestore.model.VmfsDataInfo;
import com.dmeplugin.dmestore.services.DmeAccessServiceImpl;
import com.dmeplugin.dmestore.services.VmfsAccessServiceImpl;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VmfsAccessControllerTest {

    @Test
    public void listVmfsTest() throws Exception {
        VmfsAccessController controller = new VmfsAccessController();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/accessvmfs/listvmfs"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createvmfsTest() throws Exception {
        VmfsAccessController controller = new VmfsAccessController();
        Map<String, Object> params = new HashMap<>();
        params.put("hostIp", "10.143.132.232");
        params.put("hostPort", 26335);
        params.put("userName", "testadmin001");
        params.put("password", "Pbu421234");
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/accessvmfs/createvmfs").contentType(MediaType.APPLICATION_JSON).content(params.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void mountvmfsTest() throws Exception {
        VmfsAccessController controller = new VmfsAccessController();
        Map<String, Object> params = new HashMap<>();
        params.put("hostIp", "10.143.132.232");
        params.put("hostPort", 26335);
        params.put("userName", "testadmin001");
        params.put("password", "Pbu421234");
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/accessvmfs/mountvmfs").contentType(MediaType.APPLICATION_JSON).content(params.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void service_listVmfs_Test() throws Exception {
        VmfsAccessServiceImpl vmfsAccessService = new VmfsAccessServiceImpl();
        List<VmfsDataInfo> re = vmfsAccessService.listVmfs();
        Gson gson = new Gson();
        System.out.println("re==" + gson.toJson(re));
    }


}