package com.positivarium.api.service;

import com.positivarium.api.dto.ArticleDTO;
import com.positivarium.api.entity.Article;
import com.positivarium.api.entity.User;
import com.positivarium.api.exception.ResourceNotFoundException;
import com.positivarium.api.mapping.ArticleMapping;
import com.positivarium.api.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Automatically initialise mocks, using JUnit and Mockito
// Mockito simulates other methods that are called in tested method
// JUnit actually tests it
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    // These are simulated
    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserService userService;

    @Mock
    private ArticleMapping articleMapping;

    // this is the actual service but mocks are injected
    // nothing but the service will be tested
    @InjectMocks
    private ArticleService articleService;

    private Authentication authentication;
    private User user;
    private Article article;

    // scenario variables
    private final Long userId = 3L;
    private final Long articleId = 1L;
    private final String username = "claire";

    @BeforeEach
    void setup() {
        // Fake authentication, simulates user connection
        // returns "claire" when getName() is called
        authentication = mock(Authentication.class);
        // lenient() is used to override stubs in tests without Mockito throwing UnnecessaryStubbingException
        lenient().when(authentication.isAuthenticated()).thenReturn(true);
        lenient().when(authentication.getName()).thenReturn(username);

        // Simulates userService.getUser(username) returning user with id 3
        user = User.builder().id(userId).build();
        lenient().when(userService.getCurrentUser(authentication)).thenReturn(user);
        // simulates that user liked
        lenient().when(articleRepository.userLikedArticle(userId, articleId)).thenReturn(true);

        // simulates that published article was found in DB
        article = Article.builder().id(articleId).build();
        lenient().when(articleRepository.findByIdAndIsPublishedTrue(articleId)).thenReturn(Optional.of(article));
        // simulates that article has 10 likes
        lenient().when(articleRepository.countLikesByArticleId(articleId)).thenReturn(10L);
    }


    @Test
    void testGetArticleById_success() {
        // override authentication because getArticleById does not call getCurrentUser
        when(userService.getUser(username)).thenReturn(user);

        // simulates that mapping returns DTO
        ArticleDTO dto = ArticleDTO.builder().id(articleId).userLiked(true).likesCount(10L).build();
        when(articleMapping.entityToDtoWithLikes(article, 10L, true)).thenReturn(dto);

        // Act
        // Calling method that I want to test
        ArticleDTO result = articleService.getArticleById(articleId, authentication);

        // Assert
        // Checks if it returns expected response (here, the DTO I built earlier)
        assertEquals(dto, result);
    }

    @Test
    void testGetArticleById_returns404(){
        // override articleRepository.findByIdAndIsPublishedTrue returning nothing
        when(articleRepository.findByIdAndIsPublishedTrue(articleId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            articleService.getArticleById(articleId, authentication);
        });
    }

    @Test
    void testCreateArticle_success(){
        ArticleDTO inputDTO = ArticleDTO.builder()
                .title("Nouveau titre")
                .content("Contenu de l'article")
                .build();

        Article articleFromDTO = Article.builder()
                .title(inputDTO.title())
                .content(inputDTO.content())
                .build();
        when(articleMapping.dtoToEntity(inputDTO)).thenReturn(articleFromDTO);

        Article savedArticle = Article.builder()
                .id(100L)
                .title(articleFromDTO.getTitle())
                .content(articleFromDTO.getContent())
                .user(user)
                .isPublished(false)
                .publishedAt(null)
                .build();
        when(articleRepository.save(articleFromDTO)).thenReturn(savedArticle);

        ArticleDTO outputDTO = ArticleDTO.builder()
                .id(100L)
                .title(savedArticle.getTitle())
                .content(savedArticle.getContent())
                .build();
        when(articleMapping.entityToDto(savedArticle)).thenReturn(outputDTO);

        // Act
        ArticleDTO result = articleService.createArticle(inputDTO, authentication);

        // Assert
        assertEquals(outputDTO, result);

        // Check if article was explicitly modified in method before save
        // articleFromDTO is last variable before save
        // To avoid forgetting if modifying method
        assertEquals(user, articleFromDTO.getUser());
        assertFalse(articleFromDTO.isPublished());
        assertNull(articleFromDTO.getPublishedAt());

        // Check if mocked methods were actually called
        // To avoid forgetting if modifying method
        verify(userService).getCurrentUser(authentication);
        verify(articleMapping).dtoToEntity(inputDTO);
        verify(articleRepository).save(articleFromDTO);
        verify(articleMapping).entityToDto(savedArticle);

        // Verify can also check that a method was NOT called in a certain scenario
        // Or that mocked class did not receive unwanted call
    }
}
