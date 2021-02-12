package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Comment;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.CommentRepository;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.CommentRequest;
import com.softserve.itacademy.response.CommentResponse;
import com.softserve.itacademy.service.converters.CommentConverter;
import com.softserve.itacademy.service.implementation.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentConverter commentConverter;
    @Mock
    private MaterialRepository materialRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CommentServiceImpl commentServiceImpl;

    @BeforeEach
    void initialize() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateValidComment() {
        when(materialRepository.findById(anyInt())).thenReturn(Optional.of(generateMaterial()));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(generateUser()));
        when(commentConverter.of(any(CommentRequest.class), any(Material.class), any(User.class))).thenReturn(generateComment());
        when(commentRepository.save(any(Comment.class))).thenReturn(generateComment());
        when(commentConverter.of(any(Comment.class))).thenReturn(generateCommentResponse());

        CommentResponse result = commentServiceImpl.create(generateCommentRequest());

        assertEquals(1, result.getOwnerId());
        assertEquals("Message", result.getMessage());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
    @Test
    void testCreateThrowsMaterialNotFoundException() {
        when(materialRepository.findById(anyInt())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> commentServiceImpl.create(generateCommentRequest()));
        verify(materialRepository, times(1)).findById(anyInt());
    }
    @Test
    void testCreateThrowsUserNotFoundException() {
        when(materialRepository.findById(anyInt())).thenReturn(Optional.of(generateMaterial()));
        when(userRepository.findById(anyInt())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> commentServiceImpl.create(generateCommentRequest()));
        verify(materialRepository, times(1)).findById(anyInt());
        verify(userRepository, times(1)).findById(anyInt());
    }
    @Test
    void testReadById() {
        when(commentRepository.findById(anyInt())).thenReturn(Optional.of(generateComment()));
        when(commentConverter.of(any(Comment.class))).thenReturn(generateCommentResponse());

        CommentResponse result = commentServiceImpl.readById(1);

        assertEquals(1, result.getOwnerId());
        assertEquals("Message", result.getMessage());
        verify(commentRepository, times(1)).findById(anyInt());
    }
    @Test
    void testReadByIdThrowsNotFoundException() {
        when(commentRepository.findById(anyInt())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> commentServiceImpl.readById(1));
        verify(commentRepository, times(1)).findById(anyInt());
    }
    @Test
    void testFindByOwner() {
        when(commentRepository.findByOwnerId(anyInt())).thenReturn(List.of(generateComment()));
        when(commentConverter.of(any(Comment.class))).thenReturn(generateCommentResponse());

        List<CommentResponse> result = commentServiceImpl.findByOwner(1);

        assertEquals(1, result.size());
        verify(commentRepository, times(1)).findByOwnerId(anyInt());

    }
    @Test
    void testFindByOwnerReturnEmpty() {
        when(commentRepository.findByOwnerId(anyInt())).thenReturn(Collections.emptyList());

        List<CommentResponse> result = commentServiceImpl.findByOwner(1);

        assertEquals(0, result.size());
        verify(commentRepository, times(1)).findByOwnerId(anyInt());
    }
    @Test
    void testFindByMaterial() {
        when(commentRepository.findByMaterialId(anyInt())).thenReturn(List.of(generateComment()));
        when(commentConverter.of(any(Comment.class))).thenReturn(generateCommentResponse());

        List<CommentResponse> result = commentServiceImpl.findByMaterial(1);

        assertEquals(1, result.size());
        verify(commentRepository, times(1)).findByMaterialId(anyInt());

    }
    @Test
    void testFindByMaterialReturnEmpty() {
        when(commentRepository.findByMaterialId(anyInt())).thenReturn(Collections.emptyList());

        List<CommentResponse> result = commentServiceImpl.findByMaterial(1);

        assertEquals(0, result.size());
        verify(commentRepository, times(1)).findByMaterialId(anyInt());
    }

    private Material generateMaterial() {
        return Material.builder()
                .name("Material")
                .ownerId(1)
                .build();
    }
    private User generateUser() {
        return User.builder()
                .name("User")
                .password("pass")
                .email("user@mail.com")
                .build();
    }
    private Comment generateComment() {
        return Comment.builder()
                .message("Message")
                .owner(generateUser())
                .material(generateMaterial())
                .build();
    }
    private CommentResponse generateCommentResponse() {
        return CommentResponse.builder()
                .message("Message")
                .ownerId(1)
                .materialId(1)
                .build();
    }
    private CommentRequest generateCommentRequest() {
        return CommentRequest.builder()
                .message("Message")
                .ownerId(1)
                .materialId(1)
                .build();
    }
}
