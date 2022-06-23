package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/23/22 6:17 PM


import com.itransition.courseproject.entity.collection.DisLike;
import com.itransition.courseproject.entity.collection.Item;
import com.itransition.courseproject.entity.collection.Like;
import com.itransition.courseproject.entity.user.User;
import com.itransition.courseproject.repository.DisLikeRepository;
import com.itransition.courseproject.repository.ItemRepository;
import com.itransition.courseproject.repository.LikeRepository;
import com.itransition.courseproject.service.interfaces.LikeDislikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

                int disliked = disLikeRepository.isDisliked(user.getId());
                Item item = itemRepository.findById(itemId).get();
                if (disliked>0){
                    DisLike disLikeData = disLikeRepository.findByUserAndItem(user, item);
                    disLikeRepository.delete(disLikeData);
                }

                Like like = new Like(
                        user,
                        item
                );
                likeRepository.save(like);
            } else if (user != null) {

                int likedByUser = likeRepository.isLikedByUser(user.getId());
                Item item = itemRepository.findById(itemId).get();

                if (likedByUser>0){
                    Like likeData = likeRepository.findByUserAndItem(user, item);
                    likeRepository.delete(likeData);
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

}
