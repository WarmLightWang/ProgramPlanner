package com.example.myapplication.element;

import java.util.List;
import java.util.Random;

public class Project {

    public long projectId;
    public String projectName;
    public String clientCode;
    public String devCode;
    public List<Application> applicationList;

    public Project(long projectId, String projectName, String clientCode, String devCode, List<Application> applicationList) {
        this(projectId, projectName, clientCode, devCode);
        this.applicationList = applicationList;
    }

    public Project(long projectId, String projectName, String clientCode, String devCode) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.clientCode = clientCode;
        this.devCode = devCode;
    }

    public Project(long projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }

    public Project() {
    }

    public static String generateCode(boolean isClient) {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);

            if (i == 5) {
                if (isClient) {
                    if (digit % 2 != 1)
                        digit = (digit + 1) % 10;
                } else {
                    if (digit % 2 != 0) {
                        digit = (digit + 1) % 10;
                    }
                }
            }
            code += digit;
        }
        return code;
    }

    public String toString() {
        return String.format("Name: %s, Id: %d, Client: %s, Dev: %s", projectName, projectId, clientCode, devCode);
    }
}
