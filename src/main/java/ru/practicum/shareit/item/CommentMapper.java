package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@Component
@AllArgsConstructor
public class CommentMapper {
    public CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getItem().getName(),
                comment.getAuthor().getName(), comment.getCreated());
    }
}
