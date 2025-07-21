package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.ReactionRequest;
import com.website.loveconnect.dto.response.PostResponse;
import com.website.loveconnect.dto.response.ReactionResponse;
import com.website.loveconnect.entity.Emotion;
import com.website.loveconnect.entity.Post;
import com.website.loveconnect.entity.Reaction;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.enumpackage.ContentType;
import com.website.loveconnect.exception.*;
import com.website.loveconnect.mapper.ReactionMapper;
import com.website.loveconnect.repository.EmotionRepository;
import com.website.loveconnect.repository.PostRepository;
import com.website.loveconnect.repository.ReactionRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.ReactionService;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ReactionServiceImpl implements ReactionService {
    UserRepository userRepository;
    ReactionRepository reactionRepository;
    EmotionRepository emotionRepository;
    PostRepository postRepository;
    ReactionMapper reactionMapper;


    @Override
    public void addReaction(ReactionRequest reactionRequest, Integer userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User Not Found"));
            Emotion emotion = emotionRepository.findByEmotionName(reactionRequest.getEmotionName())
                    .orElseThrow(()->new EmotionNotFoundException("Emotion Not Found"));
            //kiểm tra xem đã thả reaction trước đó chưa
            Reaction checkReact = reactionRepository.findByUserAndContentIdAndContentType(user,
                    reactionRequest.getContentId(),reactionRequest.getContentReact());
            if (checkReact != null) {
                checkReact.setEmotion(emotion);
                checkReact.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                reactionRepository.save(checkReact);
            }else {
                Reaction reaction = Reaction.builder()
                        .user(user)
                        .emotion(emotion)
                        .contentId(reactionRequest.getContentId())
                        .contentType(reactionRequest.getContentReact())
                        .updatedAt(new Timestamp(System.currentTimeMillis()))
                        .build();
                reactionRepository.save(reaction);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long countReactionOnAPost(Integer postId) {
        try{
            Post post = postRepository.findById(postId).orElseThrow(()->new PostNotFoundException("Post Not Found"));
            if(post!=null){
                return reactionRepository.countReactionOnAPost(post.getPostId());
            }else return null;
        }catch (DataAccessException da){
            throw  new DataAccessException("Cannot access database");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ReactionResponse countReactionAndCheckUserReact(Integer postId, Integer userId) {
        try{
            Tuple tuple =  reactionRepository.countReactionAndCheckUserReaction(postId,userId)
                    .orElseThrow(()-> new DataAccessException("Cannot access database"));
            return reactionMapper.toReactionResponse(tuple);
        }catch (DataAccessException da){
            throw new DataAccessException("Cannot access database");
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    public void deleteReaction(Integer postId, Integer userId, ContentType contentType) {
        try{
            User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User Not Found"));
            Reaction reaction =  reactionRepository.findByUserAndContentIdAndContentType(user,postId,contentType);
            if(reaction!=null){
                reactionRepository.delete(reaction);
            }else throw new DataAccessException("Cannot delete reaction");
        }catch (DataAccessException da){
            throw new DataAccessException("Cannot access database");
        }
    }
}
