package com.vs2010wy.tool.service;

import com.intellij.openapi.project.Project;
import com.vs2010wy.tool.model.ReviewTableModel;
import com.vs2010wy.tool.test.SimplePaginatedTable;

public class ReviewWindowService {
    private final static Object LOCK = new Object();
    private SimplePaginatedTable simplePaginatedTable;

    public SimplePaginatedTable getSimplePaginatedTable() {
        if (simplePaginatedTable == null) {
            synchronized (LOCK) {
                if (simplePaginatedTable == null) {
                    simplePaginatedTable = new SimplePaginatedTable();
                    simplePaginatedTable.init0();
                }
            }
        }

        return simplePaginatedTable;
    }
}
