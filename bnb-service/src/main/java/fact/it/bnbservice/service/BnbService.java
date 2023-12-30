package fact.it.bnbservice.service;

import fact.it.bnbservice.dto.AvailableRoomRequest;
import fact.it.bnbservice.dto.BnbResponse;
import fact.it.bnbservice.dto.AvailableRoomResponse;
import fact.it.bnbservice.dto.BnbRequest;
import fact.it.bnbservice.model.Bnb;
import fact.it.bnbservice.repository.BnbRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BnbService {

    private final BnbRepository bnbRepository;

    private final WebClient webClient;

    @Value("${roomservice.baseurl}")
    private String roomServiceBaseUrl;

    @PostConstruct
    public void loadData() {
        if (bnbRepository.count() <= 0) {
            List<Bnb> bnbList = new ArrayList<>();

            Bnb bnb1 = new Bnb();
            bnb1.setName("Hugo's Bnb Ter Dolen");
            bnb1.addRoomCode("Room1_Bnb1");
            bnb1.addRoomCode("Room2_Bnb1");
            bnb1.addRoomCode("Room3_Bnb1");
            bnb1.setCity("Houthalen-Helchteren");
            bnb1.setPostcode("3530");
            bnb1.setAddress("Eikendreef 21");
            bnbList.add(bnb1);

            Bnb bnb2 = new Bnb();
            bnb2.setName("Hugo's Bnb Geel");
            bnb2.addRoomCode("Room1_Bnb2");
            bnb2.addRoomCode("Room2_Bnb2");
            bnb2.setCity("Geel");
            bnb2.setPostcode("2440");
            bnb2.setAddress("Drijhoek");
            bnbList.add(bnb2);

            Bnb bnb3 = new Bnb();
            bnb3.setName("The Slumburger ");
            bnb3.setCity("Geel");
            bnb3.setPostcode("2440");
            bnb3.setAddress("Antwerpseweg 117");
            bnbList.add(bnb3);

            Bnb bnb4 = new Bnb();
            bnb4.setName("SnoozeSnacks");
            bnb4.setCity("Hasselt");
            bnb4.setPostcode("3500");
            bnb4.setAddress("Grote Markt 15");
            bnbList.add(bnb4);

            bnbRepository.saveAll(bnbList);
        }
    }

    public List<AvailableRoomResponse> getAvailableRooms(AvailableRoomRequest roomRequest, Long bnbId) {
        // Get the bnb object from the given bnbId
        Optional<Bnb> bnbOptional = bnbRepository.findById(bnbId);

        if (bnbOptional.isPresent()) {
            Bnb bnb = bnbOptional.get();
            // Add the roomCodes of the found bnb to the roomRequest
            roomRequest.setRoomCodes(bnb.getRoomCodes());

            // Call the Room service to check the size of each room
            // This will in turn call Reservation service to ensure that rooms are available
            // during the given time period
            AvailableRoomResponse[] availableRoomResponseList = webClient.post()
                    .uri("http://" + roomServiceBaseUrl + "/api/room/availableRooms")
                    .bodyValue(roomRequest)
                    .retrieve()
                    .bodyToMono(AvailableRoomResponse[].class)
                    .block();
            return Arrays.stream(
                    availableRoomResponseList != null ? availableRoomResponseList : new AvailableRoomResponse[0])
                    .toList();
        }

        return null;
    }

    // CRUD
    @Transactional(readOnly = true)
    public List<BnbResponse> getBnbsByName(String str) {
        return bnbRepository.getBnbsByNameContains(str).stream()
                .map(bnb -> BnbResponse.builder()
                        .name(bnb.getName())
                        .roomCodes(bnb.getRoomCodes())
                        .build())
                .toList();
    }

    public BnbResponse getBnb(Long id) {
        Optional<Bnb> bnbOptional = bnbRepository.findById(id);
        if (bnbOptional.isPresent()) {
            Bnb bnb = bnbOptional.get();
            return BnbResponse.builder()
                    .id(bnb.getId())
                    .name(bnb.getName())
                    .roomCodes(bnb.getRoomCodes())
                    .city(bnb.getCity())
                    .postcode(bnb.getPostcode())
                    .address(bnb.getAddress())
                    .build();
        }
        return null;
    }

    public Bnb createBnb(BnbRequest bnbRequest) {
        Bnb bnb = new Bnb();
        bnb.setId(bnbRequest.getId());
        bnb.setName(bnbRequest.getName());
        bnb.setRoomCodes(bnbRequest.getRoomCodes());
        bnb.setCity(bnbRequest.getCity());
        bnb.setPostcode(bnbRequest.getPostcode());
        bnb.setAddress(bnbRequest.getAddress());

        bnbRepository.save(bnb);
        return bnb;
    }

    public Bnb updateBnb(Long id, BnbRequest updatedBnb) {
        Optional<Bnb> bnbOptional = bnbRepository.findById(id);

        if (bnbOptional.isPresent()) {
            Bnb bnb = bnbOptional.get();

            bnb.setId(updatedBnb.getId());
            bnb.setName(updatedBnb.getName() != null ? updatedBnb.getName() : bnb.getName());
            bnb.setRoomCodes(updatedBnb.getRoomCodes().size() != 0 ? updatedBnb.getRoomCodes() : bnb.getRoomCodes());
            bnb.setCity(updatedBnb.getCity() != null ? updatedBnb.getCity() : bnb.getCity());
            bnb.setPostcode(updatedBnb.getPostcode() != null ? updatedBnb.getPostcode() : bnb.getPostcode());
            bnb.setAddress(updatedBnb.getAddress() != null ? updatedBnb.getAddress() : bnb.getAddress());

            return bnbRepository.save(bnb);
        }
        return null; // Handle not found case
    }

    public void deleteBnb(Long id) {
        bnbRepository.deleteById(id);
    }
}