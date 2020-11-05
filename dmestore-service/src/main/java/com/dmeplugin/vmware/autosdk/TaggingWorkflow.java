package com.dmeplugin.vmware.autosdk;

import com.vmware.cis.tagging.*;
import com.vmware.vapi.std.DynamicID;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaggingWorkflow {
    private Category categoryService;
    private Tag taggingService;
    private TagAssociation tagAssociation;
    private SessionHelper sessionHelper;

    public TaggingWorkflow(SessionHelper sessionHelper) {
        this.sessionHelper = sessionHelper;
        this.categoryService =
                this.sessionHelper.vapiAuthHelper.getStubFactory().createStub(Category.class,
                        sessionHelper.sessionStubConfig);
        this.taggingService =
                this.sessionHelper.vapiAuthHelper.getStubFactory().createStub(Tag.class,
                        sessionHelper.sessionStubConfig);
        this.tagAssociation =
                this.sessionHelper.vapiAuthHelper.getStubFactory()
                        .createStub(TagAssociation.class,
                                sessionHelper.sessionStubConfig);
    }

    /**
     * API to create a category. User who invokes this needs create category
     * privilege.
     *
     * @param name
     * @param description
     * @param cardinality
     * @return
     */
    public String createTagCategory(String name, String description,
                                     CategoryModel.Cardinality cardinality) {
        CategoryTypes.CreateSpec createSpec = new CategoryTypes.CreateSpec();
        createSpec.setName(name);
        createSpec.setDescription(description);
        createSpec.setCardinality(cardinality);

        Set<String> associableTypes = new HashSet<String>(); // empty hash set
        createSpec.setAssociableTypes(associableTypes);
        return this.categoryService.create(createSpec);
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
     * Deletes an existing tag category; User who invokes this API needs delete
     * privilege on the tag category.
     *
     * @param categoryId
     */
    public void deleteTagCategory(String categoryId) {
        this.categoryService.delete(categoryId);
    }

    /**
     * Creates a tag
     *
     * @param name Display name of the tag.
     * @param description Tag description.
     * @param categoryId ID of the parent category in which this tag will be
     *        created.
     * @return Id of the created tag
     */
    public String createTag(String name, String description,
                             String categoryId) {
        TagTypes.CreateSpec spec = new TagTypes.CreateSpec();
        spec.setName(name);
        spec.setDescription(description);
        spec.setCategoryId(categoryId);

        return this.taggingService.create(spec);
    }

    /**
     * Update the description of an existing tag. User who invokes this API
     * needs edit privilege on the tag.
     *
     * @param tagId the ID of the input tag
     * @param description
     */
    public void updateTag(String tagId, String description) {
        TagTypes.UpdateSpec updateSpec = new TagTypes.UpdateSpec();
        updateSpec.setDescription(description);
        this.taggingService.update(tagId, updateSpec);
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
    public void attachTag(String tagId,DynamicID objDynamicId){
        this.tagAssociation.attach(tagId,objDynamicId);
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
