package com.vs2010wy.tool.service;

import com.vs2010wy.tool.model.ReviewTableModel;

public class ReviewWindowService {
    private final static Object LOCK = new Object();
    private ReviewTableModel reviewTable;

    public ReviewTableModel getReviewTableModel() {
        if (reviewTable == null) {
            synchronized (LOCK) {
                if (reviewTable == null) {
                    reviewTable = new ReviewTableModel();
                }
            }
        }

        return reviewTable;
    }
}
