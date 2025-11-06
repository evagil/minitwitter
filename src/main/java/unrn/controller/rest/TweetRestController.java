package unrn.controller.rest;

import unrn.dto.CreateTweetRequest;
import unrn.dto.TweetDTO;
import unrn.model.Tweet;
import unrn.model.User;
import unrn.services.TweetService;
import unrn.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tweets")
@CrossOrigin(origins = "*")
public class TweetRestController {

    private final TweetService tweetService;
    private final UserService userService;

    public TweetRestController(TweetService tweetService, UserService userService) {
        this.tweetService = tweetService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTweets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        int offset = page * size;
        List<Tweet> tweets = tweetService.obtenerTodosLosTweets(offset, size);
        int total = tweetService.contarTweets();
        
        List<TweetDTO> dtos = tweets.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("tweets", dtos);
        respuesta.put("total", total);
        respuesta.put("page", page);
        respuesta.put("size", size);
        respuesta.put("totalPages", (int) Math.ceil((double) total / size));
        
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> obtenerTweetsDeUsuario(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "15") int limit) {
        User user = userService.buscarPorId(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<Tweet> tweets = tweetService.obtenerTweetsDeUsuarioConPaginacion(user, offset, limit);
        List<Tweet> todosTweets = tweetService.obtenerTweetsDeUsuario(user);
        boolean hayMas = (offset + limit) < todosTweets.size();
        
        List<TweetDTO> dtos = tweets.stream()
                .map(this::convertirADTOConRetweet)
                .collect(Collectors.toList());
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("tweets", dtos);
        respuesta.put("hayMas", hayMas);
        respuesta.put("offset", offset);
        respuesta.put("limit", limit);
        
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping
    public ResponseEntity<?> crearTweet(@RequestBody CreateTweetRequest request) {
        try {
            User user = userService.buscarPorId(request.getUserId());
            if (user == null) {
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }
            Tweet tweet = tweetService.crearTweet(user, request.getTexto());
            return ResponseEntity.ok(convertirADTO(tweet));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{tweetId}/retweet")
    public ResponseEntity<?> hacerRetweet(
            @PathVariable int tweetId,
            @RequestParam(required = false) Integer userId,
            @RequestBody(required = false) unrn.dto.CreateRetweetRequest request) {
        try {
            // Si viene en el body, usar eso; si no, usar el query param (compatibilidad hacia atrás)
            int userIdFinal = (request != null && request.getUserId() > 0) 
                ? request.getUserId() 
                : (userId != null ? userId : 0);
            
            if (userIdFinal == 0) {
                return ResponseEntity.badRequest().body("userId es requerido");
            }

            User user = userService.buscarPorId(userIdFinal);
            if (user == null) {
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }
            Tweet tweetOriginal = tweetService.buscarTweetPorId(tweetId);
            if (tweetOriginal == null) {
                return ResponseEntity.badRequest().body("Tweet no encontrado");
            }
            
            // Si hay comentario en el request, usar retweet con comentario
            String comentario = (request != null) ? request.getComentario() : null;
            Tweet retweet;
            if (comentario != null && !comentario.trim().isEmpty()) {
                retweet = tweetService.hacerRetweetConComentario(user, tweetOriginal, comentario);
            } else {
                retweet = tweetService.hacerRetweet(user, tweetOriginal);
            }
            
            return ResponseEntity.ok(convertirADTOConRetweet(retweet));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private TweetDTO convertirADTO(Tweet tweet) {
        TweetDTO dto = new TweetDTO();
        dto.setId(tweet.getId());
        dto.setUserId(tweet.getAutor().getId());
        dto.setUserName(tweet.getAutor().getUserName());
        dto.setTexto(tweet.getTexto());
        dto.setEsRetweet(false);
        dto.setCreatedAt(convertirFecha(tweet.getCreatedAt()));
        return dto;
    }

    private TweetDTO convertirADTOConRetweet(Tweet tweet) {
        TweetDTO dto = convertirADTO(tweet);
        dto.setEsRetweet(tweet.esRetweet());
        
        if (tweet.esRetweet() && tweet.tweetDeOrigen() != null) {
            Tweet original = tweet.tweetDeOrigen();
            dto.setTweetOriginal(convertirADTO(original));
            dto.setUserIdRetweet(tweet.getAutor().getId());
            dto.setUserNameRetweet(tweet.getAutor().getUserName());
            dto.setCreatedAtRetweet(convertirFecha(tweet.getCreatedAt()));
        }
        
        return dto;
    }

    private LocalDateTime convertirFecha(Date fecha) {
        if (fecha == null) {
            return LocalDateTime.now();
        }
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}

