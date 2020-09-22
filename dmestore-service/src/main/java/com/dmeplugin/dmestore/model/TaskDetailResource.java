package com.dmeplugin.dmestore.model;

/**
 * @Description: TODO
 * @ClassName: TaskDetailResource
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-18
 **/
public class TaskDetailResource {
    String operate;
    String type;
    String id;
    String name;

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
