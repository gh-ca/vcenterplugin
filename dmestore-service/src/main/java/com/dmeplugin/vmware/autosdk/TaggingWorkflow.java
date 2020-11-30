package com.dmeplugin.vmware.autosdk;

import com.vmware.cis.tagging.*;
import com.vmware.vapi.std.DynamicID;

import java.util.List;

public class TaggingWorkflow {
    private Category categoryService;

    private Tag taggingService;

    private TagAssociation tagAssociation;

    private SessionHelper sessionHelper;

    public TaggingWorkflow(SessionHelper sessionHelper) {
        this.sessionHelper = sessionHelper;
        this.categoryService = this.sessionHelper.vapiAuthHelper.getStubFactory()
            .createStub(Category.class, sessionHelper.sessionStubConfig);
        this.taggingService = this.sessionHelper.vapiAuthHelper.getStubFactory()
            .createStub(Tag.class, sessionHelper.sessionStubConfig);
        this.tagAssociation = this.sessionHelper.vapiAuthHelper.getStubFactory()
            .createStub(TagAssociation.class, sessionHelper.sessionStubConfig);
    }

    public String createTagCategory(CategoryTypes.CreateSpec createSpec) {
        return this.categoryService.create(createSpec);
    }

    public List<String> listTagCategory() {
        return this.categoryService.list();
    }

    public CategoryModel getTagCategory(String categoryid) {
        return this.categoryService.get(categoryid);
    }

    /**
     * Creates a tag
     *
     * @param name        Display name of the tag.
     * @param description Tag description.
     * @param categoryId  ID of the parent category in which this tag will be created.
     * @return Id of the created tag
     */
    public String createTag(String name, String description, String categoryId) {
        TagTypes.CreateSpec spec = new TagTypes.CreateSpec();
        spec.setName(name);
        spec.setDescription(description);
        spec.setCategoryId(categoryId);

        return this.taggingService.create(spec);
    }

    /**
     * Delete an existing tag. User who invokes this API needs delete privilege
     * on the tag.
     *
     * @param tagId the ID of the input tag
     */
    public void deleteTag(String tagId) {
        this.taggingService.delete(tagId);
    }

    // tag the Object
    public void attachTag(String tagId, DynamicID objDynamicId) {
        this.tagAssociation.attach(tagId, objDynamicId);
    }

    //list tags
    public List<String> listTags() {
        return this.taggingService.list();
    }

    public List<String> listTagsForCategory(String categoryid) {
        return this.taggingService.listTagsForCategory(categoryid);
    }

    public TagModel getTag(String tagId) {
        return this.taggingService.get(tagId);
    }

}
