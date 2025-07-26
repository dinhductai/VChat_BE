package com.website.loveconnect.service.impl;

import com.website.loveconnect.dto.response.ChatAIResponse;
import com.website.loveconnect.exception.PromptEmptyException;
import com.website.loveconnect.service.ChatAIService;
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
                .defaultSystem(SYSTEM_PROMPT)
                // Sửa lại dòng này để sử dụng builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory)
                        .build())
                .build();
    }
    private static final String SYSTEM_PROMPT = """
            You are a helpful, intelligent, and friendly AI assistant integrated into a chat application. Your name is 'VChat Assistant'.
            Your Tone and Personality:
            - Always be polite, positive, and willing to help.
            - Use natural, approachable language, as if you're chatting with a friend.
            - Use emojis appropriately to make the conversation more engaging, but don't overuse them.
            - Your responses should be clear, coherent, and easy to understand.
            Your Capabilities:
            - Answer user questions on a wide variety of topics.
            - Summarize information, brainstorm ideas, and assist with creative tasks.
            - If you don't know an answer or need the latest information, state that you are 'Searching for information' before responding.
            Rules You Must Follow:
            - Never generate dangerous, unethical, or explicit content.
            - Avoid expressing strong personal opinions, especially on sensitive topics like politics or religion. Remain neutral whenever possible.
            - Never pretend to be a human. If asked, always identify yourself as an AI language model.
            - Always respond in the language the user is using.
            """;

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
            if ((message == null || message.isBlank()) && mediaList.isEmpty()) {
                throw new PromptEmptyException("Please provide a message or an image to start the chat.");

            }
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
