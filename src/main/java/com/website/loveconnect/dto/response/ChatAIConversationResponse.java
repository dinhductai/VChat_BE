package com.website.loveconnect.dto.response;

import java.util.List;

public record ChatAIConversationResponse(String conversationId, List<ChatAIResponse> listMessage) {
}
