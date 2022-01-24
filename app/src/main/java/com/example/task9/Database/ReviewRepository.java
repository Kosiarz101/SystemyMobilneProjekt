package com.example.task9.Database;

import android.app.Application;

import com.example.task9.Models.DatabaseModels.Review;

public class ReviewRepository {
    private ReviewDao reviewDao;

    public ReviewRepository(Application application) {
        DatabaseInstance instance = DatabaseInstance.getInstance(application);
        reviewDao = instance.reviewDao();
    }
    public void insert(Review review) {
        reviewDao.insert(review);
    }

    public void update(Review review) {
        reviewDao.update(review);
    }

    public void delete(Review review) {
        reviewDao.delete(review);
    }

    public void deleteAll(){reviewDao.deleteAll();}

    public Review findReviewByBookId(String bookId) {
        return reviewDao.findReviewByBookId(bookId);
    }
}
