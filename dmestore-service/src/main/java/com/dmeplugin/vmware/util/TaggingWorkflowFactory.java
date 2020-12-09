package com.dmeplugin.vmware.util;

import com.dmeplugin.vmware.autosdk.SessionHelper;
import com.dmeplugin.vmware.autosdk.TaggingWorkflow;

/**
 * @author lianq
 * @className TaggingWorkflowFactory
 * @description TODO
 * @date 2020/11/30 10:54
 */
public class TaggingWorkflowFactory {

    private static TaggingWorkflowFactory taggingWorkflowFactory;

    private TaggingWorkflowFactory() {
    }

    public static TaggingWorkflowFactory getInstance() {
        if (taggingWorkflowFactory == null) {
            synchronized (TaggingWorkflowFactory.class) {
                if (taggingWorkflowFactory == null) {
                    taggingWorkflowFactory = new TaggingWorkflowFactory();
                }
            }
        }
        return taggingWorkflowFactory;
    }

    public TaggingWorkflow build(SessionHelper sessionHelper) throws Exception {
        return new TaggingWorkflow(sessionHelper);
    }
}
