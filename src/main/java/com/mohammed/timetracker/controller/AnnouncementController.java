package com.mohammed.timetracker.controller;

import com.mohammed.timetracker.model.Announcement;
import com.mohammed.timetracker.model.User;
import com.mohammed.timetracker.repository.AnnouncementRepository;
import com.mohammed.timetracker.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;

    public AnnouncementController(AnnouncementRepository announcementRepository,
                                  UserRepository userRepository) {
        this.announcementRepository = announcementRepository;
        this.userRepository = userRepository;
    }

    // All authenticated users can read announcements
    @GetMapping
    public ResponseEntity<List<Announcement>> getAll() {
        return ResponseEntity.ok(announcementRepository.findAllByOrderByCreatedAtDesc());
    }

    // Only ADMIN and TEAM_LEAD can post announcements
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEAM_LEAD') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<Announcement> create(@RequestBody Announcement announcement,
                                               Authentication auth) {
        User poster = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        announcement.setCreatedBy(poster);
        announcement.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(announcementRepository.save(announcement));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEAM_LEAD')")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        Announcement ann = announcementRepository.findById(id)
                .orElse(null);
        if (ann == null) return ResponseEntity.notFound().build();
        // TEAM_LEAD can only delete their own announcements
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            User user = userRepository.findByUsername(auth.getName()).orElseThrow();
            if (ann.getCreatedBy() == null || !ann.getCreatedBy().getId().equals(user.getId())) {
                return ResponseEntity.status(403).build();
            }
        }
        announcementRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
