package fact.it.roomservice.repository;

import fact.it.roomservice.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    List<Room> findByRoomCodeInAndSizeGreaterThan(List<String> roomCodes, int size);
}
