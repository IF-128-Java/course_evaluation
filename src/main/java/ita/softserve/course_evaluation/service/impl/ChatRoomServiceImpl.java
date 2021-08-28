package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.entity.ChatRoom;
import ita.softserve.course_evaluation.repository.ChatRoomRepository;
import ita.softserve.course_evaluation.service.ChatRoomService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public ChatRoom getById(Long id) {
        return chatRoomRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("ChatRoom with id: %d not found!", id)));
    }
}