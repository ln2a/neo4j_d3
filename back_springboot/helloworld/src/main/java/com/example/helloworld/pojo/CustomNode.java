package com.example.helloworld.pojo;

public class CustomNode {
//    自定义类，匹配力导向图的属性值
    private String id;
    private int group;
    private Object obj;

    public CustomNode(String id, int group, Object obj) {
        this.id = id;
        this.group = group;
        this.obj = obj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
