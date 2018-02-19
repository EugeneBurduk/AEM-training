package com.epam.aem.training.models;



import com.day.cq.wcm.api.Page;
import com.epam.aem.training.beans.CustomPage;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Model(adaptables = Resource.class)
public class TrainingModel {
    private static Logger LOGGER = LoggerFactory.getLogger(TrainingModel.class);

    @Inject
    @Default(values = "Headline")
    private String headline;

    @Inject
    @Default(values = "")
    private String behavior;

    @Inject
    @Default(values = "")
    private String dynamicPath;

    @Inject @Default(values = "")
    private String[] staticPath;

    private List<CustomPage> pages;
    private CustomPage rootPage;

    @Inject
    ResourceResolver resourceResolver;

    @PostConstruct
    protected void init() throws RepositoryException{
            if (behavior.equals("static")) {
                List<CustomPage> listPages = new ArrayList<>();
                for (String pathPage : staticPath) {
                    Resource resource = resourceResolver.getResource(pathPage);
                    CustomPage page = staticGetPages(resource);
                    listPages.add(page);
                }
                pages = listPages;
            }
            if(behavior.equals("dynamic")) {
                Resource resource = resourceResolver.getResource(dynamicPath);
                rootPage = staticGetPages(resource);
                pages = dynamicGetPages(resource);
            }
    }

    private CustomPage staticGetPages(Resource resource) {
       CustomPage page = null;
        try {
            Node node = resource.getChild("jcr:content").adaptTo(Node.class);
            String title = node.getProperty("jcr:title").getString();
            String description = node.getProperty("jcr:description").getString();
            String image = node.getNode("image/file").getPath();
            page = new CustomPage(title, description, image,resource.getPath());
        }
        catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
        }
        return page;
    }

    private List<CustomPage> dynamicGetPages(Resource resource) {
        List<CustomPage> listPages = new ArrayList<>();
        List<Resource> children = new ArrayList();
        getAllChildren(children, resource);
        for (Resource child : children) {
            if (child.isResourceType("cq:Page")) {
                Page page = child.adaptTo(Page.class);
                Resource content = child.getChild("jcr:content");
                String image = content.getChild("image/file").getPath();
                listPages.add(new CustomPage(page.getTitle(), page.getDescription(), image, child.getPath()));
            }
        }
        return listPages;
    }

    private void getAllChildren(List<Resource> listRes, Resource resource) {
        if (resource.hasChildren()) {
            Iterable<Resource> children = resource.getChildren();
            for (Resource child : children) {
                if (child.isResourceType("cq:Page")) {
                    listRes.add(child);
                    getAllChildren(listRes, child);
                }
            }
        }
    }

    public String[] getStaticPath() {
        return staticPath;
    }
    public void setHeadline(String headline) {
        this.headline = headline;
    }
    public String getHeadline() {
        return headline;
    }

    public String getBehavior() {
        return behavior;
    }

    public String getDynamicPath() {
        return dynamicPath;
    }
    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public List<CustomPage> getPages() {
        return pages;
    }
    public void setPages(List<CustomPage> pages) {
        this.pages = pages;
    }

    public CustomPage getRootPage() {
        return rootPage;
    }

    public void setRootPage(CustomPage rootPage) {
        this.rootPage = rootPage;
    }
}
