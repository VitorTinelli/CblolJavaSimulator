package dev.test.service;

import dev.test.repository.MatchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchHistoryService {

  private final MatchHistoryRepository matchHistoryRepository;

  public void listAll() {
    matchHistoryRepository.findAll();
  }
}
