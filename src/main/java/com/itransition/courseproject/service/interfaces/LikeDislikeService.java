package com.itransition.courseproject.service.interfaces;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface LikeDislikeService {

    String addLikeDislike(boolean isLike, Integer itemId, RedirectAttributes ra);

    int likesCount(Integer id);

    int disLikesCount(Integer id);

    String viewLikeDislike(boolean b, Integer itemId, Model model);

    int getTotalLikesByUserId();

    int getTotalDisLikesByUserId();
}
