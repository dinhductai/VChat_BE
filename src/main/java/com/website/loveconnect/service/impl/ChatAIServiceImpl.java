package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.response.ChatAIResponse;
import com.website.loveconnect.exception.PromptEmptyException;
import com.website.loveconnect.service.ChatAIService;
import com.website.loveconnect.util.ChatAIUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.content.Media;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatAIServiceImpl implements ChatAIService {
    ChatClient chatClient;
    public ChatAIServiceImpl(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultSystem(ChatAIUtil.SYSTEM_PROMPT)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).build();
    }


    @Override
    public List<ChatAIResponse> chat(String message, MultipartFile file,String conversationId, Integer userId){
        try {
            List<Media> mediaList = new ArrayList<>();
            if (file != null && !file.isEmpty()) {
                mediaList.add(new Media(
                        MimeTypeUtils.parseMimeType(Objects.requireNonNull(file.getContentType())),
                        file.getResource()
                ));
            }
            ChatAIUtil.checkMessageAndMediaIsNull(message,mediaList);
            return chatClient.prompt()
                    .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId)) // <-- Truyền ID
                    .user(userSpec -> {
                        if (message != null && !message.isBlank()) {
                            userSpec.text(message);
                        }
                        //thêm danh sách media (chỉ khi danh sách không rỗng)
                        if (mediaList != null && !mediaList.isEmpty()) {
                            //chuyển List<Media> thành mảng Media[]
                            userSpec.media(mediaList.toArray(new Media[0]));
                        }
                    })
                    .call()
                    //convert dữ liệu text thành dl response
                    .entity(new ParameterizedTypeReference<List<ChatAIResponse>>() {
                    });
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
