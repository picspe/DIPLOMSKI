package main.dao;

import main.models.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface MessageRepository extends CrudRepository<Message, Long>{
    Collection<Message> findAllByReceiverMail(String receiverMail);
    Collection<Message> findAllBySenderMail(String senderMail);
}
