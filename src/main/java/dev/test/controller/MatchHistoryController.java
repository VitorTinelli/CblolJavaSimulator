package dev.test.controller;

import dev.test.service.MatchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/match-history")
@RequiredArgsConstructor
public class MatchHistoryController {

  private final MatchHistoryService matchHistoryService;

  @GetMapping
  public void listAll() {
    matchHistoryService.listAll();
  }
}
