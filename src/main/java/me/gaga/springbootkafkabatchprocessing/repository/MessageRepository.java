package me.gaga.springbootkafkabatchprocessing.repository;

import me.gaga.springbootkafkabatchprocessing.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
