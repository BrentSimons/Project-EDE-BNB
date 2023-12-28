package fact.it.bnbservice.service;

import fact.it.bnbservice.dto.BnbResponse;
import fact.it.bnbservice.model.Bnb;
import fact.it.bnbservice.repository.BnbRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BnbService {

    private final BnbRepository bnbRepository;

    @PostConstruct
    public void loadData() {
        if (bnbRepository.count() <= 0) {
            List<Bnb> bnbList = new ArrayList<>();

            Bnb bnb1 = new Bnb();
            bnb1.setName("Hugo's Bnb Ter Dolen");
            bnb1.addRoomId(1L);
            bnb1.addRoomId(2L);
            bnb1.addRoomId(3L);
            bnb1.setCity("Houthalen-Helchteren");
            bnb1.setPostcode("3530");
            bnb1.setAddress("Eikendreef 21");
            bnbList.add(bnb1);

            Bnb bnb2 = new Bnb();
            bnb2.setName("Hugo's Bnb Geel");
            bnb2.addRoomId(4L);
            bnb2.addRoomId(5L);
            bnb2.setCity("Geel");
            bnb2.setPostcode("2440");
            bnb2.setAddress("Drijhoek");
            bnbList.add(bnb2);

            Bnb bnb3 = new Bnb();
            bnb3.setName("The Slumburger ");
            bnb3.addRoomId(6L);
            bnb3.setCity("Geel");
            bnb3.setPostcode("2440");
            bnb3.setAddress("Antwerpseweg 117");
            bnbList.add(bnb3);

            Bnb bnb4 = new Bnb();
            bnb4.setName("SnoozeSnacks");
            bnb4.addRoomId(7L);
            bnb4.addRoomId(8L);
            bnb4.setCity("Hasselt");
            bnb4.setPostcode("3500");
            bnb4.setAddress("Grote Markt 15");
            bnbList.add(bnb4);

            bnbRepository.saveAll(bnbList);
        }
    }

    @Transactional(readOnly = true)
    public List<BnbResponse> getBnbsByName(String str) {
        return bnbRepository.getBnbsByNameContains(str).stream()
                .map(bnb -> BnbResponse.builder()
                        .name(bnb.getName())
                        .RoomsIdList(bnb.getRoomsIdList())
                        .build())
                .toList();
    }

    public List<BnbResponse> getAllBnbs() {
        return bnbRepository.findAll().stream()
                .map(bnb -> BnbResponse.builder()
                        .name(bnb.getName())
                        .RoomsIdList(bnb.getRoomsIdList())
                        .build())
                .toList();
    }
}
