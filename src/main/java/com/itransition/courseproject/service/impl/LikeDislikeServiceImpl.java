package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/23/22 6:17 PM


import com.itransition.courseproject.entity.collection.DisLike;
import com.itransition.courseproject.entity.collection.Item;
import com.itransition.courseproject.entity.collection.Like;
import com.itransition.courseproject.entity.user.User;
import com.itransition.courseproject.projection.LikeDisLikeProjection;
import com.itransition.courseproject.repository.DisLikeRepository;
import com.itransition.courseproject.repository.ItemRepository;
import com.itransition.courseproject.repository.LikeRepository;
import com.itransition.courseproject.service.interfaces.LikeDislikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeDislikeServiceImpl implements LikeDislikeService {

    private final LikeRepository likeRepository;
    private final DisLikeRepository disLikeRepository;
    private final UserServiceImpl userService;
    private final ItemRepository itemRepository;

    @Override
    public String addLikeDislike(boolean isLike, Integer itemId, RedirectAttributes ra) {

        try {
            User user = userService.currenUser();
            if (user != null && isLike && itemRepository.findById(itemId).isPresent()) {

                int disliked = disLikeRepository.isDisliked(user.getId(), itemId);
                Item item = itemRepository.findById(itemId).get();
                if (disliked > 0) {
                    DisLike disLikeData = disLikeRepository.findByUserAndItem(user, item);
                    disLikeRepository.deleteById(disLikeData.getId());
                }

                Like like = new Like(
                        user,
                        item
                );
                likeRepository.save(like);
            } else if (user != null) {

                int likedByUser = likeRepository.isLikedByUser(user.getId(), itemId);
                Item item = itemRepository.findById(itemId).get();

                if (likedByUser > 0) {
                    Like likeData = likeRepository.findByUserAndItem(user, item);
                    likeRepository.deleteById(likeData.getId());
                }

                DisLike disLike = new DisLike(
                        user,
                        item
                );
                disLikeRepository.save(disLike);
            }
        } catch (Exception ignored) {
        }

        return "redirect:/item/detail/" + itemId;
    }

    @Override
    public int likesCount(Integer id) {
        return likeRepository.likeCountByItemId(id);
    }

    @Override
    public int disLikesCount(Integer id) {
        return disLikeRepository.disLikeCountByItemId(id);
    }

    @Override
    public String viewLikeDislike(boolean isLike, Integer itemId, Model model) {
        List<LikeDisLikeProjection> likeDislikeDetail = new ArrayList<>();
        String likeDislike = "";
        if (isLike) {
            likeDislikeDetail = likeRepository.getLikeDislikeDetail(itemId);
            likeDislike = "Likes";
        } else {
            likeDislikeDetail = disLikeRepository.getLikeDislikeDetail(itemId);
            likeDislike = "Dislikes";
        }

        model.addAttribute("likesDislikesUser", likeDislikeDetail);
        model.addAttribute("likeDislike", likeDislike);
        model.addAttribute("itemId", itemId);
        return "item/like-dislike-view";
    }

}
