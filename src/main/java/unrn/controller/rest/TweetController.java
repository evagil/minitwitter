package unrn.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unrn.dto.CreateRetweetRequest;
import unrn.dto.RetweetDetailsDto;
import unrn.dto.TweetDto;
import unrn.mapper.TweetMapper;
import unrn.model.Tweet;
import unrn.model.User;
import unrn.services.TweetService;
import unrn.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tweets")
@CrossOrigin
public class TweetController {

    private final TweetService tweetService;
    private final UserService userService;

    public TweetController(TweetService tweetService, UserService userService) {
        this.tweetService = tweetService;
        this.userService = userService;
    }

    @PostMapping("/crear")
    public TweetDto crearTweet(
            @RequestParam int userId,
            @RequestParam String texto
    ) {
        User user = userService.buscarPorId(userId);
        Tweet tweet = tweetService.crearTweet(user, texto);
        return TweetMapper.toDto(tweet);
    }

    @PostMapping("/retweet")
    public ResponseEntity<?> retweet(@RequestBody CreateRetweetRequest request) {
        User user = userService.buscarPorId(request.getUserId());
        if (user == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }
        Tweet original = tweetService.buscarTweetPorId(request.getTweetId());
        if (original == null) {
            return ResponseEntity.badRequest().body("Tweet no encontrado");
        }
        Tweet rt = tweetService.hacerRetweet(user, original, request.getComentario());
        return ResponseEntity.ok(TweetMapper.toDto(rt));
    }

    @GetMapping("/timeline")
    public List<TweetDto> timeline(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return tweetService
                .obtenerTodosLosTweets(offset, limit)
                .stream()
                .map(TweetMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/de-usuario/{userId}")
    public List<TweetDto> tweetsDeUsuario(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "15") int limit
    ) {
        User user = userService.buscarPorId(userId);
        return tweetService
                .obtenerTweetsDeUsuarioConPaginacion(user, offset, limit)
                .stream()
                .map(TweetMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/retweets")
    public List<RetweetDetailsDto> listarRetweets(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return tweetService.listarRetweets(offset, limit);
    }
}

