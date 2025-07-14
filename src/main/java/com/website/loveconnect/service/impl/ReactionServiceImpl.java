package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.request.ReactionRequest;
import com.website.loveconnect.dto.response.PostResponse;
import com.website.loveconnect.entity.Emotion;
import com.website.loveconnect.entity.Post;
import com.website.loveconnect.entity.Reaction;
import com.website.loveconnect.entity.User;
import com.website.loveconnect.exception.DataAccessException;
import com.website.loveconnect.exception.EmotionNotFoundException;
import com.website.loveconnect.exception.PostNotFoundException;
import com.website.loveconnect.exception.UserNotFoundException;
import com.website.loveconnect.repository.EmotionRepository;
import com.website.loveconnect.repository.PostRepository;
import com.website.loveconnect.repository.ReactionRepository;
import com.website.loveconnect.repository.UserRepository;
import com.website.loveconnect.service.ReactionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Override
    public void addReaction(ReactionRequest reactionRequest, Integer userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User Not Found"));
            Emotion emotion = emotionRepository.findByEmotionName(reactionRequest.getEmotionName())
                    .orElseThrow(()->new EmotionNotFoundException("Emotion Not Found"));
            Reaction reaction = Reaction.builder()
                    .user(user)
                    .emotion(emotion)
                    .contentId(reactionRequest.getContentId())
                    .contentType(reactionRequest.getContentReact())
                    .build();
            reactionRepository.save(reaction);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long countReactionOnAPost(Integer postId) {
        try{
            Post post = postRepository.findById(postId).orElseThrow(()->new PostNotFoundException("Post Not Found"));
            if(post!=null){
                return reactionRepository.countReactionOnAPost(post.getId());
            }else return null;
        }catch (DataAccessException da){
            throw  new DataAccessException("Cannot access database");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
