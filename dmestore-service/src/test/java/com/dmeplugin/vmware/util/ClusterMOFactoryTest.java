package com.dmeplugin.vmware.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.vmware.vim25.ManagedObjectReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className ClusterMOFactoryTest
 * @description TODO
 * @date 2020/12/1 20:31
 */
public class ClusterMOFactoryTest {

    @InjectMocks
    ClusterMOFactory factory;

    VmwareContext vmwareContext;
    ManagedObjectReference managedObjectReference;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vmwareContext = mock(VmwareContext.class);
        managedObjectReference = spy(ManagedObjectReference.class);
    }

    @Test
    public void getInstance() {
        ClusterMOFactory.getInstance();
    }

    @Test
    public void build() throws Exception {
        factory.build(vmwareContext, managedObjectReference);
    }
}