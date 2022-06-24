package com.itransition.courseproject.service.interfaces;

import com.itransition.courseproject.dto.CommentDto;
import com.itransition.courseproject.entity.collection.Comment;
import com.itransition.courseproject.projection.CommentProjection;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommentService {

    String saveData(Comment comment, RedirectAttributes ra, Integer itemId);

    List<CommentProjection> getCommentsByItemId(Integer id);

    int getTotalCommentsByUserId();
}
